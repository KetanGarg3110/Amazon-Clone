package com.example.ecommerceapp.adapters;

import android.annotation.SuppressLint;
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
import com.example.ecommerceapp.activities.DetailedActivity;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.models.NewProductModel;

import java.util.List;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.ViewHolder> {
// extends hum tabhi lagate hai jab hamko Recycler view ki properties ko hold kre apply kre
    // means inherit kare
    private final Context context;
    public static final String DETAILED_DATA ="detailed";
    private final List<NewProductModel> newProductModelList;

    public NewProductAdapter(Context context, List<NewProductModel>  newProductModelList) {
        this.context = context;
        this. newProductModelList = newProductModelList;
    }

    @NonNull
    @Override
    public NewProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.newproduct_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewProductAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(newProductModelList.get(position).getImg_url()).into(holder.newimg);
        holder.newProduct.setText(newProductModelList.get(position).getName());
        holder.newPrice.setText(String.valueOf(newProductModelList.get(position).getPrice()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            intent= new Intent(context, DetailedActivity.class);
            intent.putExtra(DETAILED_DATA,newProductModelList.get(position));
            intent.putExtra("type",newProductModelList.get(position).getType());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newProductModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView newimg;
        TextView newProduct, newPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            newimg=itemView.findViewById(R.id.img_newProduct);
            newPrice=itemView.findViewById(R.id.price_name);
            newProduct=itemView.findViewById(R.id.Product_name);
        }
    }
}
