package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajesh on 23/12/15.
 */
public class EnquiriesModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<EnquiriesListModel> enquiriesList = new ArrayList<EnquiriesListModel>();

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


    public List<EnquiriesListModel> getEnquiriesList() {
        return enquiriesList;
    }

    public void setEnquiriesList(List<EnquiriesListModel> enquiriesList) {
        this.enquiriesList = enquiriesList;
    }

}
