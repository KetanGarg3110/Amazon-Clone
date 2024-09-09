package com.example.ecommerceapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapters.OrderAdapter;
import com.example.ecommerceapp.fragments.ProfileFragment;
import com.example.ecommerceapp.models.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderHistory extends AppCompatActivity {
    Toolbar toolbar;

    RecyclerView orderRecycler;
    List<OrderModel> orderModelList ;
    OrderAdapter orderAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar =findViewById(R.id.orderToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        orderRecycler=findViewById(R.id.orderRecycler);

        orderRecycler.setLayoutManager(new LinearLayoutManager(OrderHistory.this, LinearLayoutManager.VERTICAL, false));
        orderModelList = new ArrayList<>();
        orderAdapter = new OrderAdapter(OrderHistory.this,orderModelList);
        orderRecycler.setAdapter(orderAdapter);

        firebaseFirestore.collection("Order")
                .document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .collection("History")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            OrderModel orderModel = document.toObject(OrderModel.class);
                            // adding data in recycler view with help of add .add main structure chiye hoya to upper side main pPm ko crete karna hoga
                            orderModelList.add(orderModel);
                            orderAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(OrderHistory.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}