package com.signity.shopkeeperapp.model.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategoryModel {

    @Expose
    @SerializedName("title")
    private String subCategoryName;

    @Expose
    @SerializedName("image")
    private String subCategoryImage;

    @Expose(deserialize = false)
    private String subCategoryImageUrl;

    public String getSubCategoryImageUrl() {
        return subCategoryImageUrl;
    }

    public void setSubCategoryImageUrl(String subCategoryImageUrl) {
        this.subCategoryImageUrl = subCategoryImageUrl;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getSubCategoryImage() {
        return subCategoryImage;
    }

    public void setSubCategoryImage(String subCategoryImage) {
        this.subCategoryImage = subCategoryImage;
    }

    @Override
    public String toString() {
        return "SubCategoryModel{" +
                "subCategoryName='" + subCategoryName + '\'' +
                ", subCategoryImage='" + subCategoryImage + '\'' +
                ", subCategoryImageUrl='" + subCategoryImageUrl + '\'' +
                '}';
    }
}
