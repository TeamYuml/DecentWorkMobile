﻿package com.example.teamyuml.decentworkmobile.views;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.utils.CreateJson;
import com.example.teamyuml.decentworkmobile.utils.UserAuth;
import com.example.teamyuml.decentworkmobile.volley.ErrorHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoticeDetails extends AppCompatActivity {

    private String IdDetails;
    private TextView title;
    private TextView profession;
    private TextView owner;
    private TextView city;
    private TextView description;
    private TextView created;
    private Button removeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);
        IdDetails = getIntent().getStringExtra("choosenProfile");
        initializeTextViews();
        getNoticeDetails();
        //TODO ADD TO CHECK IF USER IS ALREADY ASSIGNED
        removeButton = findViewById(R.id.remove_assign);
        removeButton.setVisibility(View.GONE);
    }

    /**
     * Button onClick which assign user to current viewing notice.
     * @param v
     */
    public void makeAssign(View v) {
        final String ASSIGN_URL = VolleyInstance.getBaseUrl() + "/engagments/assign/user/";

        JsonObjectRequest jsonObjectRequest = makeRequest(ASSIGN_URL, addParam());
        VolleyInstance.getInstance(this).addToRequestQueue(jsonObjectRequest, "assign");
    }

    /**
     * Button onClick for removing assign.
     * @param v
     */
    public void removeAssign(View v) {
        final String REMOVE_ASSIGN_URL = VolleyInstance.getBaseUrl()
            + "/engagments/assign/user/" + IdDetails + "/";

        JsonObjectRequest jsonObjectRequest = makeRequest(REMOVE_ASSIGN_URL, null);
        VolleyInstance.getInstance(this).addToRequestQueue(jsonObjectRequest, "removeAssign");
    }

    /**
     * Initialize text views.
     */
    private void initializeTextViews() {
        title = findViewById(R.id.title);
        profession = findViewById(R.id.profession);
        owner = findViewById(R.id.owner);
        city = findViewById(R.id.city);
        description = findViewById(R.id.description);
        created = findViewById(R.id.created);
    }

    private void getNoticeDetails() {
        final String NOTICE_DETAIL_URL = VolleyInstance.getBaseUrl() + "/engagments/engagments/" + IdDetails + "/";

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
                Toast.makeText(NoticeDetails.this,
                        "Coś poszło nie tak",
                        Toast.LENGTH_LONG).show();
                }
            });

        VolleyInstance.getInstance(this).addToRequestQueue(jsonObjectRequest, "noticeDetails");
    }

    private JsonObjectRequest makeRequest(String URL, JSONObject data) {
        return new JsonObjectRequest(
            Request.Method.DELETE, URL, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                changeButton();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.errorHandler(error, NoticeDetails.this);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return UserAuth.authorizationHeader(NoticeDetails.this);
            }
        };
    }

    /**
     * Add current engagment id to request params.
     * @return JSONObject with request params.
     */
    private JSONObject addParam() {
        CreateJson cj = new CreateJson();
        cj.addStr("engagment", IdDetails);

        try {
            return cj.makeJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Change state of buttons.
     */
    private void changeButton() {
        Button assignButton = findViewById(R.id.assign_button);

        if (assignButton.getVisibility() == View.GONE) {
            assignButton.setVisibility(View.VISIBLE);
            removeButton.setVisibility(View.GONE);
        } else {
            assignButton.setVisibility(View.GONE);
            removeButton.setVisibility(View.VISIBLE);
        }
    }
}
