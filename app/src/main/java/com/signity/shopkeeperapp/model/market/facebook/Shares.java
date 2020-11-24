package com.signity.shopkeeperapp.model.market.facebook;

import com.google.gson.annotations.SerializedName;

public class Shares {

    @SerializedName("count")
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
