package com.signity.shopkeeperapp.model.orders.offers;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApplyCouponDTO implements Serializable {

    @SerializedName("data")
    private DataDTO data;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("DiscountAmount")
    private String discountAmount;

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public String toString() {
        return
                "ApplyCouponDTO{" +
                        "data = '" + data + '\'' +
                        ",success = '" + success + '\'' +
                        ",message = '" + message + '\'' +
                        ",discountAmount = '" + discountAmount + '\'' +
                        "}";
    }
}