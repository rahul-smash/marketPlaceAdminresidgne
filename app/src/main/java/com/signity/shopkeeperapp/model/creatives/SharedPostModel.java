package com.signity.shopkeeperapp.model.creatives;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SharedPostModel {

    @Expose
    private boolean status;

    @Expose
    @SerializedName("_start")
    private int start;

    @Expose
    @SerializedName("_limit")
    private int limit;

    @Expose
    @SerializedName("totalSharedCretive")
    private int totalSharedCreative;

    @Expose
    private List<Creative> creatives;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotalSharedCreative() {
        return totalSharedCreative;
    }

    public void setTotalSharedCreative(int totalSharedCreative) {
        this.totalSharedCreative = totalSharedCreative;
    }

    public List<Creative> getCreatives() {
        return creatives;
    }

    public void setCreatives(List<Creative> creatives) {
        this.creatives = creatives;
    }
}
