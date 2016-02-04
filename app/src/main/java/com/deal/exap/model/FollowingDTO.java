package com.deal.exap.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class FollowingDTO implements Serializable {

    @DatabaseField
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String name_ara;
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

    private int is_follow;

    private int is_featured;

    public String getName_ara() {
        return name_ara;
    }

    public void setName_ara(String name_ara) {
        this.name_ara = name_ara;
    }


    public int getIs_featured() {
        return is_featured;
    }

    public void setIs_featured(int is_featured) {
        this.is_featured = is_featured;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
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



