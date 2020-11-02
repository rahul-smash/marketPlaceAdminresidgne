package com.signity.shopkeeperapp.model.customers;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DataResponse implements Serializable {

    @SerializedName("page")
    private int page;

    @SerializedName("pagelength")
    private int pagelength;

    @SerializedName("customers_count")
    private int customersCount;

    @SerializedName("customers_status")
    private String customersStatus;

    @SerializedName("customers")
    private List<CustomersResponse> customers;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagelength() {
        return pagelength;
    }

    public void setPagelength(int pagelength) {
        this.pagelength = pagelength;
    }

    public int getCustomersCount() {
        return customersCount;
    }

    public void setCustomersCount(int customersCount) {
        this.customersCount = customersCount;
    }

    public String getCustomersStatus() {
        return customersStatus;
    }

    public void setCustomersStatus(String customersStatus) {
        this.customersStatus = customersStatus;
    }

    public List<CustomersResponse> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomersResponse> customers) {
        this.customers = customers;
    }

    @Override
    public String toString() {
        return
                "DataResponse{" +
                        "page = '" + page + '\'' +
                        ",pagelength = '" + pagelength + '\'' +
                        ",customers_count = '" + customersCount + '\'' +
                        ",customers_status = '" + customersStatus + '\'' +
                        ",customers = '" + customers + '\'' +
                        "}";
    }
}