package com.signity.shopkeeperapp.model.Product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetailResponse {

    @Expose
    @SerializedName("success")
    private boolean success;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("data")
    private ProductDetail productData;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProductDetail getProductData() {
        return productData;
    }

    public void setProductData(ProductDetail productData) {
        this.productData = productData;
    }

    @Override
    public String toString() {
        return "ProductDetailResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", productData=" + productData +
                '}';
    }
}
