package com.signity.shopkeeperapp.model.Product;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectedVariant implements Parcelable {

    public static final Creator<SelectedVariant> CREATOR = new Creator<SelectedVariant>() {
        @Override
        public SelectedVariant createFromParcel(Parcel in) {
            return new SelectedVariant(in);
        }

        @Override
        public SelectedVariant[] newArray(int size) {
            return new SelectedVariant[size];
        }
    };
    @SerializedName("variant_id")
    @Expose
    private String variantId;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("mrp_price")
    @Expose
    private String mrpPrice;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("unit_type")
    @Expose
    private String unitType;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("custom_field1")
    @Expose
    private String customField1;
    @SerializedName("custom_field2")
    @Expose
    private String customField2;
    @SerializedName("custom_field3")
    @Expose
    private String customField3;
    @SerializedName("custom_field4")
    @Expose
    private String customField4;

    protected SelectedVariant(Parcel in) {
        variantId = in.readString();
        sku = in.readString();
        weight = in.readString();
        mrpPrice = in.readString();
        price = in.readString();
        discount = in.readString();
        unitType = in.readString();
        quantity = in.readString();
        customField1 = in.readString();
        customField2 = in.readString();
        customField3 = in.readString();
        customField4 = in.readString();
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCustomField1() {
        return customField1;
    }

    public void setCustomField1(String customField1) {
        this.customField1 = customField1;
    }

    public String getCustomField2() {
        return customField2;
    }

    public void setCustomField2(String customField2) {
        this.customField2 = customField2;
    }

    public String getCustomField3() {
        return customField3;
    }

    public void setCustomField3(String customField3) {
        this.customField3 = customField3;
    }

    public String getCustomField4() {
        return customField4;
    }

    public void setCustomField4(String customField4) {
        this.customField4 = customField4;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(variantId);
        dest.writeString(sku);
        dest.writeString(weight);
        dest.writeString(mrpPrice);
        dest.writeString(price);
        dest.writeString(discount);
        dest.writeString(unitType);
        dest.writeString(quantity);
        dest.writeString(customField1);
        dest.writeString(customField2);
        dest.writeString(customField3);
        dest.writeString(customField4);
    }
}