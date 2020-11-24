package com.signity.shopkeeperapp.model.creatives;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Creative implements Parcelable {

    public static final Creator<Creative> CREATOR = new Creator<Creative>() {
        @Override
        public Creative createFromParcel(Parcel in) {
            return new Creative(in);
        }

        @Override
        public Creative[] newArray(int size) {
            return new Creative[size];
        }
    };
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("is_shared")
    private boolean isShared;
    @Expose
    @SerializedName("post_id")
    private String postId;
    @Expose
    @SerializedName("image_thumbnail")
    private String imageThumbnail;
    @Expose
    @SerializedName("image")
    private String imageUrl;
    @Expose
    @SerializedName("image_id")
    private int imageId;
    @Expose
    private List<Tag> tags;
    @Expose(serialize = false, deserialize = false)
    private int likeCount;
    @Expose(serialize = false, deserialize = false)
    private int shareCount;
    @Expose(serialize = false, deserialize = false)
    private int commentCount;

    protected Creative(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        isShared = in.readByte() != 0;
        postId = in.readString();
        imageThumbnail = in.readString();
        imageUrl = in.readString();
        imageId = in.readInt();
        likeCount = in.readInt();
        shareCount = in.readInt();
        commentCount = in.readInt();
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "Creative{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isShared=" + isShared +
                ", postId='" + postId + '\'' +
                ", imageThumbnail='" + imageThumbnail + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageId=" + imageId +
                ", tags=" + tags +
                ", likeCount=" + likeCount +
                ", shareCount=" + shareCount +
                ", commentCount=" + commentCount +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeByte((byte) (isShared ? 1 : 0));
        dest.writeString(postId);
        dest.writeString(imageThumbnail);
        dest.writeString(imageUrl);
        dest.writeInt(imageId);
        dest.writeInt(likeCount);
        dest.writeInt(shareCount);
        dest.writeInt(commentCount);
    }
}
