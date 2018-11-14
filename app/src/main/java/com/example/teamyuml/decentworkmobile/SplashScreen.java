package com.example.teamyuml.decentworkmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/*
* Start class for Mobile Application
* created as SplashScreen style
* */
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //created connection with Login Activity
        startActivity(new Intent(SplashScreen.this, Login.class));
        finish();
    }
}
