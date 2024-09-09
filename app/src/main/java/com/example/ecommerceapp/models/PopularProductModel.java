package com.example.ecommerceapp.models;

import android.content.Context;

import com.example.ecommerceapp.adapters.PopularProductAdapter;

import java.io.Serializable;

public class PopularProductModel implements Serializable {
    String name,description,rating,img_url,type;
    int price,id;

    public PopularProductModel(){
    }

    public PopularProductModel(String name,String type,int id,String description,String img_url,String rating, int price){
        this.img_url=img_url;
        this.name=name;
        this.id=id;
        this.type=type;
        this.description=description;
        this.rating=rating;
        this.price=price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getName() {
        return name;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setName(String name) {
        this.name = name;
    }

}

