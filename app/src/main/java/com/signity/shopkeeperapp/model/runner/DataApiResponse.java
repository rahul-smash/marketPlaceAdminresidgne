package com.signity.shopkeeperapp.model.runner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataApiResponse implements Serializable {

    @SerializedName("phone")
    private String phone;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("profile_image")
    private String profileImage;

    @SerializedName("id")
    private String id;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return
                "DataApiResponse{" +
                        "phone = '" + phone + '\'' +
                        ",full_name = '" + fullName + '\'' +
                        ",email = '" + email + '\'' +
                        ",password = '" + password + '\'' +
                        ",profile_image = '" + profileImage + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}