package com.example.kisan_udyog.sell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kisan_udyog.MainActivity;
import com.example.kisan_udyog.R;
import com.example.kisan_udyog.gridview.GridViewAdapter;
import com.example.kisan_udyog.login.LoginActivity;
import com.example.kisan_udyog.models.DataModel;
import com.example.kisan_udyog.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SellActivity extends AppCompatActivity {
    private static final String TAG = "SellActivity";

    private Button sNext;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private Context mcontext = SellActivity.this;

    //String[] mobileArray = Objects.requireNonNull(intent.getStringArrayListExtra("type")).toArray(new String[0]);
   // ArrayList<String> mobileArray = (ArrayList<String>) intent.getParcelableArrayListExtra("type");
   /* String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X","DDFsdffsd","asdadssgsa","sadgsdg","sgdadfgfdag","sdgdggaafdg","dasfsgaadgsg","dasgdsag","adgdgsga","asdgdsgdg"};
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_price);
        getSupportActionBar().hide();


        final ArrayList<String> mobileArray = (ArrayList<String>) getIntent().getSerializableExtra("type");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_sell_price_adapter, mobileArray);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);


       /* EditText receiver_msg = (EditText) findViewById(R.id.sprice);
        Intent intent = getIntent();
        String str = intent.getStringExtra("ghj");
        receiver_msg.setText(str);

*/




       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Log.d("SellActivity", "INEGER"+mobileArray.get(i));
               Intent intent = new Intent(SellActivity.this,Review.class);
               startActivity(intent);
           }
       });
    }}