package com.example.kisan_udyog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.kisan_udyog.buyer.HomeBuyer;
import com.example.kisan_udyog.login.LoginActivity;
import com.example.kisan_udyog.models.User;
import com.example.kisan_udyog.models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private Context mcontext = SplashScreen.this;
    private FirebaseAuth mAuth;
    DatabaseReference reference, rootRef;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private UserSettings mUserSettings;
    private String userType;
    private String userID;
    private Button mrel1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        getSupportActionBar().hide();
        //setupFirebaseAuth();
        userType=setups();
       //Log.d(TAG,"WTFSS"+userType);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

             try {
                 if (userType.equals("FARMER")) {
                     Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     finish();
                     startActivity(intent);
                 } else if (userType.equals("BUYER")) {
                     Intent intent = new Intent(SplashScreen.this, HomeBuyer.class);
                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     finish();
                     startActivity(intent);
                 }
             }
             catch(Exception error1){
                 Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 finish();
                 startActivity(intent);
             }

            }
        }, 5000);
    }




 /*   private void checkCurrentUser(FirebaseUser user){
        if (user == null) {

                    Intent intent = new Intent(mcontext, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SplashScreen.this.finish();
                    startActivity(intent);
        }

    }


    private void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListner= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                checkCurrentUser(user);
                if(user!=null){
                    Log.d(TAG,"USER logged in"+ user.getUid());
                    //userType=setups();
                }
                else{
                    Log.d(TAG,"USER not logged in");
                }
            }
        };
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
        checkCurrentUser(mAuth.getCurrentUser());
    }
    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListner);
    }
*/

    private String setups(){
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
            Log.d(TAG,"WTF"+userID);
            rootRef = FirebaseDatabase.getInstance().getReference();
            reference = rootRef.child("users").child(userID);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userType = dataSnapshot.getValue(User.class).getRole();
                    Toast.makeText(SplashScreen.this, "LOGGED AS"+ userType, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        return userType;
    }
}