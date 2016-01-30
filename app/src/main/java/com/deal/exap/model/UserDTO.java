package com.deal.exap.model;

import java.io.Serializable;

/**
 * Created by Mayur on 02-01-2016.
 */
public class UserDTO implements Serializable{

    public String id;
    public String language_id;
    public String email;
    public String mobile;
    public String gender;
    public String image;
    public String currency;
    public String country_id;
    public String dob;
    public boolean is_location_service;
    public boolean is_push_alert;
    public boolean is_message_alert;
    public boolean is_deal_expiry_alert;
    public String name;
    public CountryDTO Country;
    public LanguageDTO Language;
    public String userType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public boolean is_location_service() {
        return is_location_service;
    }

    public void setIs_location_service(boolean is_location_service) {
        this.is_location_service = is_location_service;
    }

    public boolean is_push_alert() {
        return is_push_alert;
    }

    public void setIs_push_alert(boolean is_push_alert) {
        this.is_push_alert = is_push_alert;
    }

    public boolean is_message_alert() {
        return is_message_alert;
    }

    public void setIs_message_alert(boolean is_message_alert) {
        this.is_message_alert = is_message_alert;
    }

    public boolean is_deal_expiry_alert() {
        return is_deal_expiry_alert;
    }

    public void setIs_deal_expiry_alert(boolean is_deal_expiry_alert) {
        this.is_deal_expiry_alert = is_deal_expiry_alert;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CountryDTO getCountry() {
        return Country;
    }

    public void setCountry(CountryDTO country) {
        Country = country;
    }

    public LanguageDTO getLanguage() {
        return Language;
    }

    public void setLanguage(LanguageDTO language) {
        Language = language;
    }


    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
