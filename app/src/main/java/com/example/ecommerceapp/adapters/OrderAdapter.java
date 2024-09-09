package com.example.ecommerceapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.models.OrderModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    Context context;
    List<OrderModel> orderModelList;

    public OrderAdapter(Context context, List<OrderModel> orderModelList) {
        this.context = context;
        this.orderModelList = orderModelList;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {

        holder.orderName.setText(orderModelList.get(position).getName());
        holder.orderDate.setText(orderModelList.get(position).getCurrentDate());
        if (orderModelList.get(position).getTotalPrice()!=0){
            holder.orderPrice.setText(String.valueOf(orderModelList.get(position).getTotalPrice()));
        }else {
            holder.orderPrice.setText(String.valueOf(orderModelList.get(position).getTotalAmount()));
        }
        holder.orderCity.setText(orderModelList.get(position).getCity());
        holder.orderPhone.setText(orderModelList.get(position).getPhone());
        holder.orderAddress.setText(orderModelList.get(position).getAddress());

    }
    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView orderName, orderDate, orderPrice,orderCity,orderPhone, orderAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderAddress=itemView.findViewById(R.id.addressTxt);
            orderName=itemView.findViewById(R.id.nameTxt);
            orderDate=itemView.findViewById(R.id.orderDate);
            orderCity=itemView.findViewById(R.id.cityTxt);
            orderPhone=itemView.findViewById(R.id.phoneNumber);
            orderPrice=itemView.findViewById(R.id.orderPrice);
        }
    }
}
