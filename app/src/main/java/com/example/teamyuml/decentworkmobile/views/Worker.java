package com.example.teamyuml.decentworkmobile.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Worker extends AppCompatActivity {

    ArrayList<HashMap<String, String>> workersAll;
    private ListView workersList;
    private static final String NOTICE_URL = VolleyInstance.getBaseUrl() + "//";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWorkers();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
        workersList = findViewById(R.id.workerList);
        workersAll = new ArrayList<>();
    }

    private void getWorkers() {
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, NOTICE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String name = null;
                String lastName = null;
                String profession = null;
                String city = null;
                Integer id = null;

                try {
                    JSONArray results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject notice = results.getJSONObject(i);
                        id = notice.getInt("id");
                        name = notice.getString("name");
                        lastName = notice.getString("lastName");
                        profession = notice.getString("profession");
                        city = notice.getString("city");
                        HashMap<String, String> oneNotice = new HashMap<>();
                        oneNotice.put("id", String.valueOf(id));
                        oneNotice.put("name", name);
                        oneNotice.put("lastName", lastName);
                        oneNotice.put("profession", profession);
                        oneNotice.put("city", city);

                        workersAll.add(oneNotice);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (workersAll != null) {
                    initNoticeList();
                    Toast.makeText(Worker.this,
                            "Pobrano " + workersAll.size() + " profili",
                            Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Worker.this,
                        "Coś poszło nie tak",
                        Toast.LENGTH_LONG).show();
            }
        });

        VolleyInstance.getInstance(this).addToRequestQueue(jsonObjectRequest, "noticeList");
    }

    /**
     * Set adapter to ListView and add clickListener event to get clicked data
     */
    private void initNoticeList() {
        CustomListView adapter = new CustomListView(this, workersAll);
        workersList.setAdapter(adapter);
    }
}
