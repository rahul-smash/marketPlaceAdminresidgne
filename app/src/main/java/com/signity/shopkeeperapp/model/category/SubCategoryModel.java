package com.signity.shopkeeperapp.model.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategoryModel {

    @Expose
    @SerializedName("sub_category_name")
    private String subCategoryName;

    @Expose
    @SerializedName("sub_category_image")
    private String subCategoryImage;

    public String getCategoryName() {
        return subCategoryName;
    }

    public void setCategoryName(String categoryName) {
        this.subCategoryName = categoryName;
    }

    public String getCategoryImage() {
        return subCategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.subCategoryImage = categoryImage;
    }
}
