package com.example.teamyuml.decentworkmobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.model.UserList;
import com.example.teamyuml.decentworkmobile.utils.UserAuth;
import com.example.teamyuml.decentworkmobile.views.NoticeDetails;
import com.example.teamyuml.decentworkmobile.views.WorkerDetails;
import com.example.teamyuml.decentworkmobile.volley.ErrorHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailsFragment extends Fragment {
    private View v;
    private TextView title;
    private TextView profession;
    private TextView owner;
    private TextView city;
    private TextView description;
    private TextView created;
    private int IdDetails;
    private ArrayAdapter<UserList> adapter;
    private ListView AssignedList;
    ArrayList<UserList> user_list = new ArrayList<>();
    private FragmentManager fragmentManager;
    private int assignContent = R.id.assign_buttons;
    private Button edit_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.notice_details, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();

        initializeComponents(v);

        edit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toEditNotice(v);
            }
        });
        edit_btn.setVisibility(View.GONE);

        IdDetails = getArguments().getInt("id");

        getNoticeDetails();
        initializeListView();

        adapter = new ArrayAdapter<UserList>(getActivity(), R.layout.assigned_row_style, user_list);
        AssignedList.setAdapter(adapter);

        toAssignedUser();

        return v;
    }

    private void initializeComponents(View v) {
        title = v.findViewById(R.id.title);
        profession = v.findViewById(R.id.profession);
        owner = v.findViewById(R.id.owner);
        city = v.findViewById(R.id.city);
        description = v.findViewById(R.id.description);
        created = v.findViewById(R.id.created);
        edit_btn = v.findViewById(R.id.edit_btn);
        AssignedList = v.findViewById(R.id.user_list);
    }

    private void getNoticeDetails() {
        final String NOTICE_DETAIL_URL =
            VolleyInstance.getBaseUrl() + "/notices/notices/" + IdDetails + "/";
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
                    if (!owner_s.equals(UserAuth.getEmail(getActivity()))) {
                        addFragment();
                    } else {
                        edit_btn.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.errorHandler(error, getActivity());
            }
        });

        VolleyInstance.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, "noticeDetails");
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
                ErrorHandler.errorHandler(error, getActivity());
            }
        });

        VolleyInstance.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest, "noticeDetails");
    }

    private void toAssignedUser() {
        AssignedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String clickedItem = String.valueOf(user_list.get(position).getId());
                Intent toWorker = new Intent(getActivity() , WorkerDetails.class);
                toWorker.putExtra("choosenProfile", (String) clickedItem);
                startActivity(toWorker);
            }
        });
    }

    /**
     * Adds assign buttons to activity.
     */
    private void addFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment assignButtons = new AssignButtons();
        assignButtons.setArguments(prepareBundle());
        fragmentTransaction.replace(assignContent, assignButtons);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void toEditNotice(View v) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment noticeForm = new NoticeForm();
        noticeForm.setArguments(prepareBundle());
        fragmentTransaction.replace(R.id.fragment_content, noticeForm);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Adds to fragment bundle with notice data.
     * @return Returns bundle with notice data.
     */
    private Bundle prepareBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("id", IdDetails);
        bundle.putString("initClass", "DetailsFragment");
        bundle.putString("title", String.valueOf(title.getText()));
        bundle.putString("description", String.valueOf(description.getText()));
        bundle.putString("city", String.valueOf(city.getText()));
        bundle.putString("profession", String.valueOf(profession.getText()));
        return bundle;
    }

}
