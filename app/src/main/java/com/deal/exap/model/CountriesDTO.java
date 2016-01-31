package com.deal.exap.model;

import java.io.Serializable;

public class CountriesDTO implements Serializable {

    private String id;
    private String name;
    private String name_ara;

    public String getName_ara() {
        return name_ara;
    }

    public void setName_ara(String name_ara) {
        this.name_ara = name_ara;
    }

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
