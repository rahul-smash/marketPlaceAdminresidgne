package com.signity.shopkeeperapp.model.market.industry;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IndustryRegistration implements Serializable {

    @SerializedName("status")
    private boolean status;

    @SerializedName("data")
    private Data data;

    @SerializedName("is_newuser")
    private boolean isNewuser;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean isIsNewuser() {
        return isNewuser;
    }

    public void setIsNewuser(boolean isNewuser) {
        this.isNewuser = isNewuser;
    }

    @Override
    public String toString() {
        return
                "IndustryRegistration{" +
                        "status = '" + status + '\'' +
                        ",data = '" + data + '\'' +
                        ",is_newuser = '" + isNewuser + '\'' +
                        "}";
    }
}