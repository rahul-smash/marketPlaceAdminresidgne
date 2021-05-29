package com.signity.shopkeeperapp.model.storeStatus;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataResponse implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("store_id")
    private String storeId;

    @SerializedName("store_status")
    private String storeStatus;

    @SerializedName("store_msg")
    private String storeMsg;

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

    public String getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(String storeStatus) {
        this.storeStatus = storeStatus;
    }

    public String getStoreMsg() {
        return storeMsg;
    }

    public void setStoreMsg(String storeMsg) {
        this.storeMsg = storeMsg;
    }

    @Override
    public String toString() {
        return
                "DataResponse{" +
                        "id = '" + id + '\'' +
                        ",store_id = '" + storeId + '\'' +
                        ",store_status = '" + storeStatus + '\'' +
                        ",store_msg = '" + storeMsg + '\'' +
                        "}";
    }
}