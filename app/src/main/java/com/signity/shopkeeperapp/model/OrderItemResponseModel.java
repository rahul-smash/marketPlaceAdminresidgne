package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajesh on 20/10/15.
 */
public class OrderItemResponseModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    protected String message;
    @SerializedName("data")
    @Expose
    protected OrdersListModel ordersListModel;

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

    public OrdersListModel getOrdersListModel() {
        return ordersListModel;
    }

    public void setOrdersListModel(OrdersListModel ordersListModel) {
        this.ordersListModel = ordersListModel;
    }
}
