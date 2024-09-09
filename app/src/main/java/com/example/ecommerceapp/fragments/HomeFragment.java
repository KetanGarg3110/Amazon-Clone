package com.example.ecommerceapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.activities.RegistrationActivity;
import com.example.ecommerceapp.adapters.CategoryAdapter;
import com.example.ecommerceapp.adapters.NewProductAdapter;
import com.example.ecommerceapp.adapters.PopularProductAdapter;
import com.example.ecommerceapp.models.CategoryModel;
import com.example.ecommerceapp.models.NewProductModel;
import com.example.ecommerceapp.models.PopularProductModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    RecyclerView cat_recyclerView;
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelsList;
    RecyclerView newPro_recyclerView;
    NewProductAdapter newProductAdapter;
    List<NewProductModel> newProductModelList;
    FirebaseFirestore db;
    TextView catSeeAll,newSellAll,popSeeAll;
    ImageSlider imageSlider;
    RecyclerView popRecyclerView;
    PopularProductAdapter popularProductAdapter;
    FirebaseAuth auth;
    List<PopularProductModel> popularProductModelList;
    Toolbar toolbar;
    Intent cartIntent;
    BottomNavigationView bottomNavigationView;
    String cartAdd;

    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint({"NotifyDataSetChanged", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //esme java file se xml file link hoti hai.
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        catSeeAll = root.findViewById(R.id.Category_See_All);
        newSellAll = root.findViewById(R.id.New_Product_See_All);
        popSeeAll = root.findViewById(R.id.popular_products_see_all);
        assert getActivity() != null;
        bottomNavigationView=getActivity().findViewById(R.id.btmNav);

        auth= FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        catSeeAll.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.home_container, new SearchFragment()) // Replace with your container ID
                .addToBackStack(null) // Optional: Add to back stack for navigation history
                .commit());
        newSellAll.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.home_container, new SearchFragment()) // Replace with your container ID
                .addToBackStack(null) // Optional: Add to back stack for navigation history
                .commit());
        popSeeAll.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.home_container, new SearchFragment()) // Replace with your container ID
                .addToBackStack(null) // Optional: Add to back stack for navigation history
                .commit());



        cat_recyclerView = root.findViewById(R.id.recycler_category);
        newPro_recyclerView = root.findViewById(R.id.rec_new_product);
        popRecyclerView = root.findViewById(R.id.rec_popular_products);



        //Image slider
        imageSlider = root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.shoes_banner1, "Discount On shoes Item", ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.burger_banner, "50% Off", ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.watch_banner1, "10% Off", ScaleTypes.FIT));
        imageSlider.setImageList(slideModels);

       onCreateFirebaseData();

        return root;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onCreateFirebaseData() {
        //popularProduct recyclerview
        popRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        popularProductModelList = new ArrayList<>();
        popularProductAdapter = new PopularProductAdapter(getContext(), popularProductModelList);
        popRecyclerView.setAdapter(popularProductAdapter);

        db.collection("PopularProducts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            PopularProductModel popularProductModel = document.toObject(PopularProductModel.class);
                            // adding data in recycler view with help of add .add main structure chiye hoya to upper side main pPm ko crete karna hoga
                            popularProductModelList.add(popularProductModel);
                            popularProductAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });

        //category
        cat_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        categoryModelsList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryModelsList);
//        cat_recyclerView.setHasFixedSize(true);
//        cat_recyclerView.setDrawingCacheEnabled(true);
//        cat_recyclerView.setItemViewCacheSize(24);
//        cat_recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        cat_recyclerView.setAdapter(categoryAdapter);

//    Firebase database on category model
        db.collection("CategoryModel")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CategoryModel categoryModel = document.toObject(CategoryModel.class);
                            categoryModelsList.add(categoryModel);
                            categoryAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
        // firebase data base on category model
        //  firstly set on recycler in which whose scroll view are applied.
        newPro_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        // then using arraylist because of we know array list se over array use kar sakte hai.
        newProductModelList = new ArrayList<>();
        // now calling a parameterised constructor.
        newProductAdapter = new NewProductAdapter(getContext(), newProductModelList);
