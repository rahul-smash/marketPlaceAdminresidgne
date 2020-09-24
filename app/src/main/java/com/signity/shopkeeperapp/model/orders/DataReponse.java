package com.signity.shopkeeperapp.model.orders;

import com.google.gson.annotations.SerializedName;
import com.signity.shopkeeperapp.model.OrdersListModel;

import java.io.Serializable;
import java.util.List;

public class DataReponse implements Serializable {

    @SerializedName("orders_type")
    private String ordersType;

    @SerializedName("orders_total")
    private int ordersTotal;

    @SerializedName("orders")
    private List<OrdersListModel> orders;

    @SerializedName("page")
    private int page;

    public String getOrdersType() {
        return ordersType;
    }

    public void setOrdersType(String ordersType) {
        this.ordersType = ordersType;
    }

    public int getOrdersTotal() {
        return ordersTotal;
    }

    public void setOrdersTotal(int ordersTotal) {
        this.ordersTotal = ordersTotal;
    }

    public List<OrdersListModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersListModel> orders) {
        this.orders = orders;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return
                "DataReponse{" +
                        "orders_type = '" + ordersType + '\'' +
                        ",orders_total = '" + ordersTotal + '\'' +
                        ",orders = '" + orders + '\'' +
                        ",page = '" + page + '\'' +
                        "}";
    }
}