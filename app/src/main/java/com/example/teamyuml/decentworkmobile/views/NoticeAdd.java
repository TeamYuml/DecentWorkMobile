package com.example.teamyuml.decentworkmobile.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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


/**
 * Class responsible for adding notice
 * Parsing data to JSON and sending to the server
 */
public class NoticeAdd extends AppCompatActivity implements View.OnClickListener {
    private EditText noticeTitle;
    private EditText noticeDescription;
    private Spinner noticeCity;
    private final String NOTICE_ADD_URL = VolleyInstance.getBaseUrl() + "/engagments/engagments/";
    private int dropDownLayout = android.R.layout.simple_dropdown_item_1line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engagment_form);
        noticeTitle = findViewById(R.id.title);
        noticeDescription = findViewById(R.id.description);
        findViewById(R.id.add_notice_btn).setOnClickListener(this);
        setCitySpinner();
    }

    /**
     * Makes request to server and add notice if success or shows
     * error when failure.
     */
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
                    ErrorHandler.errorHandler(error, NoticeAdd.this);
                }
            }) {
                public Map<String, String> getHeaders() {
                    return UserAuth.authorizationHeader(NoticeAdd.this);
                }
            };

            VolleyInstance.getInstance(this).addToRequestQueue(jsonObjectRequest, "Create notice");
        } catch (JSONException e) {
            Toast.makeText(this, "Nie można sparsować parametrów", Toast.LENGTH_LONG).show();
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
        noticeCity = findViewById(R.id.city);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
            NoticeAdd.this, dropDownLayout, populateCities());
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

