package com.example.kisan_udyog.sell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kisan_udyog.R;


public class Review extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private Context mcontext = Review.this;
    private Button ssubmit, stotal;
    private EditText mtypePrice, mquantity;
    private String typePrice;
    private TextView mtotalCost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        getSupportActionBar().hide();

        EditText mtypeName = (EditText) findViewById(R.id.sname);
        Intent intent = getIntent();
        String typeName = intent.getStringExtra("typeName");
        mtypeName.setText(typeName);

        mtotalCost = findViewById(R.id.totalCost);
        mquantity= findViewById(R.id.quantity);
        mtypePrice = (EditText) findViewById(R.id.sphone);
        typePrice = intent.getStringExtra("typePrice");
        mtypePrice.setText(typePrice);

        stotal=(Button) findViewById(R.id.getTotal);
        stotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int b= Integer.parseInt(mquantity.getText().toString());
                int a= Integer.parseInt(typePrice);
                int total =a*b;
                mtotalCost.setText(String.valueOf(total));
            }
        });

        ssubmit=(Button) findViewById(R.id.ssubmit);
        ssubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Review.this,AddPostActivity.class);
                int b= Integer.parseInt(mquantity.getText().toString());
                int a= Integer.parseInt(typePrice);
                int total =a*b;
                String s=Integer.toString(total);
                intent.putExtra("total", s);
                intent.putExtra("quantity", mquantity.getText().toString());
                startActivity(intent);
            }
        });
    }
}

