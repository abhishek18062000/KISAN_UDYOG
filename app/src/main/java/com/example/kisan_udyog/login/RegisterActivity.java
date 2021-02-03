package com.example.kisan_udyog.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.kisan_udyog.MainActivity;
import com.example.kisan_udyog.R;
import com.example.kisan_udyog.models.User;
import com.example.kisan_udyog.utils.FirebaseMethods;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private Context mContext;
    private double latitude;
    private double longitude;

    private FirebaseMethods firebaseMethods;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;
    private FirebaseDatabase mFirebaseDatabase;

    private String email, username, password,repassword,city;
    private String phoneNumber;
    private String selectedRbText;
    private EditText mEmail, mPassword, mName, mRePassword, mCity, mPhoneNumber;
    private Button mRegister;
    private String append="";

    RadioGroup radioGroup;
    RadioButton selectedRadioButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mContext=RegisterActivity.this;
        firebaseMethods=new FirebaseMethods(mContext);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }

        requestLocation();
        initWidgets();
        setupFirebaseAuth();
        init();
    }
//-----------------------------------------WIDGETS INITATION----------------------------------------------------

    private void init(){
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEmail.getText().toString();
                username = mName.getText().toString();
                password = mPassword.getText().toString();
                repassword = mRePassword.getText().toString();
                city = mCity.getText().toString();
                phoneNumber = mPhoneNumber.getText().toString();

                if(checkInputs(email, username,password,repassword,city,phoneNumber)){
                    firebaseMethods.registerNewEmail(email,password);
                }
            }
        });
    }

    private Boolean checkInputs(String email, String username, String password, String repassword, String city,String phoneNumber){
        if(email.equals("") || username.equals("") || password.equals("") || repassword.equals("") || city.equals("") || phoneNumber.equals("")) {
            Toast.makeText(mContext, "Fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initWidgets(){
        mEmail=(EditText) findViewById(R.id.regemail);
        mPassword=(EditText) findViewById(R.id.regpassword);
        mRePassword=(EditText) findViewById(R.id.reEnterPassword);
        mName=(EditText) findViewById(R.id.fullName);
        mCity=(EditText) findViewById(R.id.city);
        mPhoneNumber=(EditText) findViewById(R.id.phoneNumber);
        mRegister=(Button) findViewById(R.id.register);
        radioGroup = findViewById(R.id.radioGroup);
    }




//-----------------------------------------FIREBASE STUFF----------------------------------------------------
private void checkIfUsernameExists(final String username) {
    Log.d(TAG, "checkIfUsernameExists: Checking if  " + username + " already exists.");

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    Query query = reference
            .child(getString(R.string.dbname_user))
            .orderByChild(getString(R.string.field_username))
            .equalTo(username);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                if (singleSnapshot.exists()) {
                    Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + singleSnapshot.getValue(User.class).getUsername());
                    append = myRef.push().getKey().substring(3, 10);
                    Log.d(TAG, "onDataChange: username already exists. Appending random string to name: " + append);
                }
            }

            String mUsername = "";
            mUsername = username + append;



            //input from radio button
            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            if (selectedRadioButtonId != -1) {
                selectedRadioButton = findViewById(selectedRadioButtonId);
                selectedRbText = selectedRadioButton.getText().toString();
                Toast.makeText(mContext, "Signup successful. Sending verification email.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Nothing Selected", Toast.LENGTH_SHORT).show();
            }

            //add new user to the database
            firebaseMethods.addNewUser(email, username,city, latitude, longitude, Long.parseLong(phoneNumber), "",selectedRbText);

            Toast.makeText(mContext, "Signup successful. Sending verification email.", Toast.LENGTH_SHORT).show();

            mAuth.signOut();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}




    private void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d(TAG,"USER logged in"+ user.getUid());
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            checkIfUsernameExists(username);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    finish();
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



//-------------------------------------------------GET LOCATION--------------------------------------------------------------------------
    private void requestLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getCurrentLocation();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            getCurrentLocation();
        } else {
            Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
    private void getCurrentLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(RegisterActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(RegisterActivity.this).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    Log.d(TAG, "LATITUDE:----------------------------------------------------" + latitude);
                    Log.d(TAG, "LONGITUDE:--------------------------------------------------" + longitude);
                }
            }
        }, Looper.getMainLooper());
    }
}
