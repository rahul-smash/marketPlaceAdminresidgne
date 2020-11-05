package com.signity.shopkeeperapp.model.customers.addCustomer;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserAddressResponse implements Serializable {

	@SerializedName("softdelete")
	private String softdelete;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("store_id")
	private String storeId;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("email")
	private String email;

	@SerializedName("area_id")
	private String areaId;

	@SerializedName("area_name")
	private String areaName;

	@SerializedName("address")
	private String address;

	@SerializedName("city")
	private String city;

	@SerializedName("state")
	private String state;

	@SerializedName("zipcode")
	private String zipcode;

	@SerializedName("modified")
	private String modified;

	@SerializedName("created")
	private String created;

	@SerializedName("id")
	private String id;

	public void setSoftdelete(String softdelete){
		this.softdelete = softdelete;
	}

	public String getSoftdelete(){
		return softdelete;
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

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
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

	public void setModified(String modified){
		this.modified = modified;
	}

	public String getModified(){
		return modified;
	}

	public void setCreated(String created){
		this.created = created;
	}

	public String getCreated(){
		return created;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"UserAddressResponse{" + 
			"softdelete = '" + softdelete + '\'' + 
			",user_id = '" + userId + '\'' + 
			",store_id = '" + storeId + '\'' + 
			",first_name = '" + firstName + '\'' + 
			",mobile = '" + mobile + '\'' + 
			",email = '" + email + '\'' + 
			",area_id = '" + areaId + '\'' + 
			",area_name = '" + areaName + '\'' + 
			",address = '" + address + '\'' + 
			",city = '" + city + '\'' + 
			",state = '" + state + '\'' + 
			",zipcode = '" + zipcode + '\'' + 
			",modified = '" + modified + '\'' + 
			",created = '" + created + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}