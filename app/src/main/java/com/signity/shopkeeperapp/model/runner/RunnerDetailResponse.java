package com.signity.shopkeeperapp.model.runner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RunnerDetailResponse implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private List<DataResponse> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DataResponse> getData() {
        return data;
    }

    public void setData(List<DataResponse> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return
                "RunnerDetailResponse{" +
                        "success = '" + success + '\'' +
                        ",data = '" + data + '\'' +
                        "}";
    }
}