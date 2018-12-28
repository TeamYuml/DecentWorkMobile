package com.example.teamyuml.decentworkmobile.views;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.fragments.AssignButtons;
import com.example.teamyuml.decentworkmobile.fragments.ListViewFragment;
import com.example.teamyuml.decentworkmobile.model.UserList;
import com.example.teamyuml.decentworkmobile.utils.CreateJson;
import com.example.teamyuml.decentworkmobile.utils.UserAuth;
import com.example.teamyuml.decentworkmobile.volley.ErrorHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static com.example.teamyuml.decentworkmobile.R.layout.assigned_row_style;


public class NoticeDetails extends AppCompatActivity {

    private String IdDetails;
    private TextView title;
    private TextView profession;
    private TextView owner;
    private TextView city;
    private TextView description;
    private TextView created;
    private TextView user;
    private FragmentManager fragmentManager;
    private ArrayAdapter<UserList> adapter;
    private ListView AssignedList;
    private int assignContent = R.id.assign_buttons;
    private Button deleteButton;
    private String DELETE_URL = "/notices/notices/set_notice_done/?notice=";
    private final String USER_NOTICES_URL = VolleyInstance.getBaseUrl() + "/notices/user/notices/";
    ArrayList<UserList> user_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);
        fragmentManager = this.getSupportFragmentManager();
        IdDetails = getIntent().getStringExtra("choosenProfile");
        initializeLayoutComponents();
        getNoticeDetails();
        initializeListView();
        adapter = new ArrayAdapter<UserList>(this, R.layout.assigned_row_style, user_list);
        AssignedList.setAdapter(adapter);
        toAssignedUser();
    }

    /**
     * Initialize text views and list view for assigned users
     */
    private void initializeLayoutComponents() {
        title = findViewById(R.id.title);
        profession = findViewById(R.id.profession);
        owner = findViewById(R.id.owner);
        city = findViewById(R.id.city);
        description = findViewById(R.id.description);
        created = findViewById(R.id.created);
        AssignedList = findViewById(R.id.user_list);
        deleteButton = findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteNotice(v);
            }
        });
    }

    private void deleteNotice(View v) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, DELETE_URL+IdDetails, addParams(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Fragment notice = new ListViewFragment();

                    notice.setArguments(setParameters(
                        USER_NOTICES_URL,
                        R.id.noticeList,
                        R.layout.notice_list_view,
                        "getUserNotice",
                        "NoticeDetails"
                    ));

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ErrorHandler.errorHandler(error, NoticeDetails.this);
                }
            }) {
                public Map<String, String> getHeaders() {
                    return UserAuth.authorizationHeader(NoticeDetails.this);
                }
            };

            VolleyInstance.getInstance(NoticeDetails.this).addToRequestQueue(jsonObjectRequest, "Delete notice");
        } catch (JSONException e) {
            Toast.makeText(NoticeDetails.this, "Nie udało się usunąć ogłoszenia", Toast.LENGTH_LONG).show();
        }
    }

    private JSONObject addParams() throws JSONException {

        CreateJson cj = new CreateJson();
        cj.addStr("is_done", "True");
        return cj.makeJSON();
    }

    private Bundle setParameters(String url, int listViewId, int listLayoutId, String methodName, String initClass) {
        Bundle parameters = new Bundle();
        parameters.putString("url", url);
        parameters.putInt("listViewId", listViewId);
        parameters.putInt("listLayoutId", listLayoutId);
        parameters.putString("methodName", methodName);
        parameters.putString("initClass", initClass);
        return parameters;
    }

    private void getNoticeDetails() {
        final String NOTICE_DETAIL_URL = VolleyInstance.getBaseUrl() + "/notices/notices/" + IdDetails + "/";
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (
            Request.Method.GET, NOTICE_DETAIL_URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String title_s = response.getString("title");
                        String profession_s = response.getString("profession");
                        String owner_s = response.getString("owner");
                        String city_s = response.getString("city");
                        String description_s = response.getString("description");
                        String created_s = response.getString("created");
                        title.setText(title_s);
                        profession.setText(profession_s);
                        owner.setText(owner_s);
                        city.setText(city_s);
                        description.setText(description_s);
                        created.setText(created_s);
                        // Show assign buttons when notice do not belogns to currently logged user
                        if (!owner_s.equals(UserAuth.getEmail(NoticeDetails.this))) {
                            addFragment();
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                    }
                }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.errorHandler(error, NoticeDetails.this);
                }
            });

        VolleyInstance.getInstance(this).addToRequestQueue(jsonObjectRequest, "noticeDetails");
    }

    /**
     * Adds assign buttons to activity.
     */
    private void addFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment assignButtons = new AssignButtons();
        assignButtons.setArguments(addBundle());
        fragmentTransaction.replace(assignContent, assignButtons);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Adds to fragment bundle with notice id.
     * @return Returns bundle with notice id.
     */
    private Bundle addBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("id", Integer.parseInt(IdDetails));
        return bundle;
    }

    private void initializeListView() {
        final String ASSIGNED_NOTICE_URL = VolleyInstance.getBaseUrl() +
            "/notices/assign/list/?notice=" + IdDetails;

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest (
            Request.Method.GET, ASSIGNED_NOTICE_URL, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for(int i=0; i<response.length(); i++) {
                            JSONObject object = response.getJSONObject(i);

                            user_list.add(
                                new UserList(
                                    object.getString("email"),
                                    object.getInt("user")
                                )
                            );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ErrorHandler.errorHandler(error, NoticeDetails.this);
                }
            });

        VolleyInstance.getInstance(this).addToRequestQueue(jsonArrayRequest, "noticeDetails");
    }

    private void toAssignedUser() {
        AssignedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String clickedItem = String.valueOf(user_list.get(position).getId());
                Intent toWorker = new Intent(NoticeDetails.this , WorkerDetails.class);
                toWorker.putExtra("choosenProfile", (String) clickedItem);
                startActivity(toWorker);
            }
        });
    }
}
