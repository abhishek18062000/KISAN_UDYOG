package com.example.kisan_udyog;


import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kisan_udyog.login.LoginActivity;
import com.example.kisan_udyog.sell.SellerProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class HomeFragment extends Fragment{
    private static final String TAG = "HomeFragment";
    private Button signouts;
    private ImageView image;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private TextView result;
    Interpreter interpreter;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupFirebaseAuth();

        signouts=view.findViewById(R.id.signouts);
        signouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                getActivity().finish();
            }
        });

        image=view.findViewById(R.id.profilePic);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), SellerProfile.class);
                startActivity(intent);
            }
        });
        return view;
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

    private void checkCurrentUser(FirebaseUser user){
        if (user == null){
            Intent intent= new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }


}
