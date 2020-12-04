package com.signity.shopkeeperapp.model.runner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RunnersResponseDTO implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private List<RunnerDetail> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<RunnerDetail> getData() {
        return data;
    }

    public void setData(List<RunnerDetail> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return
                "RunnersResponseDTO{" +
                        "success = '" + success + '\'' +
                        ",data = '" + data + '\'' +
                        "}";
    }
}