package com.signity.shopkeeperapp.model.orders;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductModel implements Serializable {

    @SerializedName("product_id")
    private String productId;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("isTaxEnable")
    private String isTaxEnable;

    @SerializedName("variant_id")
    private String variantId;

    @SerializedName("weight")
    private String weight;

    @SerializedName("mrp_price")
    private String mrpPrice;

    @SerializedName("price")
    private String price;

    @SerializedName("discount")
    private String discount;

    @SerializedName("unit_type")
    private String unitType;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("product_type")
    private int productType;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIsTaxEnable() {
        return isTaxEnable;
    }

    public void setIsTaxEnable(String isTaxEnable) {
        this.isTaxEnable = isTaxEnable;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMrpPrice() {
        return mrpPrice;
    }

    public void setMrpPrice(String mrpPrice) {
        this.mrpPrice = mrpPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    @Override
    public String toString() {
        return
                "ProductModel{" +
                        "product_id = '" + productId + '\'' +
                        ",product_name = '" + productName + '\'' +
                        ",isTaxEnable = '" + isTaxEnable + '\'' +
                        ",variant_id = '" + variantId + '\'' +
                        ",weight = '" + weight + '\'' +
                        ",mrp_price = '" + mrpPrice + '\'' +
                        ",price = '" + price + '\'' +
                        ",discount = '" + discount + '\'' +
                        ",unit_type = '" + unitType + '\'' +
                        ",quantity = '" + quantity + '\'' +
                        ",product_type = '" + productType + '\'' +
                        "}";
    }
}