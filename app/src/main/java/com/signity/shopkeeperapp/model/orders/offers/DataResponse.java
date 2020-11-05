package com.signity.shopkeeperapp.model.orders.offers;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DataResponse implements Serializable {

	@SerializedName("id")
	private String id;

	@SerializedName("store_id")
	private String storeId;

	@SerializedName("name")
	private String name;

	@SerializedName("coupon_code")
	private String couponCode;

	@SerializedName("discount")
	private String discount;

	@SerializedName("usage_limit")
	private String usageLimit;

	@SerializedName("minimum_order_amount")
	private String minimumOrderAmount;

	@SerializedName("order_facilities")
	private String orderFacilities;

	@SerializedName("offer_notification")
	private String offerNotification;

	@SerializedName("valid_from")
	private String validFrom;

	@SerializedName("valid_to")
	private String validTo;

	@SerializedName("offer_term_condition")
	private String offerTermCondition;

	@SerializedName("discount_upto")
	private String discountUpto;

	@SerializedName("discount_type")
	private String discountType;

	@SerializedName("image")
	private String image;

	@SerializedName("image_100_80")
	private String image10080;

	@SerializedName("image_300_200")
	private String image300200;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setStoreId(String storeId){
		this.storeId = storeId;
	}

	public String getStoreId(){
		return storeId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setCouponCode(String couponCode){
		this.couponCode = couponCode;
	}

	public String getCouponCode(){
		return couponCode;
	}

	public void setDiscount(String discount){
		this.discount = discount;
	}

	public String getDiscount(){
		return discount;
	}

	public void setUsageLimit(String usageLimit){
		this.usageLimit = usageLimit;
	}

	public String getUsageLimit(){
		return usageLimit;
	}

	public void setMinimumOrderAmount(String minimumOrderAmount){
		this.minimumOrderAmount = minimumOrderAmount;
	}

	public String getMinimumOrderAmount(){
		return minimumOrderAmount;
	}

	public void setOrderFacilities(String orderFacilities){
		this.orderFacilities = orderFacilities;
	}

	public String getOrderFacilities(){
		return orderFacilities;
	}

	public void setOfferNotification(String offerNotification){
		this.offerNotification = offerNotification;
	}

	public String getOfferNotification(){
		return offerNotification;
	}

	public void setValidFrom(String validFrom){
		this.validFrom = validFrom;
	}

	public String getValidFrom(){
		return validFrom;
	}

	public void setValidTo(String validTo){
		this.validTo = validTo;
	}

	public String getValidTo(){
		return validTo;
	}

	public void setOfferTermCondition(String offerTermCondition){
		this.offerTermCondition = offerTermCondition;
	}

	public String getOfferTermCondition(){
		return offerTermCondition;
	}

	public void setDiscountUpto(String discountUpto){
		this.discountUpto = discountUpto;
	}

	public String getDiscountUpto(){
		return discountUpto;
	}

	public void setDiscountType(String discountType){
		this.discountType = discountType;
	}

	public String getDiscountType(){
		return discountType;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setImage10080(String image10080){
		this.image10080 = image10080;
	}

	public String getImage10080(){
		return image10080;
	}

	public void setImage300200(String image300200){
		this.image300200 = image300200;
	}

	public String getImage300200(){
		return image300200;
	}

	@Override
 	public String toString(){
		return 
			"DataResponse{" + 
			"id = '" + id + '\'' + 
			",store_id = '" + storeId + '\'' + 
			",name = '" + name + '\'' + 
			",coupon_code = '" + couponCode + '\'' + 
			",discount = '" + discount + '\'' + 
			",usage_limit = '" + usageLimit + '\'' + 
			",minimum_order_amount = '" + minimumOrderAmount + '\'' + 
			",order_facilities = '" + orderFacilities + '\'' + 
			",offer_notification = '" + offerNotification + '\'' + 
			",valid_from = '" + validFrom + '\'' + 
			",valid_to = '" + validTo + '\'' + 
			",offer_term_condition = '" + offerTermCondition + '\'' + 
			",discount_upto = '" + discountUpto + '\'' + 
			",discount_type = '" + discountType + '\'' + 
			",image = '" + image + '\'' + 
			",image_100_80 = '" + image10080 + '\'' + 
			",image_300_200 = '" + image300200 + '\'' + 
			"}";
		}
}