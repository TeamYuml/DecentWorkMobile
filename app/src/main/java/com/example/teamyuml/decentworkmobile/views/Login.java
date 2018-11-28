package com.example.teamyuml.decentworkmobile.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.utils.CreateJson;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Class creating connection with server
 * Checking adres email
 * Parsing data to JSON and sending to the server
 */
public class Login extends AppCompatActivity implements View.OnClickListener {
    // TODO: CHECK IF WE NEED PROGRESS DIALOG OR NOT
    Button signIn;

    private EditText emailInput;
    private EditText passwordInput;
    SignInButton btnGoogleAuth;

    private static final int Req_Code = 9001;
    private static final String LOGIN_URL = VolleyInstance.getBaseUrl() + "/common/login/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        signIn = findViewById(R.id.sign_in);
        signIn.setOnClickListener(this);
        btnGoogleAuth = findViewById(R.id.btn_googleAuth);
        btnGoogleAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_googleAuth:
                        signInGoogle();
                        break;
                }
            }
        });
    }

    private void signInGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivity(signInIntent);
    }

    @Override
    protected void onStart() {

        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        Toast.makeText(getApplicationContext(), "logged", Toast.LENGTH_SHORT).show();
    }


    @Override
    /**
     * Makes request to server and sign in user if success or shows
     * error when failure.
     */
    public void onClick(View v) {
        JSONObject data = this.addParams();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.POST, LOGIN_URL, data, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    String email = null;

                    try {
                        email = response.getString("email");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (email != null) {
                        // TODO: ADD INTENT TO NEXT ACTIVITY
                        Toast.makeText(Login.this, email,
                            Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: ADD LABEL INSTEAD OF TOAST PROBABLY
                    if (error.networkResponse.statusCode == 401) {
                        Toast.makeText(Login.this,
                            "Zły email lub hasło.", Toast.LENGTH_LONG).show();
                    } else if (error.networkResponse.statusCode == 400){
                        Toast.makeText(Login.this, "Dane są nie poprawne.",
                            Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Login.this, "Coś poszło nie tak.",
                            Toast.LENGTH_LONG).show();
                    }
                }
            });

        VolleyInstance.getInstance(this).addToRequestQueue(jsonObjectRequest, "login");
    }

    /**
     * Reads all params from input and add them to Hash Maps
     * @return Valid Json object as request parameters.
     */
    private JSONObject addParams() {
        // TODO: ADD CHECKS FOR EMPTY STRINGS
        String emailText = emailInput.getText().toString();
        String passwordText = passwordInput.getText().toString();

        CreateJson cj = new CreateJson();
        cj.addStr("email", emailText);
        cj.addStr("password", passwordText);

        try {
            return cj.makeJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
