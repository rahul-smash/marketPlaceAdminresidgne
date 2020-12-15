package com.signity.shopkeeperapp.model.dashboard;

import com.google.gson.annotations.SerializedName;
import com.signity.shopkeeperapp.model.verify.StoreResponse;

import java.io.Serializable;

public class DataResponse implements Serializable {

    @SerializedName("products")
    private int products;

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
    private int cancelledOrders;

    @SerializedName("delivered_orders")
    private int deliveredOrders;

    @SerializedName("dashboard_orders")
    private DashboardOrdersData dashboardOrdersData;

    @SerializedName("customers")
    private int customers;

    @SerializedName("outstanding")
    private String outstanding;

    @SerializedName("store")
    private StoreResponse store;

    public DashboardOrdersData getDashboardOrdersData() {
        return dashboardOrdersData;
    }

    public void setDashboardOrdersData(DashboardOrdersData dashboardOrdersData) {
        this.dashboardOrdersData = dashboardOrdersData;
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

    public int getCancelledOrders() {
        return cancelledOrders;
    }

    public void setCancelledOrders(int cancelledOrders) {
        this.cancelledOrders = cancelledOrders;
    }

    public int getDeliveredOrders() {
        return deliveredOrders;
    }

    public void setDeliveredOrders(int deliveredOrders) {
        this.deliveredOrders = deliveredOrders;
    }

    public int getProducts() {
        return products;
    }

    public void setProducts(int products) {
        this.products = products;
    }

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

    public int getCustomers() {
        return customers;
    }

    public void setCustomers(int customers) {
        this.customers = customers;
    }

    public String getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(String outstanding) {
        this.outstanding = outstanding;
    }

    public StoreResponse getStore() {
        return store;
    }

    public void setStore(StoreResponse store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return
                "DataResponse{" +
                        "products = '" + products + '\'' +
                        ",total_orders = '" + totalOrders + '\'' +
                        ",due_orders = '" + dueOrders + '\'' +
                        ",active_orders = '" + activeOrders + '\'' +
                        ",customers = '" + customers + '\'' +
                        ",outstanding = '" + outstanding + '\'' +
                        ",store = '" + store + '\'' +
                        "}";
    }
}