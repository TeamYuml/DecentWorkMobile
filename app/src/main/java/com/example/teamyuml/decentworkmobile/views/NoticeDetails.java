package com.example.teamyuml.decentworkmobile.views;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NoticeDetails extends AppCompatActivity {

    String IdDetails;
    TextView title;
    TextView profession;
    TextView owner;
    TextView city;
    TextView description;
    TextView created;
    List<String> noticeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);
        IdDetails = getIntent().getStringExtra("choosenProfile");
        title = findViewById(R.id.title);
        profession = findViewById(R.id.profession);
        owner = findViewById(R.id.owner);
        city = findViewById(R.id.city);
        description = findViewById(R.id.description);
        created = findViewById(R.id.created);
        getNoticeDetails();
    }

    private void getNoticeDetails() {
        final String NOTICE_DETAIL_URL = VolleyInstance.getBaseUrl() + "/engagments/" + IdDetails + "/";

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
}