package com.deal.exap.model;

import java.io.Serializable;

/**
 * Created by Mayur on 09-01-2016.
 */
public class DealDTO implements Serializable{

    private String name_eng;
    private String name_ara;
    private String detail_eng;
    private String detail_ara;
    private String term_eng;
    private String term_ara;
    private String discount;
    private String visible_price;
    private String final_price;
    private String type;
    private String deal_image;
    private String end_date;
    private String partner_logo;
    private String redeem_option;
    private int redeemed;
    private String distance;
    private int rating;
    private int review;

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
}