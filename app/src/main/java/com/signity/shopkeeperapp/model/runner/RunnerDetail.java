package com.signity.shopkeeperapp.model.runner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RunnerDetail implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("status")
    private String status;

    @SerializedName("profile_image")
    private String profileImage;

    @SerializedName("social_platform")
    private String socialPlatform;

    @SerializedName("active_order")
    private int activeOrder;

    @SerializedName("area")
    private List<AreaResponse> area;

    @SerializedName("profile_image_100_80")
    private String profileImage10080;

    @SerializedName("profile_image_300_200")
    private String profileImage300200;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getSocialPlatform() {
        return socialPlatform;
    }

    public void setSocialPlatform(String socialPlatform) {
        this.socialPlatform = socialPlatform;
    }

    public int getActiveOrder() {
        return activeOrder;
    }

    public void setActiveOrder(int activeOrder) {
        this.activeOrder = activeOrder;
    }

    public String getProfileImage10080() {
        return profileImage10080;
    }

    public void setProfileImage10080(String profileImage10080) {
        this.profileImage10080 = profileImage10080;
    }

    public List<AreaResponse> getArea() {
        return area;
    }

    public void setArea(List<AreaResponse> area) {
        this.area = area;
    }

    public String getProfileImage300200() {
        return profileImage300200;
    }

    public void setProfileImage300200(String profileImage300200) {
        this.profileImage300200 = profileImage300200;
    }

    @Override
    public String toString() {
        return
                "DataDTO{" +
                        "id = '" + id + '\'' +
                        ",full_name = '" + fullName + '\'' +
                        ",email = '" + email + '\'' +
                        ",phone = '" + phone + '\'' +
                        ",status = '" + status + '\'' +
                        ",profile_image = '" + profileImage + '\'' +
                        ",social_platform = '" + socialPlatform + '\'' +
                        ",actie_order = '" + activeOrder + '\'' +
                        ",profile_image_100_80 = '" + profileImage10080 + '\'' +
                        ",profile_image_300_200 = '" + profileImage300200 + '\'' +
                        "}";
    }
}