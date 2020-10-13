package com.signity.shopkeeperapp.model.image;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageResponse implements Serializable {

    @SerializedName("url")
    private String url;

    @SerializedName("image_url")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

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