package com.example.ecommerceapp.models;

import java.io.Serializable;

public class CartFragmentModel {

    String currentTime,currentDate,productName,productPrice, TotalQunatity;
    int TotalPrice;
     private  String documentId;

    public String getCurrentTime() {
        return currentTime;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getTotalQunatity() {
        return TotalQunatity;
    }

    public void setTotalQunatity(String TotalQunatity) {
        this.TotalQunatity = TotalQunatity;
    }

    public int getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(int TotalPrice) {
        this.TotalPrice = TotalPrice;
    }

    public CartFragmentModel(String currentTime, String currentDate, String productName, String productPrice, String TotalQuantity, int TotalPrice) {
        this.currentTime = currentTime;
        this.currentDate = currentDate;
        this.productName = productName;
        this.productPrice = productPrice;
        this.TotalQunatity = TotalQuantity;
        this.TotalPrice = TotalPrice;
    }
    public  CartFragmentModel(){
    }

}
