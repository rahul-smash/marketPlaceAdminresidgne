package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.signity.shopkeeperapp.customer.CustomerDetailFragment;

/**
 * Created by rajesh on 16/10/15.
 */
public class GetCustomerDetailModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private CustomerDetailModel data;

    /**
     *
     * @return
     * The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     * The data
     */
    public CustomerDetailModel getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(CustomerDetailModel data) {
        this.data = data;
    }
}
