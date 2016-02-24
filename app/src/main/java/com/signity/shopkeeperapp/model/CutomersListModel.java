package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajesh on 16/10/15.
 */
public class CutomersListModel {

    @SerializedName("customers_status")
    @Expose
    private String customersStatus;
    @SerializedName("customers")
    @Expose
    private List<UserModel> customers = new ArrayList<UserModel>();

    /**
     *
     * @return
     * The customersStatus
     */
    public String getCustomersStatus() {
        return customersStatus;
    }

    /**
     *
     * @param customersStatus
     * The customers_status
     */
    public void setCustomersStatus(String customersStatus) {
        this.customersStatus = customersStatus;
    }

    /**
     *
     * @return
     * The customers
     */
    public List<UserModel> getCustomers() {
        return customers;
    }

    /**
     *
     * @param customers
     * The customers
     */
    public void setCustomers(List<UserModel> customers) {
        this.customers = customers;
    }
}
