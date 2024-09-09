package com.example.ecommerceapp.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.models.CartFragmentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class CartFragmentAdapter extends RecyclerView.Adapter<CartFragmentAdapter.ViewHolder> {

   Context context;
    List<CartFragmentModel> cartList ;
    int totalAmount =0;

    public CartFragmentAdapter(Context context, List<CartFragmentModel> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mycart_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartFragmentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.date.setText(cartList.get(position).getCurrentDate());
        holder.name.setText(cartList.get(position).getProductName());
        holder.time.setText(cartList.get(position).getCurrentTime());
        holder.price.setText(cartList.get(position).getProductPrice()+"&");
        holder.totalQuantity.setText(cartList.get(position).getTotalQunatity());
        holder.totalPrice.setText(String.valueOf(cartList.get(position).getTotalPrice()));

        // Update total amount
        updateTotalAmount();


        totalAmount =totalAmount +cartList.get(position).getTotalPrice();

        holder.linearCart.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Contact")
                    .setMessage("Are you sure you want to delete this item from cart?")
                            .setIcon(R.drawable.baseline_delete_24)
                                    .setPositiveButton("Yes", (dialog, which) -> {
                                        // Assuming each cart item has a unique documentId field
                                        String documentId = cartList.get(position).getDocumentId(); // Add a getDocumentId method in your model
                                        removeItemFromFirebase(documentId, position);
                                    }).setNegativeButton("No", (dialog, which) -> {})
                            .show();
            return true;
        });
    }

    private void removeItemFromFirebase(String documentId, int position) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth =FirebaseAuth.getInstance();

        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        firestore.collection("AddCart")
                .document(userId)
                .collection("Users")
                .document(documentId) // Use the documentId of the item to be removed
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Successfully removed from Firebase
                    cartList.remove(position);
                    notifyItemRemoved(position);
                    // Optionally, update the total amount
                    updateTotalAmount();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(context, "Failed to delete item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateTotalAmount() {
        totalAmount =0; //Reset total amount
        for (CartFragmentModel item : cartList){
            totalAmount +=item.getTotalPrice();
        }
        //total amount pass to cart activity
        Intent intent =new Intent("MyTotalAmount");
        intent.putExtra("totalAmount",totalAmount);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView date,time,price,name,totalPrice,totalQuantity;
        LinearLayout linearCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.CurrentDate);
            time=itemView.findViewById(R.id.Currenttime);
            price=itemView.findViewById(R.id.product_price);
            name=itemView.findViewById(R.id.product_name);
            totalPrice=itemView.findViewById(R.id.totaL_price);
            totalQuantity=itemView.findViewById(R.id.total_quantity);
            linearCart=itemView.findViewById(R.id.linear_cart);
        }
    }
}
