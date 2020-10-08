package com.signity.shopkeeperapp.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Variant implements Parcelable {

    public static final Creator<Variant> CREATOR = new Creator<Variant>() {
        @Override
        public Variant createFromParcel(Parcel in) {
            return new Variant(in);
        }

        @Override
        public Variant[] newArray(int size) {
            return new Variant[size];
        }
    };
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("unit_type")
    @Expose
    private String unitType;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("mrp_price")
    @Expose
    private String mrpPrice;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("order_by")
    @Expose
    private String orderBy;

    public Variant() {
    }

    protected Variant(Parcel in) {
        id = in.readString();
        storeId = in.readString();
        productId = in.readString();
        sku = in.readString();
        unitType = in.readString();
        weight = in.readString();
        mrpPrice = in.readString();
        discount = in.readString();
        price = in.readString();
        orderBy = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(storeId);
        dest.writeString(productId);
        dest.writeString(sku);
        dest.writeString(unitType);
        dest.writeString(weight);
        dest.writeString(mrpPrice);
        dest.writeString(discount);
        dest.writeString(price);
        dest.writeString(orderBy);
    }
}
