package com.signity.shopkeeperapp.model.orders.storeAddress;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CityDTO implements Serializable {

    @SerializedName("city")
    private String city;

    @SerializedName("id")
    private String id;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return
                "CityDTO{" +
                        "city = '" + city + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}