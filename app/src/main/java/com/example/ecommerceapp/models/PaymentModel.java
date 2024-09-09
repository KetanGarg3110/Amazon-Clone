package com.example.ecommerceapp.models;

import java.io.Serializable;

public class PaymentModel implements Serializable {
    private String name,address ,city,phoneNo,currentTime,currentDate;
    private  int TotalPrice;

    public PaymentModel(String name, String address, String city, String phoneNo, String currentTime, String currentDate, int totalPrice) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.phoneNo = phoneNo;
        this.currentTime = currentTime;
        this.currentDate = currentDate;
        TotalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCurrentTime() {
        return currentTime;
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

    public int getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        TotalPrice = totalPrice;
    }
}
