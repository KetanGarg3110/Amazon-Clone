package com.example.ecommerceapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.activities.OnClickCategory;
import com.example.ecommerceapp.fragments.SearchFragment;
import com.example.ecommerceapp.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final Context context;
    private final List<CategoryModel> list;
    public static final String TYPE ="type";

    public CategoryAdapter(Context context, List<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.catagory_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
         // Glide's primary focus is on making scrolling any kind of a list of images as smooth and fast as possible, but Glide is also
        // effective for almost any case where you need to fetch, resize, and display a remote image.
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.catImg);
        holder.catTxt.setText(list.get(position).getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, OnClickCategory.class);
            intent.putExtra(TYPE,list.get(position).getType());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView catImg;
        TextView catTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catImg =itemView.findViewById(R.id.cat_img);
            catTxt =itemView.findViewById(R.id.cat_txt);

        }
    }
}
