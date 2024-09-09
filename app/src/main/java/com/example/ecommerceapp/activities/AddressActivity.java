package com.example.ecommerceapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapters.AddressAdapter;
import com.example.ecommerceapp.models.AddressModel;
import com.example.ecommerceapp.models.NewProductModel;
import com.example.ecommerceapp.models.PopularProductModel;
import com.example.ecommerceapp.models.SearchFragmentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddressActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView addressRecycler;
    AddressAdapter addressAdapter;
    List<AddressModel> addressModelList;
    AddressModel addressModel;
    Button addAddress,continueBuy;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        toolbar=findViewById(R.id.addressToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        addressRecycler=findViewById(R.id.addressRecycler);
        addAddress=findViewById(R.id.addAddress);
        continueBuy=findViewById(R.id.continuePayment);

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        addressRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addressModelList=new ArrayList<>();
        addressAdapter=new AddressAdapter(getApplicationContext(),addressModelList);
        addressRecycler.setAdapter(addressAdapter);

        //getting data from firbase to show recycler view.

        firestore.collection("CurrentUser")
                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                        .collection("Address")
                .get()
                .addOnCompleteListener(task -> {
                    for (DocumentSnapshot snapshot:task.getResult().getDocuments()) {
                         addressModel = snapshot.toObject(AddressModel.class);
                        if(addressModel != null) {
                            addressModel.setDocumentId(snapshot.getId());
                            addressModelList.add(addressModel);
                            addressAdapter.notifyDataSetChanged();
                        }
                    }
                        });

        addAddress.setOnClickListener(v -> {
            Intent intent=new Intent(AddressActivity.this,AddAddress.class);
            startActivity(intent);
        }   );


        continueBuy.setOnClickListener(v ->{
            //getting data from detailed activity. getIntent() is used for fetching the data .matlab ,mainactivity se jo data aa raha hai usse recieve karne ke liye.
         Object obj =getIntent().getSerializableExtra("item ");

            int totalQuantity = getIntent().getIntExtra("totalQuantity",1);
            int totalPrice = getIntent().getIntExtra("totalPrice", 0); // Default value is 0
            //cart totalAmount
            int totalAmount = getIntent().getIntExtra("totalAmount",0);
            //  instanceof is a keyword used for checking if a reference variable contains a given type of object reference or not
            // Henceforth it is known as a comparison operator where the instance is getting compared to type returning boolean
            // true or false as in Java we do not have 0 and 1 boolean return types.

                    if (obj instanceof NewProductModel) {
                        NewProductModel newProductModel = (NewProductModel) obj;
                        totalPrice=newProductModel.getPrice()*totalQuantity;
                       // totalAmount =newProductModel.get
                    } else if (obj instanceof PopularProductModel) {
                        PopularProductModel popularProductModel = (PopularProductModel) obj;
                        totalPrice=popularProductModel.getPrice()*totalQuantity;
                    } else if (obj instanceof SearchFragmentModel) {
                        SearchFragmentModel searchFragmentModel = (SearchFragmentModel) obj;
                        totalPrice=searchFragmentModel.getPrice()*totalQuantity;
                    }

            AddressModel selectedAddress = addressAdapter.getSelectedAddress();
                    if (selectedAddress!=null){
                        Intent intent= new Intent(
                                AddressActivity.this,Payment.class);
                        intent.putExtra("address",selectedAddress.getAddress());
                        intent.putExtra("name",selectedAddress.getName());
                        intent.putExtra("phone",selectedAddress.getPhoneNo());
                        intent.putExtra("city",selectedAddress.getCity());
                        intent.putExtra("totalPrice",totalPrice);
                        intent.putExtra("totalAmount",totalAmount);
                      //  Toast.makeText(this, "$"+totalAmount, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }else {
                        Toast.makeText(AddressActivity.this, "Please select an address", Toast.LENGTH_SHORT).show();
                    }
        });
    }



}