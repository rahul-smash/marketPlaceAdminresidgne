package com.signity.shopkeeperapp.model.Categories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCategoryResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<GetCategoryData> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<GetCategoryData> getData() {
        return data;
    }

    public void setData(List<GetCategoryData> data) {
        this.data = data;
    }

}
