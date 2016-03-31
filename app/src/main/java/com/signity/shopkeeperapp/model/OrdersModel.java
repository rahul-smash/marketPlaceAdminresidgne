package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajesh on 16/10/15.
 */
public class OrdersModel {

    @SerializedName("orders_type")
    @Expose
    private String ordersType;
    @SerializedName("orders")
    @Expose
    private List<OrdersListModel> orders = new ArrayList<OrdersListModel>();

    /**
     *
     * @return
     * The ordersType
     */
    public String getOrdersType() {
        return ordersType;
    }

    /**
     *
     * @param ordersType
     * The orders_type
     */
    public void setOrdersType(String ordersType) {
        this.ordersType = ordersType;
    }

    /**
     *
     * @return
     * The orders
     */
    public List<OrdersListModel> getOrders() {
        return orders;
    }

    /**
     *
     * @param orders
     * The orders
     */
    public void setOrders(List<OrdersListModel> orders) {
        this.orders = orders;
    }
}
