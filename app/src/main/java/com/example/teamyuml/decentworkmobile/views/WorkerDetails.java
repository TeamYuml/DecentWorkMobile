package com.example.teamyuml.decentworkmobile.views;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

public class WorkerDetails extends AppCompatActivity {
    String IdDetails;
    TextView name;
    TextView last_name;
    TextView city;
    TextView profession;
    TextView phone;
    TextView description;
    List<String> workerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_details);
        IdDetails = getIntent().getStringExtra("choosenProfile");
        name = findViewById(R.id.name);
        last_name = findViewById(R.id.last_name);
        city = findViewById(R.id.city);
        profession = findViewById(R.id.profession);
        description = findViewById(R.id.description);
        getWorkerDetails();
    }

    private void getWorkerDetails() {
        final String WORKER_DETAIL_URL = VolleyInstance.getBaseUrl() + "/profiles/userProfiles/" + IdDetails + "/";

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (
                Request.Method.GET, WORKER_DETAIL_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject user = response.getJSONObject("user");
                    String name_s = user.getString("first_name");
                    String last_name_s = user.getString("last_name");
                    String city_s = response.getString("city");
                    String phone_s = response.getString("phone_numbers");
                    String description_s = response.getString("description");
                    JSONArray professionsJson = (JSONArray) response.get("professions");
                    List<String> professions = new ArrayList<>();

                    for (int j = 0; j < professionsJson.length(); j++) {
                        professions.add(professionsJson.getString(j));
                    }

                    name.setText(name_s);
                    last_name.setText(last_name_s);
                    city.setText(city_s);
                    profession.setText(TextUtils.join(",",professions));
                    phone.setText(phone_s);
                    description.setText(description_s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WorkerDetails.this,
                        "Coś poszło nie tak",
                        Toast.LENGTH_LONG).show();
            }
        });
        VolleyInstance.getInstance(this).addToRequestQueue(jsonObjectRequest, "workerDetails");
    }
}
