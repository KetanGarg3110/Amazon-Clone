package com.example.ecommerceapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerceapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail,editTextPassword;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail= findViewById(R.id.EMail);
        editTextPassword= findViewById(R.id.Password);

//        if (Objects.requireNonNull(getSupportActionBar()).isShowing()){
//            getSupportActionBar().hide();// action bar ko hide karne ke liye.
//        }

     //   progress dialog box
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false); //this means user can not cancel the dialog box.

    }
    public void logSignIn(View view){

        progressDialog.show();
        String userEmail=editTextEmail.getText().toString();
        String userPassword =editTextPassword.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            Toast.makeText(LoginActivity.this, "Enter your E-mail", Toast.LENGTH_SHORT).show();
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (!isValidPassword(userPassword)) {
            Toast.makeText(LoginActivity.this, "Password is not strong enough. Please use a password with at least 8 characters, including uppercase and lowercase letters, numbers, and special characters.", Toast.LENGTH_SHORT).show();
            editTextPassword.setError(" * password with at least 8 characters \n * Including uppercase and lowercase letters\n * Include special character \n * Include atleast one number");
            editTextPassword.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(LoginActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
            editTextPassword.setError("password is required");
            editTextPassword.requestFocus();
            progressDialog.dismiss();
            return;
        }
        firebaseAuth= FirebaseAuth.getInstance(); // FirebaseAuth() is the entry point of the firebase Authentication Sdk. first ,obtain an instance of this class by calling getInstance().then sign in or sign up or register a uder with one of the methods.
        firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(LoginActivity.this, task -> {
            if (task.isSuccessful()){
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                startActivities(new Intent[]{new Intent(LoginActivity.this, MainActivity.class)});
            }else {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Error in Login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidPassword(String userPassword) {
        // check if password is at least 8 characters long
        if (userPassword.length() < 8) {
            return false;
        }
        // check if password contains at least one upper case latter
        if (!userPassword.matches(".*[A-Z].*")) {
            return false;
        }
        // check if password contain at least one lower case latter
        if (!userPassword.matches(".*[a-z].*")){
            return  false;
        }
        // check if password conation at least one special character.
        if (!userPassword.matches(".*[!@#$%^&*()_+=-].*")){
            return  false;
        }
        //check if password conation at least one number.
        return userPassword.matches(".*[0-9].*");
    }
    public  void logSignUp(View view){
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }
}