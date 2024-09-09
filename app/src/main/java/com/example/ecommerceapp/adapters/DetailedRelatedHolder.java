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
import com.example.ecommerceapp.models.DetailedRelatedModel;

import java.util.List;

public class DetailedRelatedHolder extends RecyclerView.Adapter<DetailedRelatedHolder.ViewHolder> {

   private final Context context;
  private final   List<DetailedRelatedModel> detailedRelatedModelList;
    public static final String DETAILED_SEARCH ="detailed";

    public DetailedRelatedHolder(Context context, List<DetailedRelatedModel> detailedRelatedModelList) {
        this.context = context;
        this.detailedRelatedModelList = detailedRelatedModelList;
    }

    @NonNull
    @Override
    public DetailedRelatedHolder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.related_prod_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailedRelatedHolder.ViewHolder holder, int position) {

       Glide.with(context).load(detailedRelatedModelList.get(position).getImg_url()).into(holder.relatedProductImg);
        holder.relatedProductName.setText(detailedRelatedModelList.get(position).getName());
       holder.relatedProductPrice.setText(String.valueOf(detailedRelatedModelList.get(position).getPrice()));
        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            intent= new Intent(context, DetailedActivity.class);
            intent.putExtra(DETAILED_SEARCH, detailedRelatedModelList.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return detailedRelatedModelList.size();
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView relatedProductName,relatedProductPrice;
        public ImageView relatedProductImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relatedProductImg=itemView.findViewById(R.id.product_img);
            relatedProductName=itemView.findViewById(R.id.product_name);
            relatedProductPrice=itemView.findViewById(R.id.price_name);
        }
    }
}
