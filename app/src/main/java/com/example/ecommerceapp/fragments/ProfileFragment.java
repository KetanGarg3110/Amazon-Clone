package com.example.ecommerceapp.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.activities.Intro_SignIn;
import com.example.ecommerceapp.activities.OrderHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    AppCompatButton orderBtn ,logOut;
    EditText userName , emailId ;
    ImageView doneBtn ,profile;
    TextView displayUserName;
    Toolbar toolbar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage storage;
    StorageReference storageReference;

    Dialog dialog;
    ProgressDialog progressDialog;
   // Uri helps in accessing files, whether they are internal or external
   //When you want to share data between different apps, you use Uri in conjunction with Intent.
    Uri setImageUri;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // this function showing picture in profile..
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    setImageUri = result.getData().getData();
                    profile.setImageURI(setImageUri);
                }
            });


    @SuppressLint({"ResourceAsColor", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View root=inflater.inflate(R.layout.fragment_profile, container, false);

       userName =root.findViewById(R.id.name);
        emailId =root.findViewById(R.id.email);
        doneBtn =root.findViewById(R.id.doneBtn);
        profile =root.findViewById(R.id.profileImage);
        displayUserName =root.findViewById(R.id.displayUserName);

        orderBtn=root.findViewById(R.id.your_order_btn);
        logOut=root.findViewById(R.id.log_out);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        storage= FirebaseStorage.getInstance();

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        profile.setOnClickListener(v -> {

            // this method load gallery .
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });


       loadUserProfileData();

        doneBtn.setOnClickListener(v -> {
            String name = userName.getText().toString().trim();
            String email =emailId.getText().toString().trim();
            displayUserName.setText(name);


            if(TextUtils.isEmpty(name)){
                userName.setError("Please enter the name of the product ");
                Toast.makeText(getContext(), "Please fill the the all details correctly before confirming", Toast.LENGTH_SHORT).show();

            } else if (TextUtils.isEmpty(email)) {
                emailId.setError("Please enter the price of the product ");
                Toast.makeText(getContext(), "Please fill the the all details correctly before confirming", Toast.LENGTH_SHORT).show();
            } else {

                uploadProfileData(name,email);

            }
        });

        orderBtn.setOnClickListener(v -> {
            Intent intent =new Intent(getContext(), OrderHistory.class);
            startActivity(intent);
        });

        logOut.setOnClickListener(v -> {
           assert getContext() != null;
            dialog =new Dialog(getContext());
           dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_profile);

            TextView noBtn =dialog.findViewById(R.id.noBtn);
            TextView yesBtn =dialog.findViewById(R.id.yesBtn);

            yesBtn.setOnClickListener(v1 -> {
                firebaseAuth.signOut();
                startActivity(new Intent(getContext(), Intro_SignIn.class));
                assert getActivity() != null;
                getActivity().finish();
            });
            noBtn.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
       return root;
    }

    private void uploadProfileData(String name, String email) {
        if (setImageUri != null) {
            storageReference = storage.getReference().child("userProfile/" + setImageUri.getLastPathSegment());
            storageReference.putFile(setImageUri)
                    .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                progressDialog.show();
                                String imageUrl = uri.toString();
                                // update Firestore with new Profile data.
                                firebaseFirestore.collection("UserProfile").document("9gvFgj5NhzhWGqNwrpT2")
                                        .update("name", name, "Email", email, "img_url", imageUrl)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> {
                                            Log.e("ProfileFragment", "Failed to update profile: ", e);
                                            Toast.makeText(getContext(), "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                                progressDialog.dismiss();
                            }))
                    .addOnFailureListener(e -> {
                        Log.e("ProfileFragment", "Image Upload Failed: ", e);
                        Toast.makeText(getContext(), "Image Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Update Firestore without image
            firebaseFirestore.collection("UserProfile").document("9gvFgj5NhzhWGqNwrpT2")
                    .update("name", name, "Email", email)
                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void loadUserProfileData() {

       // String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
       // Log.d("ProfileFragment1", "Document data: " + userId);
        firebaseFirestore.collection("UserProfile").document("9gvFgj5NhzhWGqNwrpT2")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            progressDialog.show();
                            String name = document.getString("name");
                            String email = document.getString("Email");
                            String imageUrl = document.getString("img_url");

                            if (name!=null){
                                userName.setText(name);
                                displayUserName.setText(name);
                            }

                            if (email !=null){
                                emailId.setText(email);
                            }

                            if (imageUrl != null) {
                                // Load image from URL using Glide or Picasso
                                Glide.with(this)
                                        .load(imageUrl)
                                        .into(profile);
                            }

                        }else {
                            Toast.makeText(getContext(), "No profile data found.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    } else {
                        Log.d("ProfileFragment", "Failed to load profile: ", task.getException());
                        Toast.makeText(getContext(), "Failed to load profile data"+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar=view.findViewById(R.id.profileToolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            HomeFragment homeFragment=new HomeFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.home_container,homeFragment).addToBackStack(null).commit();
        });
    }

}