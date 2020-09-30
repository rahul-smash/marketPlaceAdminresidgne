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
    private List<GetCategoryData> data;

    @SerializedName("category_total")
    private int total;

    @SerializedName("page")
    private int page;

    @SerializedName("pagelength")
    private int pagelength;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagelength() {
        return pagelength;
    }

    public void setPagelength(int pagelength) {
        this.pagelength = pagelength;
    }

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
