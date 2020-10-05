package com.signity.shopkeeperapp.model.productStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductStatus {
    @SerializedName("success")
    @Expose
    public Boolean success;

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

    @SerializedName("message")
    @Expose
    public String message;
}

