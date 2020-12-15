package com.signity.shopkeeperapp.model.dashboard;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DashboardOrdersData implements Serializable {

    @SerializedName("total_orders")
    private int totalOrders;

    @SerializedName("due_orders")
    private int dueOrders;

    @SerializedName("active_orders")
    private int activeOrders;

    @SerializedName("shipped_orders")
    private int shippedOrders;

    @SerializedName("rejected_orders")
    private int rejectedOrders;

    @SerializedName("cancel_orders")
    private int cancelOrders;

    @SerializedName("delivered_orders")
    private int deliveredOrders;

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getDueOrders() {
        return dueOrders;
    }

    public void setDueOrders(int dueOrders) {
        this.dueOrders = dueOrders;
    }

    public int getActiveOrders() {
        return activeOrders;
    }

    public void setActiveOrders(int activeOrders) {
        this.activeOrders = activeOrders;
    }

    public int getShippedOrders() {
        return shippedOrders;
    }

    public void setShippedOrders(int shippedOrders) {
        this.shippedOrders = shippedOrders;
    }

    public int getRejectedOrders() {
        return rejectedOrders;
    }

    public void setRejectedOrders(int rejectedOrders) {
        this.rejectedOrders = rejectedOrders;
    }

    public int getCancelOrders() {
        return cancelOrders;
    }

    public void setCancelOrders(int cancelOrders) {
        this.cancelOrders = cancelOrders;
    }

    public int getDeliveredOrders() {
        return deliveredOrders;
    }

    public void setDeliveredOrders(int deliveredOrders) {
        this.deliveredOrders = deliveredOrders;
    }

    @Override
    public String toString() {
        return
                "DashboardOrdersData{" +
                        "total_orders = '" + totalOrders + '\'' +
                        ",due_orders = '" + dueOrders + '\'' +
                        ",active_orders = '" + activeOrders + '\'' +
                        ",shipped_orders = '" + shippedOrders + '\'' +
                        ",rejected_orders = '" + rejectedOrders + '\'' +
                        ",cancel_orders = '" + cancelOrders + '\'' +
                        ",delivered_orders = '" + deliveredOrders + '\'' +
                        "}";
    }
}