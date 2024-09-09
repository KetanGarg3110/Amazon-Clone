package com.example.ecommerceapp.models;

import java.io.Serializable;

public class DetailedRelatedModel implements Serializable {
    String img_url,name ,description,rating ,type;
    int price ,id;

    public DetailedRelatedModel(String img_url, String name, String description, String rating, String type, int price, int id) {
        this.img_url = img_url;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.type = type;
        this.price = price;
        this.id = id;
    }

    public DetailedRelatedModel() {
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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
}
