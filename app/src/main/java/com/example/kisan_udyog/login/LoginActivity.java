package com.example.kisan_udyog.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kisan_udyog.MainActivity;
import com.example.kisan_udyog.R;

import com.example.kisan_udyog.buyer.Buyer;
import com.example.kisan_udyog.models.User;
import com.example.kisan_udyog.sell.SellActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context mContext;
    private EditText mEmail, mPassword;
    private TextView mRegisterHere;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    DatabaseReference reference, rootRef;
    private String userType;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acttivity_login);
        getSupportActionBar().hide();

        mEmail=(EditText) findViewById(R.id.loginEmail);
        mPassword=(EditText) findViewById(R.id.loginPassword);
        mContext = LoginActivity.this;

        setupFirebaseAuth();
        init();

    }
    private boolean isStringNull(String string){
        if(string.equals("")){
            return true;
        }
        else {
            return false;
        }
    }

//----------------------------------------getting type of user--------------------------------------


    private void init(){
        Button btnLogin = (Button) findViewById(R.id.logins);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email= mEmail.getText().toString();
                String password= mPassword.getText().toString();

                if(isStringNull(email) && isStringNull(password)){
                    Toast.makeText(mContext, "you must full all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            userType=setups();
                            if(!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "unSUCCESSFUL",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                try {

                                    Log.d(TAG,"WTFSS"+userType);
                                    if(user.isEmailVerified() ){
                                        if(userType.equals("BUYER")){
                                            Intent intent = new Intent(LoginActivity.this, Buyer.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            finish();
                                            startActivity(intent);
                                        }
                                        else if (userType.equals("FARMER")){
                                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            finish();
                                            startActivity(intent);
                                        }
                                    }

                                    else{
                                        Toast.makeText(mContext, "Email is not verified ", Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                    }
                                }
                                catch (NullPointerException e){
                                    Log.e(TAG, "oncompete: NulPointer exception"+e.getMessage());
                                }
                            }
                        }
                    });
                }
            }
        });



        mRegisterHere = (TextView) findViewById(R.id.register_heres);
        mRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                //finish();
                startActivity(intent);
            }
        });
    }


    private String  setups(){
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();

            Log.d(TAG,"WTF"+userID);

            rootRef = FirebaseDatabase.getInstance().getReference();
            reference = rootRef.child("users").child(userID);
            Log.d(TAG,"WTF"+reference);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userType = dataSnapshot.getValue(User.class).getRole();
                    Toast.makeText(LoginActivity.this, "LOGGED AS"+ userType, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }
        return userType;
    }



    private void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d(TAG,"USER logged in"+ user.getUid());
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
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

}
