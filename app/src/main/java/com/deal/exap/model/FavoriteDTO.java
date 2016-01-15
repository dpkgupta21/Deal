package com.deal.exap.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class FavoriteDTO implements Serializable{

    @DatabaseField
    private String id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String image;
    @DatabaseField
    private String deal_count;

    @DatabaseField
    private String icon_image;
    @DatabaseField
    private String favourite;
    @DatabaseField
    private String category_favourite_count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDeal_count() {
        return deal_count;
    }

    public void setDeal_count(String deal_count) {
        this.deal_count = deal_count;
    }

    public String getIcon_image() {
        return icon_image;
    }

    public void setIcon_image(String icon_image) {
        this.icon_image = icon_image;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public String getCategory_favourite_count() {
        return category_favourite_count;
    }

    public void setCategory_favourite_count(String category_favourite_count) {
        this.category_favourite_count = category_favourite_count;
    }
}
