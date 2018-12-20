package com.example.teamyuml.decentworkmobile.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.utils.CreateJson;
import com.example.teamyuml.decentworkmobile.utils.UserAuth;
import com.example.teamyuml.decentworkmobile.volley.ErrorHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AssignButtons extends Fragment implements View.OnClickListener {

    private Button assign;
    private Button remove;

    /**
     * Notice id.
     */
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.assign_buttons, container, false);

        Bundle bundle = getArguments();
        id = bundle.getInt("id");

        assign = v.findViewById(R.id.button_assign);
        assign.setOnClickListener(this);
        remove = v.findViewById(R.id.button_remove_assign);
        remove.setOnClickListener(this);

        if (UserAuth.getToken(getActivity()) != null) {
            checkAssign();
        } else {
            changeButtons(View.GONE, View.GONE);
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_assign:
                makeAssign();
                break;

            case R.id.button_remove_assign:
                removeAssign();
                break;
        }
    }

    /**
     * Assign user to currently displaying notice.
     */
    private void makeAssign() {
        final String ASSIGN_URL = VolleyInstance.getBaseUrl() + "/engagments/assign/user/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST, ASSIGN_URL, addParam(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                changeButtons(View.GONE, View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.errorHandler(error, getActivity());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return UserAuth.authorizationHeader(getActivity());
            }
        };

        VolleyInstance.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, "assign");
    }

    /**
     * Add current engagment id to request params.
     * @return JSONObject with request params.
     */
    private JSONObject addParam() {
        CreateJson cj = new CreateJson();
        cj.addInt("engagment", id);

        try {
            return cj.makeJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Remove assignment between notice and user.
     */
    private void removeAssign() {
        final String REMOVE_ASSIGN_URL = VolleyInstance.getBaseUrl()
            + "/engagments/assign/user/" + id + "/";

        StringRequest stringRequest = new StringRequest(
            Request.Method.DELETE, REMOVE_ASSIGN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                changeButtons(View.VISIBLE, View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.errorHandler(error, getActivity());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return UserAuth.authorizationHeader(getActivity());
            }
        };

        VolleyInstance.getInstance(getActivity()).addToRequestQueue(stringRequest, "removeAssign");
    }

    /**
     * Check if user already assign to notice.
     */
    private void checkAssign() {
        final String CHECK_ASSIGN_URL =
            VolleyInstance.getBaseUrl() + "/engagments/assign/check/?engagment=" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.GET, CHECK_ASSIGN_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("is_assigned")) {
                        changeButtons(View.GONE, View.VISIBLE);
                    } else {
                        changeButtons(View.VISIBLE, View.GONE);
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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return UserAuth.authorizationHeader(getActivity());
            }
        };

        VolleyInstance.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, "checkAssign");
    }

    /**
     * Change state of assign buttons.
     * @param assignValue - {@link View#GONE}/{@link View#VISIBLE} state.
     * @param removeValue - {@link View#GONE}/{@link View#VISIBLE} state.
     */
    private void changeButtons(int assignValue, int removeValue) {
        assign.setVisibility(assignValue);
        remove.setVisibility(removeValue);
    }
}
