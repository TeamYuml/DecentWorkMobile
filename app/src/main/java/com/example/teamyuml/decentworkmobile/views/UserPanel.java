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
import com.example.teamyuml.decentworkmobile.utils.UserAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class UserPanel extends AppCompatActivity {

    private TextView name;
    private TextView profession;
    private TextView last_name;
    private TextView city;
    private TextView description;
    private TextView phone;
    private String USER_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);
        System.out.println(UserAuth.getToken(this));
        USER_URL = VolleyInstance.getBaseUrl()
            + "/profiles/userProfiles/" + UserAuth.getId(UserPanel.this) + "/";
        initializeTextViews();
        getUserData();
    }

    private void getUserData() {
        System.out.println("IDDD " + UserAuth.getId(this));
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (
                Request.Method.GET, USER_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    setAllTextViews(
                        getResponse(response),
                        getProfessions(response.getJSONArray("professions"))
                    );
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

    /**
     * Initialize all {@link TextView} from layout.
     */
    private void initializeTextViews() {
        name = findViewById(R.id.name);
        last_name = findViewById(R.id.last_name);
        city = findViewById(R.id.city);
        profession = findViewById(R.id.profession);
        description = findViewById(R.id.description);
        phone = findViewById(R.id.phone);
    }

    /**
     * Read response data except professions.
     * @param response - Response data.
     * @return Data about user.
     * @throws JSONException - When wrong json is passed.
     */
    private Map<String, String> getResponse(JSONObject response) throws JSONException {
        JSONObject user = response.getJSONObject("user");
        Map<String, String> profile = new HashMap<>();
        profile.put("name", user.getString("first_name"));
        profile.put("last_name", user.getString("last_name"));
        profile.put("city", response.getString("city"));
        profile.put("description", response.getString("description"));
        profile.put("phone", response.getString("phone_numbers"));

        return profile;
    }

    /**
     * Get user professions from response.
     * @param response Profession list.
     * @return List with professions names.
     * @throws JSONException - When wrong {@link JSONArray} is passed.
     */
    private List<String> getProfessions(JSONArray response) throws JSONException {
        List<String> professions = new ArrayList<>();

        for (int i = 0; i < response.length(); i++) {
            professions.add(response.getString(i));
        }

        return professions;
    }

    /**
     * Set text to all {@link TextView} from layout.
     * @param profile - Map with data about user.
     */
    private void setAllTextViews(Map<String, String> profile, List<String> professions) {
        name.setText(profile.get("name"));
        last_name.setText(profile.get("last_name"));
        city.setText(profile.get("city"));
        //profession.setText((CharSequence) professions);
        phone.setText(profile.get("phone"));
        description.setText(profile.get("description"));
    }
}
