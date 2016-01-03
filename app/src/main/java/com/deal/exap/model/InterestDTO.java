package com.deal.exap.model;

import java.io.Serializable;

/**
 * Created by Mayur on 02-01-2016.
 */
public class InterestDTO implements Serializable{

    public String id;
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
