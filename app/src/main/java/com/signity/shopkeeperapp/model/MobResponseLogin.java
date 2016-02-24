package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajesh on 7/1/16.
 */
public class MobResponseLogin {

    @SerializedName("user_exists")
    @Expose
    private Integer userExists;
    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("message")
    @Expose

    private String message;

    @SerializedName("data")
    @Expose
    private MobResponseDetails data;

    /**
     * @return The userExists
     */
    public Integer getUserExists() {
        return userExists;
    }

    /**
     * @param userExists The user_exists
     */
    public void setUserExists(Integer userExists) {
        this.userExists = userExists;
    }

    /**
     * @return The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * @param success The success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The data
     */
    public MobResponseDetails getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(MobResponseDetails data) {
        this.data = data;
    }

}
