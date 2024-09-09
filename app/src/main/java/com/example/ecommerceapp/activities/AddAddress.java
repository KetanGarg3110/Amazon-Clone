package com.example.ecommerceapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ecommerceapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddAddress extends AppCompatActivity {

    TextInputEditText name,address,city,postalCode,phoneNumber;
    Button addAddressBtn;
    Toolbar toolbar;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        toolbar=findViewById(R.id.addressToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        name=findViewById(R.id.name);
        address=findViewById(R.id.addressLane);
        city=findViewById(R.id.city);
        postalCode=findViewById(R.id.postalCode);
        phoneNumber=findViewById(R.id.phoneNumber);
        addAddressBtn=findViewById(R.id.addAddressBtn);

        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        addAddressBtn.setOnClickListener(v -> {
            String userName = Objects.requireNonNull(name.getText()).toString();
            String userAddress = Objects.requireNonNull(address.getText()).toString();
            String userCity = Objects.requireNonNull(city.getText()).toString();
            String userPostalCode = Objects.requireNonNull(postalCode.getText()).toString();
            String userPhoneNo = Objects.requireNonNull(phoneNumber.getText()).toString();

//            String final_address="";
//            if(!userName.isEmpty()){
//                final_address+=userName;
//            }
//            if(!userAddress.isEmpty()){
//                final_address+=userAddress;
//            }
//            if(!userCity.isEmpty()){
//                final_address+=userCity;
//            }
//            if(!userPhoneNo.isEmpty()){
//                final_address+=userPhoneNo;
//            }
//            if(!userPostalCode.isEmpty()){
//                final_address+=userPostalCode;
//            }
            if(!userName.isEmpty() && !userAddress.isEmpty() && !userCity.isEmpty() && !userPhoneNo.isEmpty() && !userPostalCode.isEmpty()){
                Map<String,String> map =new HashMap<>();
                map.put("name",userName);
                map.put("address",userAddress);
                map.put("city",userCity);
                map.put("phoneNo",userPhoneNo);
                map.put("postalCode",userPostalCode);

                firebaseFirestore.collection("CurrentUser")
                        .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                        .collection("Address")
                        .add(map)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Toast.makeText(AddAddress.this, "Address Added", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddAddress.this, AddressActivity.class));
                                finish();
                            }
                        });
            }else {
                Toast.makeText(this, "Kindly fill All Field", Toast.LENGTH_SHORT).show();
            }

        });

    }
}