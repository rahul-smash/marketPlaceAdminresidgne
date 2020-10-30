package com.signity.shopkeeperapp.model.customers.detail;

import com.google.gson.annotations.SerializedName;
import com.signity.shopkeeperapp.model.OrdersListModel;

import java.io.Serializable;
import java.util.List;

public class DataResponse implements Serializable {

    @SerializedName("total_orders")
    private int totalOrders;

    @SerializedName("active_orders")
    private int activeOrders;

    @SerializedName("due_amount")
    private String dueAmount;

    @SerializedName("paid_amount")
    private String paidAmount;

    @SerializedName("loyalityPoints")
    private int loyalityPoints;

    @SerializedName("customers")
    private CustomersResponse customers;

    @SerializedName("customer_address")
    private CustomerAddressResponse customerAddress;

    @SerializedName("customer_order")
    private List<OrdersListModel> customerOrder;

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getActiveOrders() {
        return activeOrders;
    }

    public void setActiveOrders(int activeOrders) {
        this.activeOrders = activeOrders;
    }

    public String getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(String dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public int getLoyalityPoints() {
        return loyalityPoints;
    }

    public void setLoyalityPoints(int loyalityPoints) {
        this.loyalityPoints = loyalityPoints;
    }

    public CustomersResponse getCustomers() {
        return customers;
    }

    public void setCustomers(CustomersResponse customers) {
        this.customers = customers;
    }

    public CustomerAddressResponse getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(CustomerAddressResponse customerAddress) {
        this.customerAddress = customerAddress;
    }

    public List<OrdersListModel> getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(List<OrdersListModel> customerOrder) {
        this.customerOrder = customerOrder;
    }

    @Override
    public String toString() {
        return
                "DataResponse{" +
                        "total_orders = '" + totalOrders + '\'' +
                        ",active_orders = '" + activeOrders + '\'' +
                        ",due_amount = '" + dueAmount + '\'' +
                        ",paid_amount = '" + paidAmount + '\'' +
                        ",loyalityPoints = '" + loyalityPoints + '\'' +
                        ",customers = '" + customers + '\'' +
                        ",customer_address = '" + customerAddress + '\'' +
                        ",customer_order = '" + customerOrder + '\'' +
                        "}";
    }
}