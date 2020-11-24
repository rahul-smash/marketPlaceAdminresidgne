package com.signity.shopkeeperapp.model.market.facebook;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Comments implements Serializable {

    @SerializedName("data")
    private List<Object> data;

    @SerializedName("summary")
    private Summary summary;

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return
                "Comments{" +
                        "data = '" + data + '\'' +
                        ",summary = '" + summary + '\'' +
                        "}";
    }
}