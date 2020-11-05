package com.signity.shopkeeperapp.model.orders.loyalty;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataResponse implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("store_id")
    private String storeId;

    @SerializedName("amount")
    private String amount;

    @SerializedName("points")
    private String points;

    @SerializedName("coupon_code")
    private String couponCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    @Override
    public String toString() {
        return
                "DataResponse{" +
                        "id = '" + id + '\'' +
                        ",store_id = '" + storeId + '\'' +
                        ",amount = '" + amount + '\'' +
                        ",points = '" + points + '\'' +
                        ",coupon_code = '" + couponCode + '\'' +
                        "}";
    }
}