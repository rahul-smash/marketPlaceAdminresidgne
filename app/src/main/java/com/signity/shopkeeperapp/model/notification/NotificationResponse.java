package com.signity.shopkeeperapp.model.notification;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NotificationResponse implements Parcelable {

    public static final Creator<NotificationResponse> CREATOR = new Creator<NotificationResponse>() {
        @Override
        public NotificationResponse createFromParcel(Parcel in) {
            return new NotificationResponse(in);
        }

        @Override
        public NotificationResponse[] newArray(int size) {
            return new NotificationResponse[size];
        }
    };
    @SerializedName("id")
    private String id;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("message")
    private String message;
    @SerializedName("type")
    private String type;
    @SerializedName("is_read")
    private boolean isRead;
    @SerializedName("action")
    private String action;
    @SerializedName("action_id")
    private String actionId;
    @SerializedName("created")
    private String created;
    @SerializedName("modified")
    private String modified;

    protected NotificationResponse(Parcel in) {
        id = in.readString();
        storeId = in.readString();
        userId = in.readString();
        message = in.readString();
        type = in.readString();
        isRead = in.readByte() != 0;
        action = in.readString();
        actionId = in.readString();
        created = in.readString();
        modified = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return
                "NotificationResponse{" +
                        "id = '" + id + '\'' +
                        ",store_id = '" + storeId + '\'' +
                        ",user_id = '" + userId + '\'' +
                        ",message = '" + message + '\'' +
                        ",type = '" + type + '\'' +
                        ",is_read = '" + isRead + '\'' +
                        ",action = '" + action + '\'' +
                        ",action_id = '" + actionId + '\'' +
                        ",created = '" + created + '\'' +
                        ",modified = '" + modified + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(storeId);
        dest.writeString(userId);
        dest.writeString(message);
        dest.writeString(type);
        dest.writeByte((byte) (isRead ? 1 : 0));
        dest.writeString(action);
        dest.writeString(actionId);
        dest.writeString(created);
        dest.writeString(modified);
    }
}