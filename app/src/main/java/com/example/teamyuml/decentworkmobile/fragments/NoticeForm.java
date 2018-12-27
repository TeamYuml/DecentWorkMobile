package com.example.teamyuml.decentworkmobile.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.utils.CreateJson;
import com.example.teamyuml.decentworkmobile.utils.UserAuth;
import com.example.teamyuml.decentworkmobile.volley.ErrorHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoticeForm extends Fragment implements View.OnClickListener {

    private Spinner noticeCity;
    private View v;
    private int dropDownLayout = android.R.layout.simple_dropdown_item_1line;
    private EditText noticeTitle;
    private EditText noticeDescription;
    private final String NOTICE_ADD_URL = VolleyInstance.getBaseUrl() + "/engagments/engagments/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.notice_form, container, false);
        noticeTitle = v.findViewById(R.id.title);
        noticeDescription = v.findViewById(R.id.description);
        v.findViewById(R.id.add_notice_btn).setOnClickListener(this);
        setCitySpinner();
        return v;
    }

    @Override
    public void onClick(View v) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, NOTICE_ADD_URL, addParams(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //TODO ADD INTENT TO USER NOTICES
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ErrorHandler.errorHandler(error, getActivity());
                }
            }) {
                public Map<String, String> getHeaders() {
                    return UserAuth.authorizationHeader(getActivity());
                }
            };

            VolleyInstance.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, "Create notice");
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "Nie można sparsować parametrów", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method is responsibility for adding params to engagment
     * @return JSONObject object
     */
    private JSONObject addParams() throws JSONException {
        String title = noticeTitle.getText().toString();
        String description = noticeDescription.getText().toString();
        String city = noticeCity.getSelectedItem().toString();

        CreateJson cj = new CreateJson();
        cj.addStr("city", city);
        cj.addStr("title" , title);
        cj.addStr("description", description);

        return cj.makeJSON();
    }

    /**
     * Initializes spinner with cities.
     */
    private void setCitySpinner() {
        noticeCity = v.findViewById(R.id.city);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity(), dropDownLayout, populateCities());
        arrayAdapter.setDropDownViewResource(dropDownLayout);
        noticeCity.setAdapter(arrayAdapter);
    }

    /**
     * Populates cities list.
     * @return List of Strings with cities names.
     */
    private List<String> populateCities() {
        List<String> cities = new ArrayList<>();
        cities.add("Łódź");
        cities.add("Poznań");
        cities.add("Wrocław");

        return cities;
    }
}
