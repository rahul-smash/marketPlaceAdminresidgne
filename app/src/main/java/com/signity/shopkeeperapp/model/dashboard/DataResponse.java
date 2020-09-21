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

    @SerializedName("customers")
    private int customers;

    @SerializedName("outstanding")
    private String outstanding;

    @SerializedName("store")
    private StoreResponse store;

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