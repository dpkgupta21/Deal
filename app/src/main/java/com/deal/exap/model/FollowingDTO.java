package com.deal.exap.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class FollowingDTO implements Serializable {

    @DatabaseField
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String image;
    @DatabaseField
    private String logo;

    @DatabaseField
    private String address_eng;
    @DatabaseField
    private String address_ara;
    @DatabaseField
    private String location;

    @DatabaseField
    private String active_coupan;
    @DatabaseField
    private String download;
    @DatabaseField
    private String follower;


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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAddress_eng() {
        return address_eng;
    }

    public void setAddress_eng(String address_eng) {
        this.address_eng = address_eng;
    }

    public String getAddress_ara() {
        return address_ara;
    }

    public void setAddress_ara(String address_ara) {
        this.address_ara = address_ara;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getActive_coupan() {
        return active_coupan;
    }

    public void setActive_coupan(String active_coupan) {
        this.active_coupan = active_coupan;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }
}



