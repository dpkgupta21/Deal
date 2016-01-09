package com.deal.exap.model;

import java.io.Serializable;

/**
 * Created by Mayur on 09-01-2016.
 */
public class CategoryDTO implements Serializable{

    private String name;
    private String image;
    private String deal_count;

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
}
