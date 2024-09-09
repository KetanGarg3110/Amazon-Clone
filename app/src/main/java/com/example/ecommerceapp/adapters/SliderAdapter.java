package com.example.ecommerceapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.ecommerceapp.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater; //, it is used to convert XML layouts into actual View objects that can be used in Java code.

    public SliderAdapter(Context context){
        this.context=context;
    }


    int[] imageArr ={
            R.drawable.on_board_screen1,
            R.drawable.on_board_screen2,
            R.drawable.on_board_screen3
    };
    int[] headingArr ={
            R.string.first_slide,
            R.string.second_slide,
            R.string.third_slide
    };
    int[] descriptionArr ={
            R.string.description,
            R.string.description,
            R.string.description
    };

    @Override
    public int getCount() {//getCount(): Returns the number of pages.
        return headingArr.length;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
       //isViewFromObject(View view, Object object): Determines whether a page View is associated with a specific key object.
        return view== object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.sliding_layout,container,false);

        ImageView imageView =view.findViewById(R.id.slider_img);
        TextView heading =view.findViewById(R.id.heading);
        TextView description =view.findViewById(R.id.description);

        imageView.setImageResource(imageArr[position]);
        heading.setText(headingArr[position]);
        description.setText(descriptionArr[position]);

        container.addView(view);

        return  view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
