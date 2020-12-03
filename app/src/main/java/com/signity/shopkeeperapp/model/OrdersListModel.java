package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajesh on 16/10/15.
 */
public class OrdersListModel implements Serializable {


    @SerializedName("user_lat")
    @Expose
    private String Destinationuser_lat;
    @SerializedName("user_lng")
    @Expose
    private String Destinationuser_lng;
    @SerializedName("display_order_id")
    @Expose
    private String display_order_id;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("runner_id")
    @Expose
    private String runnerId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("total_amount")
    @Expose
    private Double totalAmount;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("discount")
    @Expose
    private Double discount;
    @SerializedName("total")
    @Expose
    private Double total;
    @SerializedName("checkout")
    @Expose
    private Double checkout;
    @SerializedName("tax")
    @Expose
    private Double tax;
    @SerializedName("shipping_charges")
    @Expose
    private Double shippingCharges;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("items")
    @Expose
    private List<ItemListModel> items = new ArrayList<ItemListModel>();
    @SerializedName("calculated_tax_detail")
    @Expose
    private List<OrderTaxModel> taxes = new ArrayList<OrderTaxModel>();
    @SerializedName("store_fixed_tax_detail")
    @Expose
    private List<StoreTaxModel> storeTaxes = new ArrayList<StoreTaxModel>();
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @Expose
    private String platform;
    @SerializedName("order_facility")
    @Expose
    private String orderFacility;
    @SerializedName("delivery_time_slot")
    @Expose
    private String deliveryTimeSlot;
    @Expose(serialize = false)
    private int pageNumber;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getDeliveryTimeSlot() {
        return deliveryTimeSlot;
    }

    public void setDeliveryTimeSlot(String deliveryTimeSlot) {
        this.deliveryTimeSlot = deliveryTimeSlot;
    }

    public String getRunnerId() {
        return runnerId;
    }

    public void setRunnerId(String runnerId) {
        this.runnerId = runnerId;
    }

    public String getOrderFacility() {
        return orderFacility;
    }

    public void setOrderFacility(String orderFacility) {
        this.orderFacility = orderFacility;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDestinationuser_lat() {
        return Destinationuser_lat;
    }

    public void setDestinationuser_lat(String destinationuser_lat) {
        Destinationuser_lat = destinationuser_lat;
    }

    public String getDestinationuser_lng() {
        return Destinationuser_lng;
    }

    public void setDestinationuser_lng(String destinationuser_lng) {
        Destinationuser_lng = destinationuser_lng;
    }

    public String getDisplay_order_id() {
        return display_order_id;
    }

    public void setDisplay_order_id(String display_order_id) {
        this.display_order_id = display_order_id;
    }

    /**
     * @return The orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId The order_id
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName The customer_name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }


    /**
     * @return The time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return The totalAmount
     */
    public Double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount The total_amount
     */
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return The note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note The note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return The discount
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * @param discount The discount
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    /**
     * @return The total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * @param total The total
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * @return The checkout
     */
    public Double getCheckout() {
        return checkout;
    }

    /**
     * @param checkout The checkout
     */
    public void setCheckout(Double checkout) {
        this.checkout = checkout;
    }

    /**
     * @return The shippingCharges
     */
    public Double getShippingCharges() {
        return shippingCharges;
    }

    /**
     * @param shippingCharges The shipping_charges
     */
    public void setShippingCharges(Double shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    /**
     * @return The couponCode
     */
    public String getCouponCode() {
        return couponCode;
    }

    /**
     * @param couponCode The coupon_code
     */
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    /**
     * @return The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address The address
     */
    public void setAddress(String address) {
        this.address = address;
    }


    /**
     * @return The items
     */
    public List<ItemListModel> getItems() {
        return items;
    }

    /**
     * @param items The items
     */
    public void setItems(List<ItemListModel> items) {
        this.items = items;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }


    public List<OrderTaxModel> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<OrderTaxModel> taxes) {
        this.taxes = taxes;
    }

    public List<StoreTaxModel> getStoreTaxes() {
        return storeTaxes;
    }

    public void setStoreTaxes(List<StoreTaxModel> storeTaxes) {
        this.storeTaxes = storeTaxes;
    }
}
