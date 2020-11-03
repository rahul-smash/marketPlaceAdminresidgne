package com.signity.shopkeeperapp.book;

import com.signity.shopkeeperapp.model.Product.GetProductData;

public interface OrderCartListener {
    void onAddProduct(GetProductData getProductData);

    void onRemoveProduct(GetProductData getProductData);
}
