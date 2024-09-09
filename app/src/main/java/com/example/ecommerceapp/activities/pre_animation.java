package com.example.ecommerceapp.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.example.ecommerceapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class pre_animation extends AppCompatActivity {
 FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth= FirebaseAuth.getInstance();

        askForFullScreen();
        setContentView(R.layout.activity_pre_animation);

        new Handler().postDelayed(() -> {  // handler can help us we take parallel threading as asencrus task way
            if (auth.getCurrentUser()==null){
                startActivity(new Intent(pre_animation.this, OnBoardActivity.class));
                finish();// stack main se activity ko bahar kar do /ya fer delete kar do
            }else{
                startActivity(new Intent(pre_animation.this, RegistrationActivity.class));
                finish();
            }
        },2000);
    }

    private void askForFullScreen() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }catch (NullPointerException e){
            Log.e(TAG, "askForFullScreen: ");
        }
    }

}