package com.deal.exap.model;

import java.io.Serializable;

public class MenuDTO  implements Serializable{

    private String alert;
    private String nearby;
    private String wallet;
    private String favorite;
    private String following;

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getNearby() {
        return nearby;
    }

    public void setNearby(String nearby) {
        this.nearby = nearby;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }
}
