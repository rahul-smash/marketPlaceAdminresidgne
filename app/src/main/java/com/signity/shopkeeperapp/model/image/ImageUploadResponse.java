package com.signity.shopkeeperapp.model.image;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageUploadResponse implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private MessageResponse message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public MessageResponse getMessage() {
        return message;
    }

    public void setMessage(MessageResponse message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return
                "ImageUploadResponse{" +
                        "success = '" + success + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}