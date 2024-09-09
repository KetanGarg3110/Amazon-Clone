package com.example.ecommerceapp.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapters.OnClickCategoryAdapter;
import com.example.ecommerceapp.models.OnClickCategoryModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OnClickCategory extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseFirestore firebaseFirestore;
    RecyclerView onClickCategoryRecycler;

    List<OnClickCategoryModel> onClickCategoryModelArrayList;
    OnClickCategoryAdapter onClickCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_on_click_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String type = getIntent().getStringExtra("type");

        onClickCategoryRecycler = findViewById(R.id.onClickCategoryRecycler);
        firebaseFirestore = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.onClickCategoryToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle("Category Product");

        onClickCategoryRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        onClickCategoryModelArrayList = new ArrayList<>();
        onClickCategoryAdapter = new OnClickCategoryAdapter(this, onClickCategoryModelArrayList);
    //    OnClickCategoryAdapter = new OnClickCategoryAdapter(this, searchFragmentModelsList);
        onClickCategoryRecycler.setAdapter(onClickCategoryAdapter);

        //Define the collection names and types filters
        List<String> collectionNames = List.of("SeeAll","NewProduct","PopularProducts");

        fetchDataFromCollections(collectionNames,type);

    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchDataFromCollections(List<String> collectionNames, String type) {
        for (String collectionName : collectionNames) {
            fetchDataFromCollection(collectionName, type);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchDataFromCollection(String collectionName , String type) {

        //Handling Queries: Created explicit Query objects and used them to execute queries.

        CollectionReference collectionRef = firebaseFirestore.collection(collectionName);
        Query query = collectionRef;

        if (type != null && !type.isEmpty()) {
            query = collectionRef.whereEqualTo("type", type);
            // type is catagory name .
            // and "type" is field name .
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot snapshot : querySnapshot.getDocuments()) {
                        OnClickCategoryModel onClickCategoryModel = snapshot.toObject(OnClickCategoryModel.class);
                        if (onClickCategoryModel != null) {
                            onClickCategoryModelArrayList.add(onClickCategoryModel);
                        }
                    }
                    onClickCategoryAdapter.notifyDataSetChanged();
                }
            } else {
                // Handle error
                if (task.getException() != null) {
                    task.getException().printStackTrace();
                }
            }
        });

     //   for(String collectionName : collection) {

//            if (type == null || type.isEmpty()) {
//
//                firebaseFirestore.collection(collectionName).get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
//                            OnClickCategoryModel seeAllModel = snapshot.toObject(OnClickCategoryModel.class);
//                            onClickCategoryModelArrayList.add(seeAllModel);
//                            OnClickCategoryAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//            }
//
//            if (type != null && type.equalsIgnoreCase("watch")) {
//                firebaseFirestore.collection(collectionName).whereEqualTo("type", "watch").get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
//                            OnClickCategoryModel seeAllModel = snapshot.toObject(OnClickCategoryModel.class);
//                            onClickCategoryModelArrayList.add(seeAllModel);
//                            OnClickCategoryAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//            }
//            if (type != null && type.equalsIgnoreCase("Mens")) {
//                firebaseFirestore.collection(collectionName).whereEqualTo("type", "Mens").get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
//                            OnClickCategoryModel seeAllModel = snapshot.toObject(OnClickCategoryModel.class);
//                            onClickCategoryModelArrayList.add(seeAllModel);
//                            OnClickCategoryAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//            }
//            if (type != null && type.equalsIgnoreCase("Shoes")) {
//                firebaseFirestore.collection(collectionName).whereEqualTo("type", "Shoes").get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
//                            OnClickCategoryModel seeAllModel = snapshot.toObject(OnClickCategoryModel.class);
//                            onClickCategoryModelArrayList.add(seeAllModel);
//                            OnClickCategoryAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//            }
//            if (type != null && type.equalsIgnoreCase("kids")) {
//                firebaseFirestore.collection(collectionName).whereEqualTo("type", "kids").get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
//                            OnClickCategoryModel seeAllModel = snapshot.toObject(OnClickCategoryModel.class);
//                            onClickCategoryModelArrayList.add(seeAllModel);
//                            OnClickCategoryAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//            }
//            if (type != null && type.equalsIgnoreCase("Camera")) {
//                firebaseFirestore.collection(collectionName).whereEqualTo("type", "Camera").get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
//                            OnClickCategoryModel seeAllModel = snapshot.toObject(OnClickCategoryModel.class);
//                            onClickCategoryModelArrayList.add(seeAllModel);
//                            OnClickCategoryAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//            }
//            if (type != null && type.equalsIgnoreCase("women")) {
//                firebaseFirestore.collection(collectionName).whereEqualTo("type", "women").get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
//                            OnClickCategoryModel seeAllModel = snapshot.toObject(OnClickCategoryModel.class);
//                            onClickCategoryModelArrayList.add(seeAllModel);
//                            OnClickCategoryAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//            }
//            if (type != null && type.equalsIgnoreCase("mobile")) {
//                firebaseFirestore.collection(collectionName).whereEqualTo("type", "mobile").get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
//                            OnClickCategoryModel seeAllModel = snapshot.toObject(OnClickCategoryModel.class);
//                            onClickCategoryModelArrayList.add(seeAllModel);
//                            OnClickCategoryAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//            }
        }
    }