package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajesh on 16/10/15.
 */
public class CustomerDetailModel {

    @SerializedName("total_orders")
    @Expose
    private Integer totalOrders;
    @SerializedName("active_orders")
    @Expose
    private Integer activeOrders;
    @SerializedName("due_amount")
    @Expose
    private Integer dueAmount;
    @SerializedName("paid_amount")
    @Expose
    private Integer paidAmount;
    @SerializedName("customers")
    @Expose
    private List<UserModel> customers = new ArrayList<UserModel>();

    /**
     * @return The totalOrders
     */
    public Integer getTotalOrders() {
        return totalOrders;
    }

    /**
     * @param totalOrders The total_orders
     */
    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
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
     * @return The dueAmount
     */
    public Integer getDueAmount() {
        return dueAmount;
    }

    /**
     * @param dueAmount The due_amount
     */
    public void setDueAmount(Integer dueAmount) {
        this.dueAmount = dueAmount;
    }

    /**
     * @return The paidAmount
     */
    public Integer getPaidAmount() {
        return paidAmount;
    }

    /**
     * @param paidAmount The paid_amount
     */
    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    /**
     * @return The customers
     */
    public List<UserModel> getCustomers() {
        return customers;
    }

    /**
     * @param customers The customers
     */
    public void setCustomers(List<UserModel> customers) {
        this.customers = customers;
    }
}
