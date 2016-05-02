package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajesh on 16/10/15.
 */
public class DashBoardModelDetail {

    @SerializedName("due_orders")
    @Expose
    private Integer dueOrders;
    @SerializedName("active_orders")
    @Expose
    private Integer activeOrders;
    @SerializedName("customers")
    @Expose
    private Integer customers;
    @SerializedName("outstanding")
    @Expose
    private Double outstanding;

    @SerializedName("store")
    @Expose
    private DashBoardModelStoreDetail store;

    /**
     * @return The dueOrders
     */
    public Integer getDueOrders() {
        return dueOrders;
    }

    /**
     * @param dueOrders The due_orders
     */
    public void setDueOrders(Integer dueOrders) {
        this.dueOrders = dueOrders;
    }

    /**
     * @return The activeOrders
     */
    public Integer getActiveOrders() {
        return activeOrders;
    }

    /**
     * @param activeOrders The active_orders
     */
    public void setActiveOrders(Integer activeOrders) {
        this.activeOrders = activeOrders;
    }

    /**
     * @return The customers
     */
    public Integer getCustomers() {
        return customers;
    }

    /**
     * @param customers The customers
     */
    public void setCustomers(Integer customers) {
        this.customers = customers;
    }

    /**
     * @return The outstanding
     */
    public Double getOutstanding() {
        return outstanding;
    }

    /**
     * @param outstanding The outstanding
     */
    public void setOutstanding(Double outstanding) {
        this.outstanding = outstanding;
    }


    /**
     * @return The store
     */
    public DashBoardModelStoreDetail getStore() {
        return store;
    }

    /**
     * @param store The store
     */
    public void setStore(DashBoardModelStoreDetail store) {
        this.store = store;
    }


    @Override
    public String toString() {
        return "DashBoardModelDetail{" +
                "store=" + store +
                ", outstanding=" + outstanding +
                ", customers=" + customers +
                ", activeOrders=" + activeOrders +
                ", dueOrders=" + dueOrders +
                '}';
    }
}


