package com.signity.shopkeeperapp.model.Categories;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategory implements Parcelable {
    public static final Creator<SubCategory> CREATOR = new Creator<SubCategory>() {
        @Override
        public SubCategory createFromParcel(Parcel source) {
            return new SubCategory(source);
        }

        @Override
        public SubCategory[] newArray(int size) {
            return new SubCategory[size];
        }
    };
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("deleted")
    @Expose
    private Boolean deleted;
    @SerializedName("sort")
    @Expose
    private String sort;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("image_100_80")
    @Expose
    private String image10080;
    @SerializedName("image_300_200")
    @Expose
    private String image300200;
    @Expose(serialize = false, deserialize = false)
    private String categoryName;
    @Expose(serialize = false, deserialize = false)
    private String categoryId;
    @Expose(serialize = false, deserialize = false)
    private boolean isOpen;

    public SubCategory() {
    }

    protected SubCategory(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.version = in.readString();
        this.status = in.readString();
        this.deleted = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.sort = in.readString();
        this.image = in.readString();
        this.image10080 = in.readString();
        this.image300200 = in.readString();
        this.categoryName = in.readString();
        this.categoryId = in.readString();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage10080() {
        return image10080;
    }

    public void setImage10080(String image10080) {
        this.image10080 = image10080;
    }

    public String getImage300200() {
        return image300200;
    }

    public void setImage300200(String image300200) {
        this.image300200 = image300200;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.version);
        dest.writeString(this.status);
        dest.writeValue(this.deleted);
        dest.writeString(this.sort);
        dest.writeString(this.image);
        dest.writeString(this.image10080);
        dest.writeString(this.image300200);
        dest.writeString(this.categoryName);
        dest.writeString(this.categoryId);
    }
}
