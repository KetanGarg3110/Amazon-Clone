package com.example.ecommerceapp.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.models.AddressModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

  private  final   Context context;
   private final List<AddressModel> addressList;
   private int selectedPosition = -1; //No selection by default.
    private RadioButton selectedRadioBtn;

    public AddressAdapter(Context context, List<AddressModel> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.address_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {

        holder.address_address.setText(addressList.get(position).getAddress());
        holder.address_city.setText(addressList.get(position).getCity());
        holder.address_name.setText(addressList.get(position).getName());
        holder.address_phone.setText(addressList.get(position).getPhoneNo());
        holder.address_postalCode.setText(addressList.get(position).getPostalCode());
    //    This line of code sets the state of a radio button. It checks if the current position matches the selectedPosition and, if so, marks the radio button as selected (checked).
        Log.d("Given positon","position"+position);
        holder.radioButton.setChecked(position == selectedPosition);

//        holder.itemView.setOnClickListener(v -> {
//            // when user click postion
//            Log.d("SelectedPosition1","selectedPosition"+selectedPosition);
//            int oldPosition = selectedPosition;
//            selectedPosition = holder.getBindingAdapterPosition();// Use bindingAdapterPosition instead
//            notifyItemChanged(oldPosition);// updating the old position
//            notifyItemChanged(selectedPosition);//updating the new position
//        });

    holder.radioButton.setOnClickListener(v -> {
        Log.d("SelectedRadioBtn","Position clicked"+position);
        if (selectedRadioBtn !=null){
            selectedRadioBtn.setChecked(false);
        }
        selectedRadioBtn=(RadioButton) v;
        selectedRadioBtn.setChecked(true);
        selectedPosition = holder.getBindingAdapterPosition();
    });
        holder.address_add.setOnLongClickListener(v -> {
//            new AlertDialog.Builder(context)
//                    .setTitle("Delete Address")
//                    .setMessage("Are you sure you want to delete this Address?")
//                    .setIcon(R.drawable.baseline_delete_24)
//                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Assuming each cart item has a unique documentId field
                        String documentId = addressList.get(position).getDocumentId(); // Add a getDocumentId method in your model
                        removeItemFromFirebase(documentId, position);
//                    }).setNegativeButton("No", null)
//        .show();
            return true;
        });
    }

    private void removeItemFromFirebase(String documentId, int position) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth =FirebaseAuth.getInstance();

        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        firestore.collection("CurrentUser")
                .document(userId)
                .collection("Address")
                .document(documentId) // Use the documentId of the item to be removed
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Successfully removed from Firebase
                    addressList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Address deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(context, "Failed to delete address: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }


    //Returns the currently selected address from the list.
    public AddressModel getSelectedAddress() {
        Log.d("SelectedPosition2","selectedPosition"+selectedPosition);
        Log.d("AddressListDebug", "Address list size: " + addressList.size());
        if (selectedPosition >= 0 && selectedPosition < addressList.size()) {
            return addressList.get(selectedPosition);
        }
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView address_name,address_phone,address_address,address_city,address_postalCode;
        LinearLayout address_add;
        RadioButton radioButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           // address=itemView.findViewById(R.id.address_add);
            address_address=itemView.findViewById(R.id.address_address);
            address_city=itemView.findViewById(R.id.address_city);
            address_name=itemView.findViewById(R.id.address_name);
            address_phone=itemView.findViewById(R.id.address_phone);
            address_postalCode=itemView.findViewById(R.id.address_postal);
            radioButton=itemView.findViewById(R.id.addressRadioButton);
            address_add=itemView.findViewById(R.id.address_add);
        }
    }

}
