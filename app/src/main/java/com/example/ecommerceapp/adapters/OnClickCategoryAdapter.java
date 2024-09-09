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
import com.example.ecommerceapp.activities.DetailedActivity;
import com.example.ecommerceapp.models.OnClickCategoryModel;

import java.util.List;

public class OnClickCategoryAdapter extends RecyclerView.Adapter<OnClickCategoryAdapter.ViewHolder> {

  private final Context context;
   private final List<OnClickCategoryModel> OnClickCategoryModels;
    public static final String DETAILED_SEARCH ="detailed";

    public OnClickCategoryAdapter(Context context, List<OnClickCategoryModel> OnClickCategoryModels) {
        this.context = context;
        this.OnClickCategoryModels = OnClickCategoryModels;
    }

    @NonNull
    @Override
    public OnClickCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.onclickcategory, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OnClickCategoryAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(OnClickCategoryModels.get(position).getImg_url()).into(holder.productImg);
        holder.productPrice.setText(String.valueOf(OnClickCategoryModels.get(position).getPrice()));
        holder.productName.setText(OnClickCategoryModels.get(position).getName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            intent= new Intent(context, DetailedActivity.class);
            intent.putExtra(DETAILED_SEARCH, OnClickCategoryModels.get(position));
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return OnClickCategoryModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView productName, productPrice;
        ImageView productImg ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImg =itemView.findViewById(R.id.onClick_img);
            productName =itemView.findViewById(R.id.onClick_name);
            productPrice =itemView.findViewById(R.id.onClick_price);
        }
    }
}
