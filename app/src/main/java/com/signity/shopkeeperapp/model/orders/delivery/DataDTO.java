package com.signity.shopkeeperapp.model.orders.delivery;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DataDTO implements Serializable {

    @SerializedName("is24x7_open")
    private String is24x7Open;

    @SerializedName("date_time_collection")
    private List<DateTimeCollectionDTO> dateTimeCollection;

    public String getIs24x7Open() {
        return is24x7Open;
    }

    public void setIs24x7Open(String is24x7Open) {
        this.is24x7Open = is24x7Open;
    }

    public List<DateTimeCollectionDTO> getDateTimeCollection() {
        return dateTimeCollection;
    }

    public void setDateTimeCollection(List<DateTimeCollectionDTO> dateTimeCollection) {
        this.dateTimeCollection = dateTimeCollection;
    }

    @Override
    public String toString() {
        return
                "DataDTO{" +
                        "is24x7_open = '" + is24x7Open + '\'' +
                        ",date_time_collection = '" + dateTimeCollection + '\'' +
                        "}";
    }
}