package com.signity.shopkeeperapp.model.category;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AddCategoryModel implements Serializable {

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String image;

    @SerializedName("subCate")
    private List<SubCategoryModel> subCate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<SubCategoryModel> getSubCate() {
        return subCate;
    }

    public void setSubCate(List<SubCategoryModel> subCate) {
        this.subCate = subCate;
    }

    @Override
    public String toString() {
        return
                "AddCategoryModel{" +
                        "title = '" + title + '\'' +
                        ",image = '" + image + '\'' +
                        ",subCate = '" + subCate + '\'' +
                        "}";
    }
}