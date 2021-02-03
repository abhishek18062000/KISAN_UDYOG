package com.example.kisan_udyog.sell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kisan_udyog.MainActivity;
import com.example.kisan_udyog.R;
import com.example.kisan_udyog.gridview.GridViewAdapter;
import com.example.kisan_udyog.login.LoginActivity;
import com.example.kisan_udyog.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellActivity extends AppCompatActivity {
    private static final String TAG = "SellActivity";

    private Button sNext;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private Context mcontext = SellActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_price);
        getSupportActionBar().hide();

        EditText receiver_msg = (EditText) findViewById(R.id.sprice);
        Intent intent = getIntent();
        String str = intent.getStringExtra("ghj");
        receiver_msg.setText(str);




        sNext=(Button) findViewById(R.id.snext);
        sNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellActivity.this,Review.class);
                startActivity(intent);
            }
        });
    }


}