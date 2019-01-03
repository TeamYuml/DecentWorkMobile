package com.example.teamyuml.decentworkmobile.views;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.fragments.AssignButtons;
import com.example.teamyuml.decentworkmobile.fragments.DetailsFragment;
import com.example.teamyuml.decentworkmobile.model.UserList;
import com.example.teamyuml.decentworkmobile.utils.UserAuth;
import com.example.teamyuml.decentworkmobile.volley.ErrorHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.teamyuml.decentworkmobile.R.layout.assigned_row_style;


public class NoticeDetails extends AppCompatActivity {
    private String IdDetails;
    private FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);
        IdDetails = getIntent().getStringExtra("choosenProfile");
        fragmentManager = getSupportFragmentManager();
        noticeDetailsFragment();
    }

    private void noticeDetailsFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(addBundle());
        fragmentTransaction.replace(R.id.fragment_content, detailsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private Bundle addBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("id", Integer.parseInt(IdDetails));
        return bundle;
    }

}
