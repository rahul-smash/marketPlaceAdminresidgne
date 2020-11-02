package com.signity.shopkeeperapp.model.customers.detail;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class CustomerAddressResponse implements Serializable {

	@SerializedName("id")
	private String id;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("store_id")
	private String storeId;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("email")
	private String email;

	@SerializedName("address")
	private String address;

	@SerializedName("address2")
	private String address2;

	@SerializedName("area_id")
	private String areaId;

	@SerializedName("area_name")
	private String areaName;

	@SerializedName("city")
	private String city;

	@SerializedName("state")
	private String state;

	@SerializedName("zipcode")
	private String zipcode;

	@SerializedName("country")
	private String country;

	@SerializedName("lat")
	private String lat;

	@SerializedName("lng")
	private String lng;

	@SerializedName("not_allow")
	private boolean notAllow;

	@SerializedName("area_charges")
	private String areaCharges;

	@SerializedName("min_amount")
	private String minAmount;

	@SerializedName("note")
	private String note;

	@SerializedName("city_id")
	private String cityId;

	@SerializedName("is_deleted")
	private boolean isDeleted;

	@SerializedName("delivery_time_slot")
	private DeliveryTimeSlotResponse deliveryTimeSlot;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setStoreId(String storeId){
		this.storeId = storeId;
	}

	public String getStoreId(){
		return storeId;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setAddress2(String address2){
		this.address2 = address2;
	}

	public String getAddress2(){
		return address2;
	}

	public void setAreaId(String areaId){
		this.areaId = areaId;
	}

	public String getAreaId(){
		return areaId;
	}

	public void setAreaName(String areaName){
		this.areaName = areaName;
	}

	public String getAreaName(){
		return areaName;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setZipcode(String zipcode){
		this.zipcode = zipcode;
	}

	public String getZipcode(){
		return zipcode;
	}

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setLat(String lat){
		this.lat = lat;
	}

	public String getLat(){
		return lat;
	}

	public void setLng(String lng){
		this.lng = lng;
	}

	public String getLng(){
		return lng;
	}

	public void setNotAllow(boolean notAllow){
		this.notAllow = notAllow;
	}

	public boolean isNotAllow(){
		return notAllow;
	}

	public void setAreaCharges(String areaCharges){
		this.areaCharges = areaCharges;
	}

	public String getAreaCharges(){
		return areaCharges;
	}

	public void setMinAmount(String minAmount){
		this.minAmount = minAmount;
	}

	public String getMinAmount(){
		return minAmount;
	}

	public void setNote(String note){
		this.note = note;
	}

	public String getNote(){
		return note;
	}

	public void setCityId(String cityId){
		this.cityId = cityId;
	}

	public String getCityId(){
		return cityId;
	}

	public void setIsDeleted(boolean isDeleted){
		this.isDeleted = isDeleted;
	}

	public boolean isIsDeleted(){
		return isDeleted;
	}

	public void setDeliveryTimeSlot(DeliveryTimeSlotResponse deliveryTimeSlot){
		this.deliveryTimeSlot = deliveryTimeSlot;
	}

	public DeliveryTimeSlotResponse getDeliveryTimeSlot(){
		return deliveryTimeSlot;
	}

	@Override
 	public String toString(){
		return 
			"CustomerAddressResponse{" + 
			"id = '" + id + '\'' + 
			",user_id = '" + userId + '\'' + 
			",store_id = '" + storeId + '\'' + 
			",first_name = '" + firstName + '\'' + 
			",last_name = '" + lastName + '\'' + 
			",mobile = '" + mobile + '\'' + 
			",email = '" + email + '\'' + 
			",address = '" + address + '\'' + 
			",address2 = '" + address2 + '\'' + 
			",area_id = '" + areaId + '\'' + 
			",area_name = '" + areaName + '\'' + 
			",city = '" + city + '\'' + 
			",state = '" + state + '\'' + 
			",zipcode = '" + zipcode + '\'' + 
			",country = '" + country + '\'' + 
			",lat = '" + lat + '\'' + 
			",lng = '" + lng + '\'' + 
			",not_allow = '" + notAllow + '\'' + 
			",area_charges = '" + areaCharges + '\'' + 
			",min_amount = '" + minAmount + '\'' + 
			",note = '" + note + '\'' + 
			",city_id = '" + cityId + '\'' + 
			",is_deleted = '" + isDeleted + '\'' + 
			",delivery_time_slot = '" + deliveryTimeSlot + '\'' + 
			"}";
		}
}