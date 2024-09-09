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
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.activities.DetailedActivity;
import com.example.ecommerceapp.models.SearchFragmentModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFragmentAdapter extends RecyclerView.Adapter<SearchFragmentAdapter.ViewHolder> {

    Context context;
  public  final List<SearchFragmentModel> arrayListSearch ;
   public final List<SearchFragmentModel> filteredDataList ;
  //  List<SearchFragmentModel> arrayList=new ArrayList<SearchFragmentModel>();

    public static final String DETAILED_SEARCH ="detailed";

    public SearchFragmentAdapter(Context context ,List<SearchFragmentModel> arrayListSearch) {
        this.context = context;
        this.arrayListSearch = arrayListSearch;
        this.filteredDataList=new ArrayList<>(arrayListSearch); // Initialize filtered list

    }

    @NonNull
    @Override
    public SearchFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_prod_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFragmentAdapter.ViewHolder holder, int position) {

        SearchFragmentModel searchFragmentModel= filteredDataList.get(position);
//        Object dataItem = filteredDataList.get(position);
//        String dataString;
//        if (dataItem != null) {
//            dataString = dataItem.toString(); // Ensure this is a meaningful conversion
//        } else {
//            dataString = "";

//        }

        Glide.with(context).load(searchFragmentModel.getImg_url()).into(holder.newImg);
        holder.priceTxt.setText(String.valueOf(searchFragmentModel.getPrice()));
        holder.prodTxt.setText(searchFragmentModel.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            intent= new Intent(context, DetailedActivity.class);
            intent.putExtra(DETAILED_SEARCH,searchFragmentModel);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return filteredDataList.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void filter(String query) {
      //  filteredDataList.clear();
        List<SearchFragmentModel> matchedUsers = new ArrayList<>();
        List<SearchFragmentModel> unmatchedUsers = new ArrayList<>();
        if (query.isEmpty()) {
            matchedUsers.addAll(arrayListSearch);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (SearchFragmentModel data : arrayListSearch) {
                if (data.getName().toLowerCase().contains(lowerCaseQuery) ){
                    matchedUsers.add(data);
                }else {
                    unmatchedUsers.add(data);
                }
            }
        }
        // Combine matched and unmatched lists
        filteredDataList.clear();
        filteredDataList.addAll(matchedUsers);
        filteredDataList.addAll(unmatchedUsers);

        notifyDataSetChanged();
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
