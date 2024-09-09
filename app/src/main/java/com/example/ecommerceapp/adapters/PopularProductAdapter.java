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
import com.example.ecommerceapp.models.PopularProductModel;

import java.util.List;

public class PopularProductAdapter extends RecyclerView.Adapter<PopularProductAdapter.ViewHolder> {

    private final Context context;
   private final List<PopularProductModel> popularProductModelList;
   public static final String DETAILED_POPULAR  = "detailed";

    public PopularProductAdapter(Context context, List<PopularProductModel> popularProductModelList) {
        this.context = context;
        this.popularProductModelList = popularProductModelList;
    }

    @NonNull
    @Override
    public PopularProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_prod_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularProductAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(popularProductModelList.get(position).getImg_url()).into(holder.newImg);
        holder.prodTxt.setText(popularProductModelList.get(position).getName());
        holder.priceTxt.setText(String.valueOf(popularProductModelList.get(position).getPrice()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, DetailedActivity.class);
            intent.putExtra(DETAILED_POPULAR,popularProductModelList.get(position));
            intent.putExtra("type",popularProductModelList.get(position).getType());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return popularProductModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView newImg ;
        TextView priceTxt;
        TextView prodTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newImg=itemView.findViewById(R.id.popular_img);
            priceTxt=itemView.findViewById(R.id.Product_price);
            prodTxt=itemView.findViewById(R.id.Product_name);
        }
    }
}
