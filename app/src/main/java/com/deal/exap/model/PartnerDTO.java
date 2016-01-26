package com.deal.exap.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PartnerDTO implements Serializable {

    private String id;
    private String name;
    private String name_ara;
    private String image;
    private String logo;
    private String address_eng;
    private String address_ara;
    private String location;
    private String user_follow;
    private String is_follow;
    private String is_featured;
    private String is_chat_on;
    private String active_coupan;
    private String download;
    private String follower;

    private ArrayList<DealDTO> deals;

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

    public String getUser_follow() {
        return user_follow;
    }

    public void setUser_follow(String user_follow) {
        this.user_follow = user_follow;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getIs_featured() {
        return is_featured;
    }

    public void setIs_featured(String is_featured) {
        this.is_featured = is_featured;
    }

    public String getIs_chat_on() {
        return is_chat_on;
    }

    public void setIs_chat_on(String is_chat_on) {
        this.is_chat_on = is_chat_on;
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

    public ArrayList<DealDTO> getDeals() {
        return deals;
    }

    public void setDeals(ArrayList<DealDTO> deals) {
        this.deals = deals;
    }

    public String getName_ara() {
        return name_ara;
    }

    public void setName_ara(String name_ara) {
        this.name_ara = name_ara;
    }
}