//        newPro_recyclerView.setHasFixedSize(true);
//        newPro_recyclerView.setDrawingCacheEnabled(true);
//        newPro_recyclerView.setItemViewCacheSize(24);
//        newPro_recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        // set adapter on recycler view.
        newPro_recyclerView.setAdapter(newProductAdapter);

        db.collection("NewProduct")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            NewProductModel newProductModel = document.toObject(NewProductModel.class);
                            newProductModelList.add(newProductModel);
                            newProductAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Required to handle menu events in the fragment

        //Access argument
        if (getArguments()!=null){
            cartAdd=getArguments().getString("cartAdd");
            // You can use this data to customize the menu
            Log.d("HomeFragment", "Received cartAdd: " + cartAdd);
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_items, menu);

//
//        MenuItem cartMenuItem = menu.findItem(R.id.add_menu_Cart);
//        MenuItem cartDotMenuItem = menu.findItem(R.id.add_menu_Cart_dot);

//        if (HomeCartFunctions.getItemCount() > 0) {
//            cartMenuItem.setVisible(false);
//            cartDotMenuItem.setVisible(true);
//        } else {
//            cartMenuItem.setVisible(true);
//            cartDotMenuItem.setVisible(false);
//        }
     //   updateCartIcon("true".equals(cartAdd));
        MenuItem menuItem;
        Bundle bundle = getArguments();
        if (bundle!=null){
            String cartAdd = bundle.getString("cartAdd");

            if ("true".equals(cartAdd)){
//                menu.findItem(R.id.add_menu_Cart_dot).setVisible(true);
//                menu.findItem(R.id.add_menu_Cart).setVisible(false);
             menuItem=  menu.findItem(R.id.add_menu_Cart_dot);
             menuItem.setIcon(R.drawable.icon_dot);
            } else if ("false".equals(cartAdd)) {
//                menu.findItem(R.id.add_menu_Cart_dot).setVisible(false);
//                menu.findItem(R.id.add_menu_Cart).setVisible(true);
                menuItem=  menu.findItem(R.id.add_menu_Cart);
                menuItem.setIcon(R.drawable.cart_icon);

            }else {
//                menu.findItem(R.id.add_menu_Cart_dot).setVisible(false);
//                menu.findItem(R.id.add_menu_Cart).setVisible(true);
                menuItem=  menu.findItem(R.id.add_menu_Cart);
                menuItem.setIcon(R.drawable.cart_icon);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //toolbar
        toolbar=view.findViewById(R.id.toolbar);
        if (toolbar!=null){
            // Configure the toolbar as the ActionBar for this fragment
            // You may need to set the toolbar as the ActionBar in the hosting Activity
            ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

            //Inflate the menu for the toolbar.
            toolbar.inflateMenu(R.menu.toolbar_items);

           //Handle menu item clicks.
           // <Class name>::<method name> .The double colon (::) operator, also known as method
           // reference operator in Java, is used to call a method by referring to it with the help of its class directly.
            toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
            // Optionally, set up toolbar navigation or actions
            // For example, adding a back button
            if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.baseline_menu_24);
              //  toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

            }
        }
        }

    @SuppressLint("NonConstantResourceId")
    private boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

        case R.id.add_menu_Cart:
            //Open cart
            Toast.makeText(getContext(), "OPEN CART", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.home_container, new CartFragment()) // Replace with your container ID
                    .addToBackStack(null) // Optional: Add to back stack for navigation history
                    .commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_cart);
            // Refresh the menu
            requireActivity().invalidateOptionsMenu();
                return true;

        case R.id.log_out:
        Toast.makeText(getContext(), "log out", Toast.LENGTH_SHORT).show();
        auth.signOut();
        startActivity(new Intent(getContext(), RegistrationActivity.class));
               return true;
        default:
        return false;
       }
    }


//    public void updateCartIcon(boolean showNotification) {
//        Menu menu = toolbar.getMenu();
//        if (menu != null) {
//            MenuItem menuItem = menu.findItem(R.id.add_menu_Cart_dot);
//            if (showNotification) {
//                menuItem.setVisible(true);
//                menuItem.setIcon(R.drawable.icon_dot); // Notification dot icon
//            } else {
//                menuItem.setVisible(false);
//            }
//        }
//    }
}
