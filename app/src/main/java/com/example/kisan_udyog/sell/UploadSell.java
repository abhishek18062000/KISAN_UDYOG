package com.example.kisan_udyog.sell;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kisan_udyog.R;

public class UploadSell extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private Context mcontext = UploadSell.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_upload);
        getSupportActionBar().hide();
    }
    }