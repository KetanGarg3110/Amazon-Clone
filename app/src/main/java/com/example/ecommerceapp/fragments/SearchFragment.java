package com.example.ecommerceapp.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapters.SearchFragmentAdapter;
import com.example.ecommerceapp.models.SearchFragmentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {

    LinearLayout bottomNavbar;
    Toolbar toolbar;
    SearchView searchTxt;
    AppCompatButton searchBtn;

    RecyclerView searchRecycler;
    List<SearchFragmentModel> arrayListSearch;
    SearchFragmentAdapter searchFragmentAdapter;

    EditText editTextSearch;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    ProgressDialog progressDialog ;
    public SearchFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_search, container, false);

        searchBtn=root.findViewById(R.id.search_btn);
        searchRecycler=root.findViewById(R.id.search_recy);
        editTextSearch = root.findViewById(R.id.editTextSearch);

        firebaseFirestore=  FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();

        progressDialog= new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data ..");
        progressDialog.show();

        searchRecycler.setHasFixedSize(true);
        searchRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayListSearch= new ArrayList<>();
        searchFragmentAdapter= new SearchFragmentAdapter(getContext(),arrayListSearch);
        searchRecycler.setAdapter(searchFragmentAdapter);

//        searchBtn.setOnClickListener(v -> {

            editTextSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                   // searchBtn.setOnClickListener(v -> {
                        // this shows filtered data when user write and click btn.
                        searchFragmentAdapter.filter(s.toString());
                 //   });
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
//      searchTxt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//          @Override
//          public boolean onQueryTextSubmit(String query) {
//              searchFragmentAdapter.filter(query);
//              return true;
//          }
//
//          @Override
//          public boolean onQueryTextChange(String newText) {
//              searchFragmentAdapter.filter(newText);
//              return true;
//          }
//      });
  //      });
        onStart();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar =view.findViewById(R.id.search_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
         toolbar.setNavigationOnClickListener(v -> {
             HomeFragment homeFragment=new HomeFragment();
              getParentFragmentManager().beginTransaction().replace(R.id.home_container,homeFragment).addToBackStack(null).commit();
         });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onStart() {
        super.onStart();
        // fetching data from multiple collection .
        List<String> collections = Arrays.asList("NewProduct","PopularProducts","SeeAll");

        for (String collectionName : collections){
            //  data aseandiing se every product name assemble in ascending order .
            firebaseFirestore.collection(collectionName).orderBy("name", Query.Direction.ASCENDING).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SearchFragmentModel searchFragmentModel = document.toObject(SearchFragmentModel.class);
                                progressDialog.dismiss();
                                arrayListSearch.add(searchFragmentModel);
                                // Notify adapter that data has changed
                                searchFragmentAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                        }
                    });
            }
    }
}