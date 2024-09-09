package com.example.ecommerceapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ecommerceapp.R;

public class Intro_SignIn extends AppCompatActivity {

    TextView introSignIn,introSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_sign_in);

        introSignIn=findViewById(R.id.SignInTxt);
        introSignUp=findViewById(R.id.SignUpTxt);

        introSignIn.setOnClickListener(view -> {
            startActivity(new Intent(Intro_SignIn.this, LoginActivity.class));
            finish();
        });
        introSignUp.setOnClickListener(view -> {
            startActivity(new Intent(Intro_SignIn.this, RegistrationActivity.class));
            finish();
        });
    }
}