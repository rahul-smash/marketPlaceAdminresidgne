package com.signity.shopkeeperapp.model.orders.delivery;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DateTimeCollectionDTO implements Serializable {

    @SerializedName("label")
    private String label;

    @SerializedName("timeslot")
    private List<TimeslotDTO> timeslot;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TimeslotDTO> getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(List<TimeslotDTO> timeslot) {
        this.timeslot = timeslot;
    }

    @Override
    public String toString() {
        return
                "DateTimeCollectionDTO{" +
                        "label = '" + label + '\'' +
                        ",timeslot = '" + timeslot + '\'' +
                        "}";
    }
}