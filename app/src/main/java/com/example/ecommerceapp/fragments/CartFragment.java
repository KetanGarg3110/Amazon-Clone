package com.example.ecommerceapp.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.activities.AddressActivity;
import com.example.ecommerceapp.adapters.CartFragmentAdapter;
import com.example.ecommerceapp.models.CartFragmentModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CartFragment extends Fragment {


    List<CartFragmentModel> cartFragmentModelsList;
    RecyclerView recycler_cart;
    CartFragmentAdapter cartFragmentAdapter;

    BottomNavigationView bottomNavigationView;

     FirebaseAuth auth;
     FirebaseFirestore firebaseFirestore;
     TextView overAllPrice;
     Toolbar toolbar;
     AppCompatButton buyBtn;


    public CartFragment() {
        // Required empty public constructor
    }

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_cart, container, false);

        overAllPrice=root.findViewById(R.id.totaL_price);
        buyBtn =root.findViewById(R.id.cart_button);



        assert getActivity() != null;
        bottomNavigationView =getActivity().findViewById(R.id.btmNav);

        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

       //get Data from my cart adapter
        assert getContext() != null;
        // When an assert statement is executed, it checks a given condition and raises an error if the condition evaluates to false.
        // This helps developers catch bugs and verify that certain conditions hold true during execution.
        //assert condition : message;
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,new IntentFilter("MyTotalAmount"));

        recycler_cart =root.findViewById(R.id.myCart_recycler);

        recycler_cart.setLayoutManager(new LinearLayoutManager(getContext()));
        cartFragmentModelsList=new ArrayList<>();
        cartFragmentAdapter =new CartFragmentAdapter(getActivity(),cartFragmentModelsList);
        recycler_cart.setAdapter(cartFragmentAdapter);

        // getting data from AddCArt collection to show recycler view .
        firebaseFirestore.collection("AddCart").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("Users").get().addOnCompleteListener(task -> {
            for (DocumentSnapshot snapshot:task.getResult().getDocuments()) {
                CartFragmentModel cartFragmentModel = snapshot.toObject(CartFragmentModel.class);
                if (cartFragmentModel!=null){
                    cartFragmentModel.setDocumentId(snapshot.getId()); //setDocumentId
                    cartFragmentModelsList.add(cartFragmentModel);
                    cartFragmentAdapter.notifyDataSetChanged();
                }
            }
        });
        return  root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //toolbar
        toolbar=view.findViewById(R.id.cartToolbar);
        if (toolbar!=null){
            // Configure the toolbar as the ActionBar for this fragment
            // You may need to set the toolbar as the ActionBar in the hosting Activity
            ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

            //Inflate the menu for the toolbar.
            toolbar.inflateMenu(R.menu.toolbar_items);

            //Handle menu item clicks.
            // <Class name>::<method name> .The double colon (::) operator, also known as method
            // reference operator in Java, is used to call a method by referring to it with the help of its class directly.
           // toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
            // Optionally, set up toolbar navigation or actions
            // For example, adding a back button
            if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
             //   Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.baseline_menu_24);
                toolbar.setNavigationOnClickListener(v -> {
                    requireActivity().onBackPressed();
                    bottomNavigationView.setSelectedItemId(R.id.nav_home);
                });

            }
        }
    }


    public BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
        @Override
        public void onReceive(Context context, Intent intent) {
         int totalBill = intent.getIntExtra("totalAmount",0);
         overAllPrice.setText("Total Amount : $"+totalBill);

            buyBtn.setOnClickListener(v -> {
                //passing data to address activity.
                Intent intent1 =new Intent(getContext(), AddressActivity.class);
                intent1.putExtra("totalAmount",totalBill);
              //  Toast.makeText(getContext(), "totalAmount: " + totalBill, Toast.LENGTH_SHORT).show();
                startActivity(intent1);

                firebaseFirestore.collection("AddCart").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                        .collection("Users").get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                                    snapshot.getReference().delete(); // Delete the item
                                }
                                // Clear the local list and notify the adapter
                                cartFragmentModelsList.clear();
                                cartFragmentAdapter.notifyDataSetChanged();
                            }
                        });
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        assert getContext()!=null;
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }
}