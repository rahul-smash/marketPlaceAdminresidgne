package com.signity.shopkeeperapp.model.stores;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.signity.shopkeeperapp.model.verify.StoreResponse;

import java.util.List;

public class StoresResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<StoreResponse> storesList;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<StoreResponse> getStoresList() {
        return storesList;
    }

    public void setStoresList(List<StoreResponse> storesList) {
        this.storesList = storesList;
    }
}
