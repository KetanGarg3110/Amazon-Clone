package com.example.ecommerceapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapters.DetailedRelatedHolder;
import com.example.ecommerceapp.fragments.CartFragment;
import com.example.ecommerceapp.fragments.HomeFragment;
import com.example.ecommerceapp.models.DetailedRelatedModel;
import com.example.ecommerceapp.models.NewProductModel;
import com.example.ecommerceapp.models.OnClickCategoryModel;
import com.example.ecommerceapp.models.PopularProductModel;
import com.example.ecommerceapp.models.SearchFragmentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DetailedActivity extends AppCompatActivity {

    ImageView detiledImg;
    TextView detailed_desc,detailed_price,detailed_quat,detailed_name,detailed_rating,detailed_category;
    Button btn_addCart,btn_buyNow;
    ImageView addItem,removeItem;


    //new products
    NewProductModel newProductModel=null;
    //popular product
    PopularProductModel popularProductModel=null;
    //search
    SearchFragmentModel searchFragmentModel=null;
    //related product
    RecyclerView related_pro_recycler;
    DetailedRelatedHolder detailedRelatedHolder;
    List<DetailedRelatedModel> detailedRelatedModelList;
    
    //onclickCategoryModel
    OnClickCategoryModel onClickCategoryModel=null;
    DetailedRelatedModel detailedRelatedModel =null;

    int totalQunatity =1;
    int totalPrice =0;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
     FirebaseFirestore firebaseFirestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        toolbar =findViewById(R.id.detailed_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        String type = getIntent().getStringExtra("type");

        //related_product_recyclerView
        related_pro_recycler=findViewById(R.id.related_pro_list);
        related_pro_recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        detailedRelatedModelList=new ArrayList<>();
        detailedRelatedHolder=new DetailedRelatedHolder(this,detailedRelatedModelList);
        related_pro_recycler.setAdapter(detailedRelatedHolder);

        //Define the collection names and types filters
        List<String> collectionNames = List.of("SeeAll","NewProduct","PopularProducts");

        fetchDataFromCollections(collectionNames,type);

        //getting data from
        final  Object obj =getIntent().getSerializableExtra("detailed");
      //  instanceof is a keyword used for checking if a reference variable contains a given type of object reference or not
      // Henceforth it is known as a comparison operator where the instance is getting compared to type returning boolean
      // true or false as in Java we do not have 0 and 1 boolean return types.
        if(obj instanceof NewProductModel){
            newProductModel =(NewProductModel) obj;
        } else if (obj instanceof  PopularProductModel) {
            popularProductModel=(PopularProductModel) obj;
        } else if (obj instanceof SearchFragmentModel) {
            searchFragmentModel=(SearchFragmentModel) obj;
        }else if (obj instanceof OnClickCategoryModel) {
            onClickCategoryModel=(OnClickCategoryModel) obj;
        }else if (obj instanceof DetailedRelatedModel) {
            detailedRelatedModel=(DetailedRelatedModel) obj;
        }

        detiledImg=findViewById(R.id.detailed_img);
        detailed_desc=findViewById(R.id.detailed_desc);
        detailed_name=findViewById(R.id.product_name);
        detailed_quat=findViewById(R.id.quantity);
        detailed_price=findViewById(R.id.price);
        detailed_rating=findViewById(R.id.rating_no);
        detailed_category=findViewById(R.id.product_category);


        btn_addCart=findViewById(R.id.addCart);
        btn_buyNow=findViewById(R.id.buyNow);

        addItem=findViewById(R.id.addItem);
        removeItem=findViewById(R.id.remove_item);


         //New Product Model
        if(newProductModel !=null){
            //instead of glide we use Picasso.get.load(newProductModel.getImg_url()).into(detailed_img);
            // detailed_img.setImageResource(newProductModel.getImg_url());
            Glide.with(getApplicationContext()).load(newProductModel.getImg_url()).into(detiledImg);
            // in this we have also write this way
            //detailed_name.setText(getIntent().getSerializableExtra("detailed").getName()); in this detaliled key instead we put value as a one by one.
            detailed_name.setText(newProductModel.getName());
            detailed_rating.setText(newProductModel.getRating());
            detailed_price.setText(String.valueOf(newProductModel.getPrice()));
            detailed_desc.setText(newProductModel.getDescription());
            totalPrice= newProductModel.getPrice() * totalQunatity;

        }
        //popular product.
        if(popularProductModel !=null){
            Glide.with(getApplicationContext()).load(popularProductModel.getImg_url()).into(detiledImg);
            detailed_name.setText(popularProductModel.getName());
            detailed_rating.setText(popularProductModel.getRating());
            detailed_price.setText(String.valueOf(popularProductModel.getPrice()));
            detailed_desc.setText(popularProductModel.getDescription());
            totalPrice= popularProductModel.getPrice() * totalQunatity;

        }

        //search
        if(searchFragmentModel !=null){
            Glide.with(getApplicationContext()).load(searchFragmentModel.getImg_url()).into(detiledImg);
            detailed_name.setText(searchFragmentModel.getName());
            detailed_rating.setText(searchFragmentModel.getRating());
            detailed_price.setText(String.valueOf(searchFragmentModel.getPrice()));
            detailed_desc.setText(searchFragmentModel.getDescription());
            totalPrice= searchFragmentModel.getPrice() * totalQunatity;


        }

        //onClickCategoryModel
        if(onClickCategoryModel !=null){
            Glide.with(getApplicationContext()).load(onClickCategoryModel.getImg_url()).into(detiledImg);
            detailed_name.setText(onClickCategoryModel.getName());
         detailed_rating.setText(onClickCategoryModel.getRating());
            detailed_price.setText(String.valueOf(onClickCategoryModel.getPrice()));
          detailed_desc.setText(onClickCategoryModel.getDescription());
            totalPrice= onClickCategoryModel.getPrice() * totalQunatity;
        }
        if(detailedRelatedModel !=null){
            Glide.with(getApplicationContext()).load(detailedRelatedModel.getImg_url()).into(detiledImg);
            detailed_name.setText(detailedRelatedModel.getName());
            detailed_rating.setText(detailedRelatedModel.getRating());
            detailed_price.setText(String.valueOf(detailedRelatedModel.getPrice()));
            detailed_desc.setText(detailedRelatedModel.getDescription());
            totalPrice= detailedRelatedModel.getPrice() * totalQunatity;
        }

        //Buy Now button
        btn_buyNow.setOnClickListener(v -> {
            Intent intent= new Intent(DetailedActivity.this,AddressActivity.class);
            // we using newproductModel to nevigate the content.  item =key hai jo intent passing main kam aye ga . and newProductModel main data store hoga.
            if (newProductModel!=null){
                intent.putExtra("item ",newProductModel);
            }
            if (popularProductModel!=null){
                intent.putExtra("item ",popularProductModel);
            }
            if (searchFragmentModel!=null){
                intent.putExtra("item ",searchFragmentModel);
            }
            intent.putExtra("totalPrice",totalPrice);
            intent.putExtra("totalQuantity", totalQunatity);
            startActivity(intent);
            finish();
        });

        // Add cart button
        btn_addCart.setOnClickListener(v -> addToCart());

        addItem.setOnClickListener(v -> {
            if (totalQunatity <10){
                totalQunatity++;
                detailed_quat.setText(String.valueOf(totalQunatity));

                if (newProductModel!=null){
                    totalPrice= newProductModel.getPrice() * totalQunatity;
                }
                if (popularProductModel!=null){
                    totalPrice= popularProductModel.getPrice() * totalQunatity;
                }
                if (searchFragmentModel!=null){
                    totalPrice= searchFragmentModel.getPrice() * totalQunatity;
                }
                if (onClickCategoryModel!=null){
                    totalPrice= onClickCategoryModel.getPrice() * totalQunatity;
                }
                if (detailedRelatedModel!=null){
                    totalPrice= detailedRelatedModel.getPrice() * totalQunatity;
                }
            }
        });

        removeItem.setOnClickListener(v -> {
            if (totalQunatity>1){
                totalQunatity--;
                detailed_quat.setText(String.valueOf(totalQunatity));
            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchDataFromCollections(List<String> collectionNames, String type) {
        for (String collectionName : collectionNames) {
            fetchDataFromCollection(collectionName, type);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchDataFromCollection(String collectionName , String type) {

        //Handling Queries: Created explicit Query objects and used them to execute queries.

        CollectionReference collectionRef = firebaseFirestore.collection(collectionName);
        Query query = collectionRef;

        if (type != null && !type.isEmpty()) {
            query = collectionRef.whereEqualTo("type", type);
            // type is catagory name .
            // and "type" is field name .
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot snapshot : querySnapshot.getDocuments()) {
                        DetailedRelatedModel detailedRelatedModel = snapshot.toObject(DetailedRelatedModel.class);
                        if (detailedRelatedModel != null) {
                            detailedRelatedModelList.add(detailedRelatedModel);
                        }
                    }
                    detailedRelatedHolder.notifyDataSetChanged();
                }
            } else {
                // Handle error
                if (task.getException() != null) {
                    task.getException().printStackTrace();
                }
            }
        });
    }
    private void addToCart() {
       final  String saveCurrentTime ,saveCurrentDate;

        //It provides methods for manipulating dates and times, allowing developers to handle complex date and time operations more easily.
        Calendar calForDate = Calendar.getInstance();
        // SimpleDateFormat is a concrete class for formatting and parsing dates in a language-independent manner. You can specify a pattern to control the format and parsing of dates.
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDateFormat=new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate = currentDateFormat.format(calForDate.getTime()); // getTime show current time.

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTimeFormat= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime =currentTimeFormat.format(calForDate.getTime());
       // HashMap is part of the java.util package and provides a way to store key-value pairs. It implements the Map interface and allows you to store and retrieve data based on keys.
        final HashMap<String,Object> map =new HashMap<>();

        map.put("productName",detailed_name.getText().toString());
        map.put("productPrice",detailed_price.getText().toString());
        map.put("currentTime",saveCurrentTime);
        map.put("currentDate",saveCurrentDate);
        map.put("TotalQunatity",detailed_quat.getText().toString());
        map.put("TotalPrice",totalPrice);
        // when user add cart then firebase updated.
        //  Gets a reference to a document.
        // Gets a reference to a collection.
        firebaseFirestore.collection("AddCart")
                .document(Objects.requireNonNull(firebaseAuth.getCurrentUser())
                        .getUid())
                .collection("Users").add(map).addOnCompleteListener(task -> {

                Toast.makeText(DetailedActivity.this, "Added to a cart", Toast.LENGTH_SHORT).show();
              //   getSupportFragmentManager().beginTransaction().replace(R.id.home_container,new CartFragment()).commit();
    }).addOnFailureListener(e -> Toast.makeText(DetailedActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show());

    }
}