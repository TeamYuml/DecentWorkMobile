package com.example.teamyuml.decentworkmobile.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class UserPanel extends AppCompatActivity {

    TextView name;
    TextView profession;
    TextView last_name;
    TextView city;
    TextView description;
    TextView phone;
    final static String USER_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);
        name = findViewById(R.id.name);
        last_name = findViewById(R.id.last_name);
        city = findViewById(R.id.city);
        profession = findViewById(R.id.profession);
        description = findViewById(R.id.description);
        phone = findViewById(R.id.phone);
        getUserData();
    }
    private void getUserData() {

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (
                Request.Method.GET, USER_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject user = response.getJSONObject("user");
                    String name_s = response.getString("name");
                    String last_name_s = response.getString("last_name");
                    String city_s = response.getString("city");
                    String phone_s = response.getString("phone");
                    String description_s = response.getString("description");
                    JSONArray professionsJson = (JSONArray) user.get("professions");
                    List<String> professions = new ArrayList<>();
                    for (int j = 0; j < professionsJson.length(); j++) {
                        professions.add(professionsJson.getString(j));
                    }
                    name.setText(name_s);
                    last_name.setText(last_name_s);
                    city.setText(city_s);
                    profession.setText((CharSequence) professions);
                    phone.setText(phone_s);
                    description.setText(description_s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserPanel.this,
                        "Coś poszło nie tak",
                        Toast.LENGTH_LONG).show();
            }
        });
        VolleyInstance.getInstance(this).addToRequestQueue(jsonObjectRequest, "UserPanel");
    }

}
