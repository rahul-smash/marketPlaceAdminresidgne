package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajesh on 2/11/15.
 */
public class MobResponse {

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
