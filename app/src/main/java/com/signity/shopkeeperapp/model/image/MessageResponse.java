package com.signity.shopkeeperapp.model.image;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageResponse implements Serializable {

    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return
                "MessageResponse{" +
                        "url = '" + url + '\'' +
                        "}";
    }
}