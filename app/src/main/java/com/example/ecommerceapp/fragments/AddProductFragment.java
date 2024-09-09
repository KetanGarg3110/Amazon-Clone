package com.example.ecommerceapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.example.ecommerceapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddProductFragment extends Fragment {


  //  LinearLayout bottomNavbar;
    Toolbar toolbar;

    ImageView addProdImg;
    EditText addProductName,addProductPrice,addProductDescription,addProductCategory;
    AppCompatButton confirmBtn;

    StorageReference imageRef;
    FirebaseStorage storage;
    FirebaseFirestore firebaseFirestore;
    Uri imageUri;

    FirebaseAuth auth;

    String finalImageUri;
  //  private static final int PICK_IMAGE_REQUEST = 1;

    public AddProductFragment() {
        // Required empty public constructor
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    addProdImg.setImageURI(imageUri);
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      return inflater.inflate(R.layout.fragment_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar=view.findViewById(R.id.addProductToolbar);
        ((AppCompatActivity ) requireActivity()).setSupportActionBar(toolbar);

       Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            HomeFragment homeFragment=new HomeFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.home_container,homeFragment).addToBackStack(null).commit();
        });

        addProductCategory=view.findViewById(R.id.addProductCategory);
        addProductDescription=view.findViewById(R.id.addProductDescription);
        addProductName=view.findViewById(R.id.addProductName);
        addProductPrice=view.findViewById(R.id.addProductPrice);
        confirmBtn=view.findViewById(R.id.confirm_button);
        addProdImg=view.findViewById(R.id.addProdImg);

        storage= FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();

        addProdImg.setOnClickListener(v -> {
            // this iss open the gallery.
            Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        confirmBtn.setOnClickListener(v -> uploadData());

    }

    private void uploadData() {
        // trim removing white spacing both ends.
        String name =addProductName.getEditableText().toString().trim();
        String priceStr =addProductPrice.getEditableText().toString().trim();
        String description =addProductDescription.getEditableText().toString().trim();
        String category =addProductCategory.getEditableText().toString().trim();

        if(TextUtils.isEmpty(name)){
            addProductName.setError("Please enter the name of the product ");
            Toast.makeText(getContext(), "Please fill the the all details correctly before confirming", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(priceStr)) {
            addProductPrice.setError("Please enter the price of the product ");
            Toast.makeText(getContext(), "Please fill the the all details correctly before confirming", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            addProductDescription.setError("Please enter the description of the product ");
            Toast.makeText(getContext(), "Please fill the the all details correctly before confirming", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(category)) {
            addProductCategory.setError("Please enter the category of the product ");
            Toast.makeText(getContext(), "Please fill the the all details correctly before confirming", Toast.LENGTH_SHORT).show();
        } else if (addProdImg==null || addProdImg.getDrawable().equals(R.drawable.add_item_icon)) {
            // it means ya to koi image nahi select kiya hai.nahi to add item icon show hoga.
            Toast.makeText(getContext(), "Please choose the product image", Toast.LENGTH_SHORT).show();
        } else {

           // convert price to number.
            double price =Double.parseDouble(priceStr);



            String saveCurrentTime, saveCurrentDate;
            //It provides methods for manipulating dates and times, allowing developers to handle complex date and time operations more easily.
            Calendar calForDate = Calendar.getInstance();
            // SimpleDateFormat is a concrete class for formatting and parsing dates in a language-independent manner. You can specify a pattern to control the format and parsing of dates.
            @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDateFormat = new SimpleDateFormat("MM dd,yyyy");
            saveCurrentDate = currentDateFormat.format(calForDate.getTime()); // getTime show current time.

            @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTimeFormat.format(calForDate.getTime());


            // Upload image to Firebase Storage
             imageRef = storage.getReference().child("images/" + imageUri.getLastPathSegment());
            if (imageUri != null) {
                imageRef.putFile(imageUri)
                        // Get the download URL of the uploaded image
                        .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {

                                    finalImageUri = uri.toString();
                                    // Prepare data to save in Firestore

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("name", name);
                                    data.put("price", price);
                                    data.put("description", description);
                                    data.put("type", category);
                                    data.put("currentTime", saveCurrentTime);
                                    data.put("currentDate", saveCurrentDate);
                                    data.put("img_url", finalImageUri);

                                    // Save data to Firestore
                                    //.document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                                    //                                            .collection("AddedUserData")
                                    firebaseFirestore.collection("SeeAll")
                                            .add(data)
                                            .addOnSuccessListener(documentReference -> {
                                                Toast.makeText(getContext(), "Product added successfully", Toast.LENGTH_SHORT).show();
                                                clearAllData();
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to add product" + e.getMessage(), Toast.LENGTH_SHORT).show());
                                })).addOnFailureListener(e -> Toast.makeText(getContext(), "Image Upload Failed :" + e.getMessage(), Toast.LENGTH_SHORT).show());

            } else {
                Toast.makeText(getContext(), "image uri is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clearAllData() {
        addProductName.setText("");
        addProductPrice.setText("");
        addProductDescription.setText("");
        addProductCategory.setText("");
        addProdImg.setImageResource(R.drawable.add_item_icon);
    }
}