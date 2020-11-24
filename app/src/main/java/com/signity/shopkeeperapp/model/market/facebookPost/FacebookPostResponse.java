package com.signity.shopkeeperapp.model.market.facebookPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacebookPostResponse {

    @Expose
    private String id;

    @Expose
    @SerializedName("post_id")
    private String postId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
