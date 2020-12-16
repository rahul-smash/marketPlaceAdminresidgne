package com.signity.shopkeeperapp.model.runner.areas;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AreaDTO implements Parcelable {

    public static final Creator<AreaDTO> CREATOR = new Creator<AreaDTO>() {
        @Override
        public AreaDTO createFromParcel(Parcel source) {
            return new AreaDTO(source);
        }

        @Override
        public AreaDTO[] newArray(int size) {
            return new AreaDTO[size];
        }
    };
    @SerializedName("area_id")
    private String areaId;
    @SerializedName("area_name")
    private String areaName;
    @SerializedName("pickup_add")
    private String pickupAdd;
    @SerializedName("city_id")
    private String cityId;
    @SerializedName("store_id")
    private String storeId;
    @Expose(deserialize = false)
    private boolean isChecked;

    public AreaDTO() {
    }

    protected AreaDTO(Parcel in) {
        this.areaId = in.readString();
        this.areaName = in.readString();
        this.pickupAdd = in.readString();
        this.cityId = in.readString();
        this.storeId = in.readString();
        this.isChecked = in.readByte() != 0;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getPickupAdd() {
        return pickupAdd;
    }

    public void setPickupAdd(String pickupAdd) {
        this.pickupAdd = pickupAdd;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return
                "AreaDTO{" +
                        "area_id = '" + areaId + '\'' +
                        ",area_name = '" + areaName + '\'' +
                        ",pickup_add = '" + pickupAdd + '\'' +
                        ",city_id = '" + cityId + '\'' +
                        ",store_id = '" + storeId + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.areaId);
        dest.writeString(this.areaName);
        dest.writeString(this.pickupAdd);
        dest.writeString(this.cityId);
        dest.writeString(this.storeId);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }
}