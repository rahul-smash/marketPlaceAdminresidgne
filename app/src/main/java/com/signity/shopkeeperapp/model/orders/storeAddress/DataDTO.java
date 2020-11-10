package com.signity.shopkeeperapp.model.orders.storeAddress;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DataDTO implements Serializable {

    @SerializedName("City")
    private CityDTO city;

    @SerializedName("Area")
    private List<AreaDTO> area;

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public List<AreaDTO> getArea() {
        return area;
    }

    public void setArea(List<AreaDTO> area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return
                "DataDTO{" +
                        "city = '" + city + '\'' +
                        ",area = '" + area + '\'' +
                        "}";
    }
}