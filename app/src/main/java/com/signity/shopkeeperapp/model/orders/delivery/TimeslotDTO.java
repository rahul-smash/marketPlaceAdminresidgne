package com.signity.shopkeeperapp.model.orders.delivery;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TimeslotDTO implements Serializable {

    @SerializedName("label")
    private String label;

    @SerializedName("value")
    private String value;

    @SerializedName("is_enable")
    private boolean isEnable;

    @SerializedName("inner_text")
    private String innerText;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public String getInnerText() {
        return innerText;
    }

    public void setInnerText(String innerText) {
        this.innerText = innerText;
    }

    @Override
    public String toString() {
        return
                "TimeslotDTO{" +
                        "label = '" + label + '\'' +
                        ",value = '" + value + '\'' +
                        ",is_enable = '" + isEnable + '\'' +
                        ",inner_text = '" + innerText + '\'' +
                        "}";
    }
}