package com.signity.shopkeeperapp.model.helpMediaModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HelpVideoResponse {

    @Expose
    @SerializedName("success")
    private boolean success;

    @Expose
    @SerializedName("data")
    private List<HelpVideos> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<HelpVideos> getData() {
        return data;
    }

    public void setData(List<HelpVideos> data) {
        this.data = data;
    }
}
