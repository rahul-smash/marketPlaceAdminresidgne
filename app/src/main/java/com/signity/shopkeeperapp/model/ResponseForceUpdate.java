package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajesh on 3/8/16.
 */
public class ResponseForceUpdate {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<ModelForceUpdate> data = new ArrayList<ModelForceUpdate>();

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ModelForceUpdate> getData() {
        return data;
    }

    public void setData(List<ModelForceUpdate> data) {
        this.data = data;
    }
}
