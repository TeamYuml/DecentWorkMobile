package com.example.teamyuml.decentworkmobile.views;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.fragments.ProfileDetailsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

public class WorkerDetails extends AppCompatActivity {
    String IdDetails;
    TextView name;
    TextView last_name;
    TextView city;
    TextView profession;
    TextView phone;
    TextView description;
    List<String> workerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_details);

        IdDetails = getIntent().getStringExtra("choosenProfile");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ProfileDetailsFragment fragment = new ProfileDetailsFragment();
        fragmentTransaction.add(R.id.profile_details, fragment);
        fragmentTransaction.commit();

        Bundle bundle = new Bundle();
        String test = VolleyInstance.getBaseUrl() + "/profiles/userProfiles/" + IdDetails + "/";
        bundle.putString("URL", test);
    }
}
