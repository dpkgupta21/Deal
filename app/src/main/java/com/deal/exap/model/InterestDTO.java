package com.deal.exap.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Mayur on 02-01-2016.
 */
public class InterestDTO implements Serializable{


    @DatabaseField
    public String id;
    @DatabaseField
    public String name;

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
}
