package com.example.teamyuml.decentworkmobile.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.fragments.ListViewFragment;
import com.example.teamyuml.decentworkmobile.fragments.NoticeForm;
import com.example.teamyuml.decentworkmobile.utils.CreateJson;
import com.example.teamyuml.decentworkmobile.utils.UserAuth;
import com.example.teamyuml.decentworkmobile.volley.ErrorHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class EditNotice extends AppCompatActivity {
    FragmentManager fragmentManager;

    private String IdDetails;
    private TextView title;
    private TextView profession;
    private TextView owner;
    private TextView city;
    private TextView description;
    private TextView created;
    private Button save_btn;
    private final String USER_NOTICES_URL = VolleyInstance.getBaseUrl() + "/notices/user/notices/";
    private final String UPDATE_NOTICE_URL = VolleyInstance.getBaseUrl() + "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notice);

        IdDetails = getIntent().getStringExtra("choosenProfile");

        initToolbar();
        initializeLayoutComponents();
        getNoticeDetails();
    }

    private void initializeLayoutComponents() {
        title = findViewById(R.id.title);
        profession = findViewById(R.id.profession);
        owner = findViewById(R.id.owner);
        city = findViewById(R.id.city);
        description = findViewById(R.id.description);
        created = findViewById(R.id.created);
        save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveEditNotice(v);
            }
        });
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.errorHandler(error, EditNotice.this);
            }
        });

        VolleyInstance.getInstance(this).addToRequestQueue(jsonObjectRequest, "noticeDetails");
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void saveEditNotice(View v) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, UPDATE_NOTICE_URL+IdDetails, addParams(), new Response.Listener<JSONObject>() {
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
                    ErrorHandler.errorHandler(error, EditNotice.this);
                }
            }) {
                public Map<String, String> getHeaders() {
                    return UserAuth.authorizationHeader(EditNotice.this);
                }
            };

            VolleyInstance.getInstance(EditNotice.this).addToRequestQueue(jsonObjectRequest, "Create notice");
        } catch (JSONException e) {
            Toast.makeText(EditNotice.this, "Nie można sparsować parametrów", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method is responsibility for adding params to engagment
     * @return JSONObject object
     */
    private JSONObject addParams() throws JSONException {
        String titleNotice = title.getText().toString();
        String descriptionNotice = description.getText().toString();

        CreateJson cj = new CreateJson();
        cj.addStr("title" , titleNotice);
        cj.addStr("description", descriptionNotice);

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
}

