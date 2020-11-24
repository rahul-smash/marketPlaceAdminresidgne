package com.signity.shopkeeperapp.model.market.facebook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SharedFacebookRequest {

    @Expose
    private String brand;
    @Expose
    private String creative;
    @Expose
    @SerializedName("creative_type")
    private String creativeType;
    @Expose
    @SerializedName("share_medium_type")
    private String shareMediumType;
    @Expose
    @SerializedName("post_id")
    private String postId;
    @Expose
    @SerializedName("tag_id")
    private int tagId;
    @Expose
    @SerializedName("store")
    private long storeId;

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getShareMediumType() {
        return shareMediumType;
    }

    public void setShareMediumType(String shareMediumType) {
        this.shareMediumType = shareMediumType;
    }

    public String getCreativeType() {
        return creativeType;
    }

    public void setCreativeType(String creativeType) {
        this.creativeType = creativeType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCreative() {
        return creative;
    }

    public void setCreative(String creative) {
        this.creative = creative;
    }

}
