package com.signity.shopkeeperapp.model.orderCount;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderCountResponse implements Parcelable {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("order_count")
    @Expose
    private Integer orderCount;
    @SerializedName("message")
    @Expose
    private String message;
    public OrderCountResponse() {
    }

    protected OrderCountResponse(Parcel in) {
        success = in.readBoolean();
        orderCount = in.readInt();
        message = in.readString();
    }

    public static final Creator<OrderCountResponse> CREATOR = new Creator<OrderCountResponse>() {
        @Override
        public OrderCountResponse createFromParcel(Parcel in) {
            return new OrderCountResponse(in);
        }

        @Override
        public OrderCountResponse[] newArray(int size) {
            return new OrderCountResponse[size];
        }
    };



    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(success);
        dest.writeInt(orderCount);
        dest.writeString(message);
    }
}