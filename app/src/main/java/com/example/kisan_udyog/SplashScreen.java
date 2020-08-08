package com.example.kisan_udyog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.graphics.drawable.PathInterpolatorCompat;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN = PathInterpolatorCompat.MAX_NUM_POINTS;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView((R.layout.splash_screen));
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this, MainActivity.class));
                SplashScreen.this.finish();
            }
            } , (long) SPLASH_SCREEN);
    }
}
