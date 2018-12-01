package com.example.teamyuml.decentworkmobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.BuildConfig;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.utils.CreateJson;
import com.example.teamyuml.decentworkmobile.utils.UserAuth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Fragment for Google sign in.
 */
public class SignInGoogle extends Fragment implements View.OnClickListener {

    String GoogleTokenURL = VolleyInstance.getBaseUrl() + "/common/tokensignin/";
    View v;
    GoogleSignInClient mGoogleSignInClient;

    private static final int RC_SIGN_IN = 9001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    /**
     * Add to button on click Listener and return {@Link View}
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.google_sign_in, container, false);

        v.findViewById(R.id.btn_googleAuth).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GoogleClientID)
            .requestEmail()
            .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_googleAuth:
                signInGoogle();
                break;
        }
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            v.findViewById(R.id.btn_googleAuth).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.btn_googleAuth).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    /**
     * Make intent to next activity after successful sign in or
     * make sign in button visible if sign in failure.
     * @param completedTask - Completed google sign api call
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();

            CreateJson cj = new CreateJson();
            cj.addStr("idToken", idToken);
            JSONObject data = cj.makeJSON();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, GoogleTokenURL, data, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            String email = response.getString("email");
                            String token = response.getString("token");

                            UserAuth.saveAuthData(getActivity(), email, token);

                            updateUI(account);
                            // TODO ADD INTENT
                            Toast.makeText(getContext(), "logged", Toast.LENGTH_LONG).show();
                            getActivity().finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.networkResponse);
                    }
                });

            VolleyInstance.getInstance(getContext()).addToRequestQueue(jsonObjectRequest, "login");
        } catch (ApiException e) {
            System.out.println(e.getStatusCode());
            updateUI(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
