package com.example.ecommerceapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerceapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class RegistrationActivity extends AppCompatActivity  {
   private EditText editTextName,editTextEmail,editTextPassword,editTextComfirmPassword;
  private  FirebaseAuth auth;
           FirebaseDatabase firebaseDatabase;
           FirebaseStorage firebaseStorage;
           ProgressDialog progressDialog;
  SharedPreferences preferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regestration);

        auth = FirebaseAuth.getInstance(); // FirebaseAuth() is the entry point of the firebase Authentication Sdk. first ,obtain an instance of this class by calling getInstance().then sign in or sign up or register a under with one of the methods.
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        editTextName = findViewById(R.id.Name);
        editTextEmail = findViewById(R.id.EMail);
        editTextPassword = findViewById(R.id.Password);
        editTextComfirmPassword = findViewById(R.id.Confirm_password);
        //progress dialog box
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false); //this means user can not cancel the dialog box.

//        if (Objects.requireNonNull(getSupportActionBar()).isShowing()){
//         getSupportActionBar().hide();// action bar ko hide karne ke liye.
//        }


        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            finish();
        }

        preferences = getSharedPreferences("register", MODE_PRIVATE);

        boolean isFirstTime = preferences.getBoolean("firstTime", true);

        if (isFirstTime) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstTime", false);
            editor.apply();

            startActivity(new Intent(RegistrationActivity.this, OnBoardActivity.class));
            finish();
        }
  }
    public void signUp(View view){
        progressDialog.show();
        String username=editTextName.getText().toString();
        String userEmail=editTextEmail.getText().toString();
        String userPassword =editTextPassword.getText().toString();
        String userConfirmPassword = editTextComfirmPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
                Toast.makeText(RegistrationActivity.this, "Enter your name", Toast.LENGTH_SHORT).show();
                editTextName.setError("Full Name is required");
                editTextName.requestFocus();
                progressDialog.dismiss();
            return;
            }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            Toast.makeText(RegistrationActivity.this, "Enter your E-mail", Toast.LENGTH_SHORT).show();
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (!isValidPassword(userPassword)){
            Toast.makeText(RegistrationActivity.this, "Password is not strong enough. Please use a password with at least 8 characters, including uppercase and lowercase letters, numbers, and special characters.", Toast.LENGTH_SHORT).show();
            editTextPassword.setError(" * password with at least 8 characters \n * Including uppercase and lowercase letters\n * Include special character \n * Include atleast one number");
            editTextPassword.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (!userConfirmPassword.equals(userPassword)){
            Toast.makeText(RegistrationActivity.this, "Enter Same Password", Toast.LENGTH_SHORT).show();
            editTextComfirmPassword.requestFocus();
            editTextComfirmPassword.setError("Enter Same Password");
            progressDialog.dismiss();
            return;
        }

 // now to feed data in firebase database use auth.
        auth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(RegistrationActivity.this, task -> {

                if (task.isSuccessful()){
// this is basically calling the current user ids
//                    DatabaseReference reference= firebaseDatabase.getReference().child("users").child(auth.getCurrentUser().getUid());
// this is basically creting a storage with upload name to unique user id .that will upadted a profile picture
//                    StorageReference storageReference=firebaseStorage.getReference().child("upload").child(auth.getCurrentUser().getUid());

                    Toast.makeText(RegistrationActivity.this, "User register successfully", Toast.LENGTH_SHORT).show();
                     progressDialog.dismiss();
//   To prevent user from returning back to register activity on pressing back button after registration.
                    Intent intent =new Intent(RegistrationActivity.this,MainActivity.class);
                  intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );//using FLAG_NEW_TASK and FLAG_ACTIVITY_TASK will clear all the activities from the back stack.so multiple activity don't crete.so user cant frustrate to click back-button.
                       startActivity(intent);              //Intent.FLAG_ACTIVITY_NEW_TASK: This flag will create a new task for the activity, which means that the activity will be launched in a new task, and the user will be able to navigate back to the previous task by pressing the back button.
                                                           //Intent.FLAG_ACTIVITY_CLEAR_TASK: This flag will clear the task stack, which means that all activities in the current task will be finished, and the new activity will be the only one in the task stack
                       finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
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

    public  void signIn(View view){
        startActivities(new Intent[]{new Intent(RegistrationActivity.this, LoginActivity.class)});
        finish();
    }


//    private void updateUI(FirebaseUser user) {
//        // Update the UI with the signed-in user's information
//        if (user != null) {
//            // User is signed in, show the user's email
//            editTextEmail.setText(user.getEmail());
//        } else {
//            // User is signed out, show a message
//            editTextEmail.setText("Not signed in");
//        }
//    }
}