package com.signity.shopkeeperapp.model.verify;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserResponse implements Parcelable {

    public static final Creator<UserResponse> CREATOR = new Creator<UserResponse>() {
        @Override
        public UserResponse createFromParcel(Parcel in) {
            return new UserResponse(in);
        }

        @Override
        public UserResponse[] newArray(int size) {
            return new UserResponse[size];
        }
    };
    @SerializedName("id")
    private String id;
    @SerializedName("on_duty")
    private String onDuty;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lng")
    private String lng;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("fb_id")
    private String fbId;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("decoded_password")
    private String decodedPassword;
    @SerializedName("phone")
    private String phone;
    @SerializedName("profile_image")
    private String profileImage;
    @SerializedName("otp_verify")
    private String otpVerify;
    @SerializedName("user_refer_code")
    private String userReferCode;
    @SerializedName("user_referred_by")
    private String userReferredBy;
    @SerializedName("theme_id")
    private String themeId;
    @SerializedName("status")
    private String status;
    @SerializedName("login_status")
    private String loginStatus;
    @SerializedName("device_id")
    private String deviceId;
    @SerializedName("device_token")
    private String deviceToken;
    @SerializedName("platform")
    private String platform;
    @SerializedName("verification_code")
    private String verificationCode;
    @SerializedName("verification_code_status")
    private String verificationCodeStatus;
    @SerializedName("created")
    private String created;
    @SerializedName("modified")
    private String modified;

    protected UserResponse(Parcel in) {
        id = in.readString();
        onDuty = in.readString();
        lat = in.readString();
        lng = in.readString();
        fullName = in.readString();
        fbId = in.readString();
        email = in.readString();
        password = in.readString();
        decodedPassword = in.readString();
        phone = in.readString();
        profileImage = in.readString();
        otpVerify = in.readString();
        userReferCode = in.readString();
        userReferredBy = in.readString();
        themeId = in.readString();
        status = in.readString();
        loginStatus = in.readString();
        deviceId = in.readString();
        deviceToken = in.readString();
        platform = in.readString();
        verificationCode = in.readString();
        verificationCodeStatus = in.readString();
        created = in.readString();
        modified = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOnDuty() {
        return onDuty;
    }

    public void setOnDuty(String onDuty) {
        this.onDuty = onDuty;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
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

    public String getDecodedPassword() {
        return decodedPassword;
    }

    public void setDecodedPassword(String decodedPassword) {
        this.decodedPassword = decodedPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getOtpVerify() {
        return otpVerify;
    }

    public void setOtpVerify(String otpVerify) {
        this.otpVerify = otpVerify;
    }

    public String getUserReferCode() {
        return userReferCode;
    }

    public void setUserReferCode(String userReferCode) {
        this.userReferCode = userReferCode;
    }

    public String getUserReferredBy() {
        return userReferredBy;
    }

    public void setUserReferredBy(String userReferredBy) {
        this.userReferredBy = userReferredBy;
    }

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getVerificationCodeStatus() {
        return verificationCodeStatus;
    }

    public void setVerificationCodeStatus(String verificationCodeStatus) {
        this.verificationCodeStatus = verificationCodeStatus;
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
                "UserResponse{" +
                        "id = '" + id + '\'' +
                        ",on_duty = '" + onDuty + '\'' +
                        ",lat = '" + lat + '\'' +
                        ",lng = '" + lng + '\'' +
                        ",full_name = '" + fullName + '\'' +
                        ",fb_id = '" + fbId + '\'' +
                        ",email = '" + email + '\'' +
                        ",password = '" + password + '\'' +
                        ",decoded_password = '" + decodedPassword + '\'' +
                        ",phone = '" + phone + '\'' +
                        ",profile_image = '" + profileImage + '\'' +
                        ",otp_verify = '" + otpVerify + '\'' +
                        ",user_refer_code = '" + userReferCode + '\'' +
                        ",user_referred_by = '" + userReferredBy + '\'' +
                        ",theme_id = '" + themeId + '\'' +
                        ",status = '" + status + '\'' +
                        ",login_status = '" + loginStatus + '\'' +
                        ",device_id = '" + deviceId + '\'' +
                        ",device_token = '" + deviceToken + '\'' +
                        ",platform = '" + platform + '\'' +
                        ",verification_code = '" + verificationCode + '\'' +
                        ",verification_code_status = '" + verificationCodeStatus + '\'' +
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
        dest.writeString(onDuty);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(fullName);
        dest.writeString(fbId);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(decodedPassword);
        dest.writeString(phone);
        dest.writeString(profileImage);
        dest.writeString(otpVerify);
        dest.writeString(userReferCode);
        dest.writeString(userReferredBy);
        dest.writeString(themeId);
        dest.writeString(status);
        dest.writeString(loginStatus);
        dest.writeString(deviceId);
        dest.writeString(deviceToken);
        dest.writeString(platform);
        dest.writeString(verificationCode);
        dest.writeString(verificationCodeStatus);
        dest.writeString(created);
        dest.writeString(modified);
    }
}