package com.example.ecommerceapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapters.SliderAdapter;

import java.util.Objects;

public class OnBoardActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;
    SliderAdapter sliderAdapter;
    Button btn_get_started;
    Animation animation;
    TextView[] dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_board);

        //hide toolbar
//        Objects.requireNonNull(getSupportActionBar()).hide();


        viewPager=findViewById(R.id.viewSlider);
        btn_get_started=findViewById(R.id.get_Started_Button);
        dotsLayout =findViewById(R.id.dots);

        addDotsIndicator(0);      // they show default position of dot.

        viewPager.addOnPageChangeListener(changeListener);

        //call adapter
        sliderAdapter =new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);  // to connet the viewholder and view

        btn_get_started.setOnClickListener(view -> {
            Intent intent =new Intent(OnBoardActivity.this, Intro_SignIn.class);
            startActivity(intent);
            finish();  // it is used to when user press button , the previous activity was not open . they remove back stack activity remove
        });

    }

    private void addDotsIndicator(int position) {
        dotsLayout.removeAllViews();
        dots= new TextView[3];            //no of dots

        for (int i=0;i<dots.length;i++){
            dots[i] =new TextView(this);
          dots[i].setText(Html.fromHtml("&#8226;"));
           dots[i].setTextSize(35);       //dot size
           dotsLayout.addView(dots[i]);
        }
        if (dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.purple_200));
        }
    }

    ViewPager.OnPageChangeListener changeListener =new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
        addDotsIndicator(position);
        if (position==0){
            btn_get_started.setVisibility(View.INVISIBLE);
        } else if (position==1) {
            btn_get_started.setVisibility(View.INVISIBLE);
        } else if (position==2) {
            animation = AnimationUtils.loadAnimation(OnBoardActivity.this,R.anim.sliding_btn_animation);
            btn_get_started.setAnimation(animation);
            btn_get_started.setVisibility(View.VISIBLE);
        }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}