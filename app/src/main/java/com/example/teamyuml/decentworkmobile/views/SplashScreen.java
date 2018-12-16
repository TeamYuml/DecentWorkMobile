package com.example.teamyuml.decentworkmobile.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/**
 * Start class for Mobile Application
 * created as SplashScreen style
 */
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(SplashScreen.this, Login.class));
        finish();
    }
}
