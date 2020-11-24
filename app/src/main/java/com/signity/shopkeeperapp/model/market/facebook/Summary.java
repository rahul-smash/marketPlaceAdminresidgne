package com.signity.shopkeeperapp.model.market.facebook;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Summary implements Serializable {

    @SerializedName("total_count")
    private int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return
                "Summary{" +
                        "total_count = '" + totalCount + '\'' +
                        "}";
    }
}