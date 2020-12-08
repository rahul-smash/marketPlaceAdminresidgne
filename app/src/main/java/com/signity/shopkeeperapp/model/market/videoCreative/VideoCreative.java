package com.signity.shopkeeperapp.model.market.videoCreative;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VideoCreative {

    @Expose
    private String description;
    @Expose
    private long id;
    @SerializedName("is_shared")
    private Boolean isShared;
    @Expose
    private String status;
    @SerializedName("Title")
    private String title;
    @Expose
    private String video;
    @SerializedName("video_thumb")
    private String videoThumb;
    @SerializedName("video_thumb_small")
    private String videoThumbSmall;

    public String getVideoThumb() {
        return videoThumb;
    }

    public void setVideoThumb(String videoThumb) {
        this.videoThumb = videoThumb;
    }

    public String getVideoThumbSmall() {
        return videoThumbSmall;
    }

    public void setVideoThumbSmall(String videoThumbSmall) {
        this.videoThumbSmall = videoThumbSmall;
    }

    public Boolean getShared() {
        return isShared;
    }

    public void setShared(Boolean shared) {
        isShared = shared;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getIsShared() {
        return isShared;
    }

    public void setIsShared(Boolean isShared) {
        this.isShared = isShared;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

}
