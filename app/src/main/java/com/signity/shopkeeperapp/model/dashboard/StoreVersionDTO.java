package com.signity.shopkeeperapp.model.dashboard;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoreVersionDTO implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private MessageDTO message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public MessageDTO getMessage() {
        return message;
    }

    public void setMessage(MessageDTO message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return
                "StoreVersionDTO{" +
                        "success = '" + success + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}