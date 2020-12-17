package com.signity.shopkeeperapp.model.faq;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FaqResponse {

    @Expose
    @SerializedName("success")
    private boolean success;

    @Expose
    @SerializedName("data")
    private List<FaqModel> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<FaqModel> getData() {
        return data;
    }

    public void setData(List<FaqModel> data) {
        this.data = data;
    }
}
