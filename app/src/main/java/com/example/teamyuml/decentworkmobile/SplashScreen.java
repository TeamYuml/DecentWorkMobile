package com.example.teamyuml.decentworkmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        startActivity(new Intent(SplashScreen.this, Login.class));
        finish();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

    }
}
