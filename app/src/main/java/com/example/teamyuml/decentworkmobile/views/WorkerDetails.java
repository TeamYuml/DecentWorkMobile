package com.example.teamyuml.decentworkmobile.views;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.teamyuml.decentworkmobile.R;

import com.example.teamyuml.decentworkmobile.fragments.ProfileDetailsFragment;

public class WorkerDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_details);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new ProfileDetailsFragment();
        fragment.setArguments(addBundle());
        fragmentTransaction.add(R.id.profile_details, fragment);
        fragmentTransaction.commit();
    }

    private Bundle addBundle() {
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putString("ID", intent.getStringExtra("choosenProfile"));
        System.out.println(bundle.getString("ID"));
        return bundle;
    }
}
