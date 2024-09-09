package com.example.ecommerceapp.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.fragments.AddProductFragment;
import com.example.ecommerceapp.fragments.CartFragment;
import com.example.ecommerceapp.fragments.HomeFragment;
import com.example.ecommerceapp.fragments.ProfileFragment;
import com.example.ecommerceapp.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

   BottomNavigationView bottomNavigationView;

   FirebaseAuth auth;
   TextView amazonTxt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth= FirebaseAuth.getInstance();
        //hide default action bar
       // Objects.requireNonNull(getSupportActionBar()).hide();

        bottomNavigationView=findViewById(R.id.btmNav);
        amazonTxt =findViewById(R.id.amazonTxt);

        loadFragment(new HomeFragment(),0);

        bottomNavigationView.setOnItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) item -> {
            int id=item.getItemId();

            if (id==R.id.nav_home){
                loadFragment(new HomeFragment(),1);

            } else if (id==R.id.nav_shopping) {
                loadFragment(new AddProductFragment(),1);

            } else if (id==R.id.nav_search) {
                loadFragment(new SearchFragment(),1);

            } else if (id==R.id.nav_cart) {
                loadFragment(new CartFragment(),1);

            } else if (id==R.id.nav_profile) {
                loadFragment(new ProfileFragment(),1);
            }

            return true;// esse pta chle ga kis button per click kiya gaya hai
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }
    private void loadFragment(Fragment fragment,int flag) {

       FragmentManager fm= getSupportFragmentManager();
       FragmentTransaction ft= fm.beginTransaction();

        if (flag==0 ){
            ft.add(R.id.home_container,fragment);
            //jitne bhi stack main fragment hai unko pop karana hoga.
        }else
            ft.replace(R.id.home_container,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}