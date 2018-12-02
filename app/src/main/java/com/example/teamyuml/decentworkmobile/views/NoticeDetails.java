package com.example.teamyuml.decentworkmobile.views;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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

    static String IdDetails;
    TextView textView;
    ArrayList noticeDetail = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);
        IdDetails = getIntent().getStringExtra("choosenNotice");
        textView = findViewById(R.id.textView);
        getNoticeDetails();
    }

    private void getNoticeDetails() {
        final String NOTICE_DETAIL_URL = VolleyInstance.getBaseUrl() + "/engagments/" + IdDetails + "/";
        System.out.print(NOTICE_DETAIL_URL);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest (Request.Method.GET, NOTICE_DETAIL_URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String title = null;
                        String profession = null;
                        Integer owner = null;
                        String city = null;
                        String description = null;
                        String created = null;
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject details = response.getJSONObject(i);
                                title = details.getString("title");
                                profession = details.getString("profession");
                                owner = details.getInt("owner");
                                city = details.getString("city");
                                description = details.getString("description");
                                created = details.getString("created");

                                ArrayList detailsData = new ArrayList();

                                detailsData.add(title);
                                detailsData.add(profession);
                                detailsData.add(owner);
                                detailsData.add(city);
                                detailsData.add(description);
                                detailsData.add(created);

                                noticeDetail.add(detailsData);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(noticeDetail == null) {
                            System.out.print("Lista jest pusta");
                        } else {
                            System.out.println(noticeDetail);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                }
            });
        VolleyInstance.getInstance(this).addToRequestQueue(jsonArrayRequest, "noticeDetails");
    }

}
