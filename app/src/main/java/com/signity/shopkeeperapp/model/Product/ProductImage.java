package com.signity.shopkeeperapp.model.Product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductImage {

    @Expose
    @SerializedName("image")
    private String image;

    public ProductImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
