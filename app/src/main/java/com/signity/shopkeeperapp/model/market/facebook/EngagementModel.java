package com.signity.shopkeeperapp.model.market.facebook;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EngagementModel implements Serializable {

    @SerializedName("comments")
    private Comments comments;

    @SerializedName("likes")
    private Likes likes;

    @SerializedName("id")
    private String id;

    private Shares shares;

    public Shares getShares() {
        return shares;
    }

    public void setShares(Shares shares) {
        this.shares = shares;
    }

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public Likes getLikes() {
        return likes;
    }

    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EngagementModel{" +
                "comments=" + comments +
                ", likes=" + likes +
                ", id='" + id + '\'' +
                ", shares=" + shares +
                '}';
    }
}