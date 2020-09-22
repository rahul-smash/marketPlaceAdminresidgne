package com.signity.shopkeeperapp.model.verify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ketan on 22/09/20.
 */
public class MobileOtpReponse {

    @SerializedName("user_exists")
    @Expose
    private int userExists;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("message")
    @Expose

    private String message;

    @SerializedName("data")
    @Expose
    private UserResponse data;

    public int getUserExists() {
        return userExists;
    }

    public void setUserExists(int userExists) {
        this.userExists = userExists;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserResponse getData() {
        return data;
    }

    public void setData(UserResponse data) {
        this.data = data;
    }
}
