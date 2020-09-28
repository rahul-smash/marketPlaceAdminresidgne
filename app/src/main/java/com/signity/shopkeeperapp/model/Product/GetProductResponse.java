package com.signity.shopkeeperapp.model.Product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProductResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("data")
    @Expose
    private List<GetProductData> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<GetProductData> getData() {
        return data;
    }

    public void setData(List<GetProductData> data) {
        this.data = data;
    }

}
