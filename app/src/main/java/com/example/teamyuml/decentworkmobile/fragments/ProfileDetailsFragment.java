package com.example.teamyuml.decentworkmobile.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.utils.UserAuth;
import com.example.teamyuml.decentworkmobile.volley.ErrorHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileDetailsFragment extends Fragment {

    private TextView name;
    private TextView profession;
    private TextView last_name;
    private TextView city;
    private TextView description;
    private TextView phone;
    private String USER_URL = VolleyInstance.getBaseUrl() + "/profiles/userProfiles/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View v = inflater.inflate(R.layout.fragment_profiles_details, container, false);
        Serializable id = (UserAuth.getId(getActivity()) != 0) ?
                           UserAuth.getId(getActivity()) :
                           getActivity().getIntent().getStringExtra("choosenProfile");

        USER_URL += id;
        name = v.findViewById(R.id.name);
        last_name = v.findViewById(R.id.last_name);
        city = v.findViewById(R.id.city);
        profession = v.findViewById(R.id.profession);
        description = v.findViewById(R.id.description);
        phone = v.findViewById(R.id.phone);
        getUserData();

        return v;
    }

    private void getUserData() {
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
                ErrorHandler.errorHandler(error, getActivity());
            }
        });

        VolleyInstance.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, "ProfileDetails");
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
        profession.setText(TextUtils.join(", ", professions));
        phone.setText(profile.get("phone"));
        description.setText(profile.get("description"));
    }
}
