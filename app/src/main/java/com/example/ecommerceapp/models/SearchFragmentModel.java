package com.example.ecommerceapp.models;

import java.io.Serializable;

public class SearchFragmentModel implements Serializable {

    String name, description,date, time ,img_url,category,rating ,type;
    int price ,id ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public void setRating(String rating) {
        this.rating = rating;
    }

    public SearchFragmentModel( String name, int price, String description, String date, String time, String img_url, String category,String rating,String type,int id) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.date = date;
        this.time = time;
        this.img_url = img_url;
        this.category = category;
        this.rating=rating;
        this.type=type;
        this.id=id;
    }
   public SearchFragmentModel(){}



}
