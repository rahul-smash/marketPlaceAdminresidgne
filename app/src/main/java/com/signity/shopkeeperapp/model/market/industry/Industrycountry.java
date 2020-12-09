package com.signity.shopkeeperapp.model.market.industry;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Industrycountry implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("phonecode")
    private String phonecode;

    @SerializedName("currency")
    private String currency;

    @SerializedName("currency_code")
    private String currencyCode;

    @SerializedName("status")
    private boolean status;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("alpha_code")
    private String alphaCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAlphaCode() {
        return alphaCode;
    }

    public void setAlphaCode(String alphaCode) {
        this.alphaCode = alphaCode;
    }

    @Override
    public String toString() {
        return
                "Industrycountry{" +
                        "id = '" + id + '\'' +
                        ",name = '" + name + '\'' +
                        ",phonecode = '" + phonecode + '\'' +
                        ",currency = '" + currency + '\'' +
                        ",currency_code = '" + currencyCode + '\'' +
                        ",status = '" + status + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",updated_at = '" + updatedAt + '\'' +
                        ",alpha_code = '" + alphaCode + '\'' +
                        "}";
    }
}