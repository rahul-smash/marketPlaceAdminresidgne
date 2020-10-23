package com.signity.shopkeeperapp.model.Product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageObject implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("image_100_80")
    private String image10080;

    @SerializedName("image_300_200")
    private String image300200;

    @SerializedName("image")
    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage10080() {
        return image10080;
    }

    public void setImage10080(String image10080) {
        this.image10080 = image10080;
    }

    public String getImage300200() {
        return image300200;
    }

    public void setImage300200(String image300200) {
        this.image300200 = image300200;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return
                "ImageObject{" +
                        "id = '" + id + '\'' +
                        ",image_100_80 = '" + image10080 + '\'' +
                        ",image_300_200 = '" + image300200 + '\'' +
                        ",image = '" + image + '\'' +
                        "}";
    }
}