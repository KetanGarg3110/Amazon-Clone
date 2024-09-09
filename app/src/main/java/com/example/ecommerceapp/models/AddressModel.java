package com.example.ecommerceapp.models;

import java.io.Serializable;

public class AddressModel implements Serializable {
    String name,address,city,postalCode,phoneNo;
    // we are taking boolean datatype because of we use radiobutton
    boolean isSelected;
    private  String documentId;


    public AddressModel() {
    }

    public AddressModel(String name,String documentId, String address, String city, String postalCode, String phoneNo, boolean isSelected) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.documentId=documentId;
        this.postalCode = postalCode;
        this.phoneNo = phoneNo;
        this.isSelected = isSelected;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
