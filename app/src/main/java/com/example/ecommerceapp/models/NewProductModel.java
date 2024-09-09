package com.example.ecommerceapp.models;

import java.io.Serializable;

public class NewProductModel implements Serializable {

    String name,description,rating,img_url,type;
    int price,id;

    public NewProductModel(){
    }
// this constructor is mainly used for when we adding data.
    public NewProductModel(String name,String type,int id, String description, String rating, String img_url , int price){
        this.img_url=img_url;
        this.description=description;
        this.name=name;
        this.id =id;
        this.type=type;
        this.price=price;
        this.rating=rating;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setPrice(int price) {
        this.price = price;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getImg_url() {
        return img_url;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getRating() {
        return rating;
    }
}
