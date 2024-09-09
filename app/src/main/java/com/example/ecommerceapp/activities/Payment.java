package com.example.ecommerceapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class Payment extends AppCompatActivity implements PaymentResultListener {

    Toolbar toolbar;
    TextView subTotal,discount,shipping, total;
    Button pay_now;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    int totalPrice =0;
    int totalQuantity=1;
    int totalAmount=0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        toolbar=findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        subTotal=findViewById(R.id.text1);
        discount=findViewById(R.id.text2);
        shipping=findViewById(R.id.text3);
        total=findViewById(R.id.text4);
        pay_now=findViewById(R.id.checkOut);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore =FirebaseFirestore.getInstance();

        // Getting data or intent from address activity .
        totalPrice =getIntent().getIntExtra("totalPrice",0);
        totalQuantity = getIntent().getIntExtra("totalQuantity",1);
        totalAmount =getIntent().getIntExtra("totalAmount",0);

//if (totalPrice!=0){
//    subTotal.setText("$"+totalPrice);
//} else if (totalAmount!=0) {
//    subTotal.setText("$"+totalAmount);
//}else {
//    subTotal.setText("$0");
//}
        int displayAmount = totalPrice != 0 ? totalPrice : totalAmount;
        subTotal.setText("$" + displayAmount);



        //  total.setText("$"+totalAmount);

        pay_now.setOnClickListener(v -> paymentMethod());

    }

    private void paymentMethod() {
        //we are using JSON Object option .not a payloadHelper.
        // Create an instance of the Checkout and pass the payment details and options as a JSONObject.
        //   * Instantiate Checkout
        Checkout checkout =new Checkout();

        //   * Reference to current activity
        final Activity activity =this;
        try{
            JSONObject option= new JSONObject();

            //Set Company name
            option.put("name","E-commerceApp");
            //Ref no
            option.put("description","Reference No.#12454");
            //Image to be display.
            option.put("image","https://uxwing.com/wp-content/themes/uxwing/download/brands-and-social-media/razorpay-icon.png");
            //currency type
            option.put("currency","INR");

            option.put("theme.color", "#3399cc");

            option.put("prefill.email", "gaurav.kumar@example.com");
            option.put("prefill.contact","9988776655");

            // Multiply with 100 to get extract amount in rupee.
            totalPrice =totalPrice *100;
            option.put("totalPrice", totalPrice);//pass amount in currency subunits
            JSONObject retryObj = new JSONObject();

            // email
            retryObj.put("email", "ketangarg@example.com");
            retryObj.put("contact","9877234533");
            option.put("retryObj",retryObj);
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
           // open Razor pay checkout activity
            checkout.open(activity,option);
        }catch (Exception e){
            Log.e("TAG","Error in starting checkout",e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Payment Id");
        builder.setMessage(s);
        builder.show();
        conformOrderFunc();
        Toast.makeText(Payment.this, "Payment Successful", Toast.LENGTH_SHORT).show();
    }

    private void conformOrderFunc() {
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
         Intent intent= getIntent();
        if (intent!=null){
            map.put("name",intent.getStringExtra("name"));
            map.put("phone",intent.getStringExtra("phone"));
            map.put("currentTime",saveCurrentTime);
            map.put("currentDate",saveCurrentDate);
            map.put("address",intent.getStringExtra("address"));
            map.put("city",intent.getStringExtra("city"));
//            if (totalPrice!=0){
//                map.put("totalPrice",totalPrice);
//            } else if (totalAmount!=0) {
//                map.put("totalAmount",totalAmount);
//            }else {
//                subTotal.setText("$0");
//            }
            map.put("totalAmount", totalPrice != 0 ? totalPrice : totalAmount);

        }else {
            Toast.makeText(this, "data is null", Toast.LENGTH_SHORT).show();
        }

        // when user add cart then firebase updated.
        //  Gets a reference to a document.
        // Gets a reference to a collection.
        firebaseFirestore.collection("Order")
                .document(Objects.requireNonNull(firebaseAuth.getCurrentUser())
                        .getUid())
                .collection("History").add(map).addOnCompleteListener(task -> {
                    Intent intent1=new Intent(Payment.this, OrderHistory.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    Toast.makeText(Payment.this, "your Order has been placed sucessfully", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> Toast.makeText(Payment.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(Payment.this, "Payment Cancel", Toast.LENGTH_SHORT).show();
    }
}