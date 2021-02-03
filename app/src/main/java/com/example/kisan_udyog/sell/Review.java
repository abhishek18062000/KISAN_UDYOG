package com.example.kisan_udyog.sell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.kisan_udyog.R;


public class Review extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private Context mcontext = Review.this;
    private Button ssubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        getSupportActionBar().hide();


        ssubmit=(Button) findViewById(R.id.ssubmit);
        ssubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Review.this,UploadSell.class);
                startActivity(intent);
            }
        });

    }
}

