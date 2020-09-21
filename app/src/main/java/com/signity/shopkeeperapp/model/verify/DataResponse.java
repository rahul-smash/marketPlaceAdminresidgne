package com.signity.shopkeeperapp.model.verify;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DataResponse implements Parcelable {

    public static final Creator<DataResponse> CREATOR = new Creator<DataResponse>() {
        @Override
        public DataResponse createFromParcel(Parcel in) {
            return new DataResponse(in);
        }

        @Override
        public DataResponse[] newArray(int size) {
            return new DataResponse[size];
        }
    };
    @SerializedName("User")
    private UserResponse userResponse;
    @SerializedName("Store")
    private StoreResponse storeResponse;

    protected DataResponse(Parcel in) {
        userResponse = in.readParcelable(UserResponse.class.getClassLoader());
        storeResponse = in.readParcelable(StoreResponse.class.getClassLoader());
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }

    public StoreResponse getStoreResponse() {
        return storeResponse;
    }

    public void setStoreResponse(StoreResponse storeResponse) {
        this.storeResponse = storeResponse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(userResponse, flags);
        dest.writeParcelable(storeResponse, flags);
    }
}