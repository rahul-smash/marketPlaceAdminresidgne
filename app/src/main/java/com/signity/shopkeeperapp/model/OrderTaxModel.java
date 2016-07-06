package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rajesh on 30/6/16.
 */
public class OrderTaxModel implements Serializable {


    @SerializedName("label")
    private String label;
    @SerializedName("rate")
    private String rate;
    @SerializedName("tax")
    private String tax;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }
}
