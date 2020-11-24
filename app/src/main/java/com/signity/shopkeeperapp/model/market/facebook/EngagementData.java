package com.signity.shopkeeperapp.model.market.facebook;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EngagementData implements Serializable {

    @SerializedName("creative_id")
    private int creativeId;

    @SerializedName("brand_id")
    private long brandId;

    @SerializedName("post_id")
    private String postId;

    @SerializedName("total_like")
    private int totalLike;

    @SerializedName("total_share")
    private int totalShare;

    @SerializedName("total_comment")
    private int totalComment;

    @SerializedName("store_id")
    private long storeId;

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public int getCreativeId() {
        return creativeId;
    }

    public void setCreativeId(int creativeId) {
        this.creativeId = creativeId;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public int getTotalShare() {
        return totalShare;
    }

    public void setTotalShare(int totalShare) {
        this.totalShare = totalShare;
    }

    public int getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(int totalComment) {
        this.totalComment = totalComment;
    }

    @Override
    public String toString() {
        return
                "EngagementData{" +
                        "creative_id = '" + creativeId + '\'' +
                        ",brand_id = '" + brandId + '\'' +
                        ",post_id = '" + postId + '\'' +
                        ",total_like = '" + totalLike + '\'' +
                        ",total_share = '" + totalShare + '\'' +
                        ",total_comment = '" + totalComment + '\'' +
                        "}";
    }
}