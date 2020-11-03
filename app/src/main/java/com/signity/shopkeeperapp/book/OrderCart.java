package com.signity.shopkeeperapp.book;

import com.signity.shopkeeperapp.model.Product.GetProductData;

import java.util.HashMap;
import java.util.Map;

public class OrderCart {

    private static Map<String, GetProductData> orderCartMap = new HashMap<>();

    public static void putOrder(GetProductData productData) {
        orderCartMap.put(productData.getId(), productData);
    }

    public static void removeOrder(String productId) {
        orderCartMap.remove(productId);
    }

    public static Map<String, GetProductData> getOrderCartMap() {
        return orderCartMap;
    }

    public static boolean isCartEmpty() {
        return orderCartMap.isEmpty();
    }

    public static void clearOrderCartMap() {
        orderCartMap.clear();
    }

}
