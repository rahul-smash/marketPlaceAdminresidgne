package com.signity.shopkeeperapp.model.orders.offers;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataDTO implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("store_id")
    private String storeId;

    @SerializedName("discount_type")
    private String discountType;

    @SerializedName("order_facilities")
    private String orderFacilities;

    @SerializedName("payment_method")
    private String paymentMethod;

    @SerializedName("name")
    private String name;

    @SerializedName("coupon_code")
    private String couponCode;

    @SerializedName("discount")
    private String discount;

    @SerializedName("discount_upto")
    private String discountUpto;

    @SerializedName("minimum_order_amount")
    private String minimumOrderAmount;

    @SerializedName("usage_limit")
    private String usageLimit;

    @SerializedName("valid_from")
    private String validFrom;

    @SerializedName("valid_to")
    private String validTo;

    @SerializedName("offer_notification")
    private String offerNotification;

    @SerializedName("offer_term_condition")
    private String offerTermCondition;

    @SerializedName("offer_description")
    private String offerDescription;

    @SerializedName("status")
    private String status;

    @SerializedName("show")
    private String show;

    @SerializedName("sort")
    private String sort;

    @SerializedName("created")
    private String created;

    @SerializedName("modified")
    private String modified;

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

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getOrderFacilities() {
        return orderFacilities;
    }

    public void setOrderFacilities(String orderFacilities) {
        this.orderFacilities = orderFacilities;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountUpto() {
        return discountUpto;
    }

    public void setDiscountUpto(String discountUpto) {
        this.discountUpto = discountUpto;
    }

    public String getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public void setMinimumOrderAmount(String minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }

    public String getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(String usageLimit) {
        this.usageLimit = usageLimit;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getOfferNotification() {
        return offerNotification;
    }

    public void setOfferNotification(String offerNotification) {
        this.offerNotification = offerNotification;
    }

    public String getOfferTermCondition() {
        return offerTermCondition;
    }

    public void setOfferTermCondition(String offerTermCondition) {
        this.offerTermCondition = offerTermCondition;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return
                "DataDTO{" +
                        "id = '" + id + '\'' +
                        ",store_id = '" + storeId + '\'' +
                        ",discount_type = '" + discountType + '\'' +
                        ",order_facilities = '" + orderFacilities + '\'' +
                        ",payment_method = '" + paymentMethod + '\'' +
                        ",name = '" + name + '\'' +
                        ",coupon_code = '" + couponCode + '\'' +
                        ",discount = '" + discount + '\'' +
                        ",discount_upto = '" + discountUpto + '\'' +
                        ",minimum_order_amount = '" + minimumOrderAmount + '\'' +
                        ",usage_limit = '" + usageLimit + '\'' +
                        ",valid_from = '" + validFrom + '\'' +
                        ",valid_to = '" + validTo + '\'' +
                        ",offer_notification = '" + offerNotification + '\'' +
                        ",offer_term_condition = '" + offerTermCondition + '\'' +
                        ",offer_description = '" + offerDescription + '\'' +
                        ",status = '" + status + '\'' +
                        ",show = '" + show + '\'' +
                        ",sort = '" + sort + '\'' +
                        ",created = '" + created + '\'' +
                        ",modified = '" + modified + '\'' +
                        "}";
    }
}