package com.signity.shopkeeperapp.model.helpMediaModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpVideos {

    @SerializedName("created_at")
    private String createdAt;
    @Expose
    private long id;
    @Expose
    private String title;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("video_thumbnail")
    private String videoThumbnail;
    @SerializedName("video_image")
    private String videoImage;
    @SerializedName("video_url")
    private String videoUrl;

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

}
