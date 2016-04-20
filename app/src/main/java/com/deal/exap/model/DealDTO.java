package com.deal.exap.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mayur on 09-01-2016.
 */
public class DealDTO implements Serializable {


    @DatabaseField
    private String id;
    @DatabaseField
    private String name_eng;
    @DatabaseField
    private String name_ara;
    @DatabaseField
    private String detail_eng;
    @DatabaseField
    private String detail_ara;
    @DatabaseField
    private String term_eng;
    @DatabaseField
    private String term_ara;
    @DatabaseField
    private String discount;
    @DatabaseField
    private String visible_price;
    @DatabaseField
    private String final_price;
    @DatabaseField
    private String type;
    @DatabaseField
    private String deal_image;
    @DatabaseField
    private String end_date;
    @DatabaseField
    private String partner_logo;

    @DatabaseField
    private int partner_id;
    @DatabaseField
    private int category_id;

    @DatabaseField
    private String redeem_option;

    @DatabaseField
    private String location;

    @DatabaseField
    private String deal_code;
    @DatabaseField
    private int redeemed;
    @DatabaseField
    private String distance;
    @DatabaseField
    private int rating;
    @DatabaseField
    private int review;
    @DatabaseField
    private int total_codes;

    private double lat;

    private double lng;

    private String is_chat_on;

    private List<String> deal_images;

    private String website;



    public String getIs_chat_on() {
        return is_chat_on;
    }

    public void setIs_chat_on(String is_chat_on) {
        this.is_chat_on = is_chat_on;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public List<String>  getDeal_images() {
        return deal_images;
    }

    public void setDeal_images(List<String>  deal_images) {
        this.deal_images = deal_images;
    }

    public String getName_eng() {
        return name_eng;
    }

    public void setName_eng(String name_eng) {
        this.name_eng = name_eng;
    }

    public String getName_ara() {
        return name_ara;
    }

    public void setName_ara(String name_ara) {
        this.name_ara = name_ara;
    }

    public String getDetail_eng() {
        return detail_eng;
    }

    public void setDetail_eng(String detail_eng) {
        this.detail_eng = detail_eng;
    }

    public String getDetail_ara() {
        return detail_ara;
    }

    public void setDetail_ara(String detail_ara) {
        this.detail_ara = detail_ara;
    }

    public String getTerm_eng() {
        return term_eng;
    }

    public void setTerm_eng(String term_eng) {
        this.term_eng = term_eng;
    }

    public String getTerm_ara() {
        return term_ara;
    }

    public void setTerm_ara(String term_ara) {
        this.term_ara = term_ara;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getVisible_price() {
        return visible_price;
    }

    public void setVisible_price(String visible_price) {
        this.visible_price = visible_price;
    }

    public String getFinal_price() {
        return final_price;
    }

    public void setFinal_price(String final_price) {
        this.final_price = final_price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeal_image() {
        return deal_image;
    }

    public void setDeal_image(String deal_image) {
        this.deal_image = deal_image;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPartner_logo() {
        return partner_logo;
    }

    public void setPartner_logo(String partner_logo) {
        this.partner_logo = partner_logo;
    }

    public String getRedeem_option() {
        return redeem_option;
    }

    public void setRedeem_option(String redeem_option) {
        this.redeem_option = redeem_option;
    }


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(int redeemed) {
        this.redeemed = redeemed;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(int partner_id) {
        this.partner_id = partner_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTotal_codes() {
        return total_codes;
    }

    public void setTotal_codes(int total_codes) {
        this.total_codes = total_codes;
    }

    public String getDeal_code() {
        return deal_code;
    }

    public void setDeal_code(String deal_code) {
        this.deal_code = deal_code;
    }


    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}



