package com.example.teamyuml.decentworkmobile.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.fragments.AssignButtons;
import com.example.teamyuml.decentworkmobile.utils.UserAuth;

import org.json.JSONException;
import org.json.JSONObject;


public class NoticeDetails extends AppCompatActivity {

    private String IdDetails;
    private TextView title;
    private TextView profession;
    private TextView owner;
    private TextView city;
    private TextView description;
    private TextView created;
    private FragmentManager fragmentManager;

    private int assignContent = R.id.assign_buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);
        fragmentManager = this.getSupportFragmentManager();
        IdDetails = getIntent().getStringExtra("choosenProfile");
        initializeTextViews();
        getNoticeDetails();
    }

    /**
     * Initialize text views.
     */
    private void initializeTextViews() {
        title = findViewById(R.id.title);
        profession = findViewById(R.id.profession);
        owner = findViewById(R.id.owner);
        city = findViewById(R.id.city);
        description = findViewById(R.id.description);
        created = findViewById(R.id.created);
    }

    private void getNoticeDetails() {
        final String NOTICE_DETAIL_URL = VolleyInstance.getBaseUrl() + "/engagments/engagments/" + IdDetails + "/";

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (
                Request.Method.GET, NOTICE_DETAIL_URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String title_s = response.getString("title");
                            String profession_s = response.getString("profession");
                            String owner_s = response.getString("owner");
                            String city_s = response.getString("city");
                            String description_s = response.getString("description");
                            String created_s = response.getString("created");
                            title.setText(title_s);
                            profession.setText(profession_s);
                            owner.setText(owner_s);
                            city.setText(city_s);
                            description.setText(description_s);
                            created.setText(created_s);

                            // Show assign buttons when notice do not belogns to currently logged user
                            if (!owner_s.equals(UserAuth.getEmail(NoticeDetails.this))) {
                                addFragment();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NoticeDetails.this,
                        "Coś poszło nie tak",
                        Toast.LENGTH_LONG).show();
                }
            });

        VolleyInstance.getInstance(this).addToRequestQueue(jsonObjectRequest, "noticeDetails");
    }

    /**
     * Adds assign buttons to activity.
     */
    private void addFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment assignButtons = new AssignButtons();
        assignButtons.setArguments(addBundle());
        fragmentTransaction.replace(assignContent, assignButtons);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Adds to fragment bundle with notice id.
     * @return Returns bundle with notice id.
     */
    private Bundle addBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("id", Integer.parseInt(IdDetails));
        return bundle;
    }
}
