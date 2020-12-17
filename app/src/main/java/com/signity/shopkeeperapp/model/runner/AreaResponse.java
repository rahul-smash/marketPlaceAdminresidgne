package com.signity.shopkeeperapp.model.runner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AreaResponse implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("zone")
    private String zone;

    @SerializedName("country_id")
    private String countryId;

    @SerializedName("city_id")
    private String cityId;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
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

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return
                "AreaResponse{" +
                        "id = '" + id + '\'' +
                        ",name = '" + name + '\'' +
                        ",zone = '" + zone + '\'' +
                        ",country_id = '" + countryId + '\'' +
                        "}";
    }
}