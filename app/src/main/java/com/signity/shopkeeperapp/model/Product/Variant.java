package com.signity.shopkeeperapp.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Variant implements Parcelable {

    public static final Creator<Variant> CREATOR = new Creator<Variant>() {
        @Override
        public Variant createFromParcel(Parcel source) {
            return new Variant(source);
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
    @Expose
    @SerializedName("custom_field1")
    private String stock;
    @Expose
    @SerializedName("custom_field2")
    private String minStock;
    @Expose
    @SerializedName("custom_field3")
    private String sellingChooser;

    public Variant() {
    }

    protected Variant(Parcel in) {
        this.id = in.readString();
        this.storeId = in.readString();
        this.productId = in.readString();
        this.sku = in.readString();
        this.unitType = in.readString();
        this.weight = in.readString();
        this.mrpPrice = in.readString();
        this.discount = in.readString();
        this.price = in.readString();
        this.orderBy = in.readString();
        this.stock = in.readString();
        this.minStock = in.readString();
        this.sellingChooser = in.readString();
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getMinStock() {
        return minStock;
    }

    public void setMinStock(String minStock) {
        this.minStock = minStock;
    }

    public String getSellingChooser() {
        return sellingChooser;
    }

    public void setSellingChooser(String sellingChooser) {
        this.sellingChooser = sellingChooser;
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
        dest.writeString(this.id);
        dest.writeString(this.storeId);
        dest.writeString(this.productId);
        dest.writeString(this.sku);
        dest.writeString(this.unitType);
        dest.writeString(this.weight);
        dest.writeString(this.mrpPrice);
        dest.writeString(this.discount);
        dest.writeString(this.price);
        dest.writeString(this.orderBy);
        dest.writeString(this.stock);
        dest.writeString(this.minStock);
        dest.writeString(this.sellingChooser);
    }
}
