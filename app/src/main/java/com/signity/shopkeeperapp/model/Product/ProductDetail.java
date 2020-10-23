package com.signity.shopkeeperapp.model.Product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetail extends GetProductData {

    @Expose
    @SerializedName("category_parent_id")
    private String categoryParentId;

    @Expose
    @SerializedName("images")
    private List<ImageObject> imageList;

    public String getCategoryParentId() {
        return categoryParentId;
    }

    public void setCategoryParentId(String categoryParentId) {
        this.categoryParentId = categoryParentId;
    }

    public List<ImageObject> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageObject> imageList) {
        this.imageList = imageList;
    }
}
