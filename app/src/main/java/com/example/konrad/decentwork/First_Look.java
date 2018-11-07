package com.example.konrad.decentwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class First_Look extends AppCompatActivity {
    public Button toLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toLogin = (Button) findViewById(R.id.LogerOn);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(First_Look.this, Login.class);
                startActivity(intent);
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first__look);
    }
}
