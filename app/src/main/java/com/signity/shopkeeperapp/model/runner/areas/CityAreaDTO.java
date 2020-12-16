package com.signity.shopkeeperapp.model.runner.areas;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CityAreaDTO implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private List<DataDTO> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return
                "CityAreaDTO{" +
                        "success = '" + success + '\'' +
                        ",data = '" + data + '\'' +
                        "}";
    }
}