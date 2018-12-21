package com.example.teamyuml.decentworkmobile.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.utils.CreateJson;
import com.example.teamyuml.decentworkmobile.utils.UserAuth;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Class responsible for adding engagments
 * Parsing data to JSON and sending to the server
 */
public class EngagmentAdd extends AppCompatActivity implements View.OnClickListener {
    private EditText engagment_title_input;
    private EditText engagment_description_input;
    private Spinner engagment_city;
    private Button sign_engagment;
    private TextView errorTextView;
    List<String> cities ;
    String selected_city;
    private static final String Engagment_add_URL = VolleyInstance.getBaseUrl() + "/engagments/engagments/";
    private static final String TAG = "Create engagment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engagment_form);
        engagment_title_input = findViewById(R.id.engagment_Title);
        engagment_description_input = findViewById(R.id.Engagment_Description);
        engagment_city = findViewById(R.id.Engagment_City);
        errorTextView = findViewById(R.id.ErrorTextView);
        cities = new ArrayList<String>();
        cities.add("Wybierz miasto");
        cities.add("Łódź");
        cities.add("Poznań");
        cities.add("Wrocław");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EngagmentAdd.this, android.R.layout.simple_dropdown_item_1line, cities);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        engagment_city.setAdapter(arrayAdapter);
        engagment_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_city = engagment_city.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        sign_engagment = findViewById(R.id.Engagment_Add);
        sign_engagment.setOnClickListener(this);
    }
    @Override
    /**
     * Makes request to server and add engagment if success or shows
     * error when failure.
     */
    public void onClick(View v)
    {
        JSONObject data = this.addParams();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
		Request.Method.POST, Engagment_add_URL, data, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String owner = response.getString("owner");
                    errorTextView.setText("pomyslnie dodano" + owner);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
              public void onErrorResponse(VolleyError error) {
                errorTextView.setText(error.getMessage());
                Log.e(TAG, "Adding Error: " + error.getMessage());
              }
        })
        {
            public Map<String, String> getHeaders() throws AuthFailureError {
                return UserAuth.authorizationHeader(EngagmentAdd.this);
            }
        };
        // Adding request to request queue
        VolleyInstance.getInstance(this).addToRequestQueue(jsonObjectRequest, "Create Engagment");
    }

    /**
     * Method is responsibility for adding params to engagment
     * @return CreateJson object
     */
    private JSONObject addParams() {	
        String engagment_title = engagment_title_input.getText().toString();
        String engagment_description = engagment_description_input.getText().toString();

        CreateJson cj = new CreateJson();
        cj.addStr("city", selected_city);
        cj.addStr("title" , engagment_title);
        cj.addStr("description", engagment_description);

        try {
            return cj.makeJSON();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
		
        return null;
    }
}

