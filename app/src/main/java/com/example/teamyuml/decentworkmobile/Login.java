package com.example.teamyuml.decentworkmobile;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/*
 * Class creating connection with server
 * Checking adres email
 * Parsing data to JSON and sending to the server
 */
public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText emailInput;
    EditText passwordInput;
    Button sigin;
    private static final String URL_FOR_LOGIN = VolleyInstance.getBaseUrl() + "/common/login/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.email_input);
        emailInput.setText(URL_FOR_LOGIN);
    }

    @Override
    public void onClick(View v) {
        
    }
}
