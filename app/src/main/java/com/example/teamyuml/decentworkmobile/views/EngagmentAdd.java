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
    private EditText advertisment_title_input;
    private EditText advertisment_description_input;

    private Spinner advertisment_city;

    private Button sign_Advertisment;

    private TextView ErrorTextView;

    List<String> Cities ;

    String selected_city;

    private static final String Engagment_add_URL = VolleyInstance.getBaseUrl() + "/engagments/engagments/";
    private static final String TAG = "Create Advertisment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_form);
        advertisment_title_input = findViewById(R.id.Advertisment_Title);
        advertisment_description_input = findViewById(R.id.Advertiser_Description);
        advertisment_city = findViewById(R.id.Advertiser_City);
        ErrorTextView = findViewById(R.id.textView5);
        Cities = new ArrayList<String>();
        Cities.add("Wybierz miasto");
        Cities.add("Łódź");
        Cities.add("Poznań");
        Cities.add("Wrocław");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EngagmentAdd.this,android.R.layout.simple_dropdown_item_1line,Cities);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        advertisment_city.setAdapter(arrayAdapter);
        advertisment_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_city = advertisment_city.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        sign_Advertisment = findViewById(R.id.Advertiser_Add);
        sign_Advertisment.setOnClickListener(this);
    }

    @Override

    /**
     * Makes request to server and add engagment if success or shows
     * error when failure.
     */
    public void onClick(View v)
    {
        JSONObject data = this.addParams();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
        (Request.Method.POST, Engagment_add_URL, data, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    String owner = response.getString("owner");
                    ErrorTextView.setText("pomyslnie dodano" + owner);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {

            @Override
              public void onErrorResponse(VolleyError error) {
                ErrorTextView.setText(error.getMessage());
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
    public JSONObject addParams() {

        String advertiser_title = advertisment_title_input.getText().toString();
        String advertisment_description = advertisment_description_input.getText().toString();


        CreateJson cj = new CreateJson();
        cj.addStr("city", selected_city);
        cj.addStr("title" , advertiser_title);
        cj.addStr("description", advertisment_description);

        try {
            return cj.makeJSON();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

