package com.signity.shopkeeperapp.model.market.industry;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("valueapp_store_id")
	private int valueappStoreId;

	@SerializedName("title")
	private String title;

	@SerializedName("address")
	private String address;

	@SerializedName("city")
	private String city;

	@SerializedName("state")
	private String state;

	@SerializedName("country")
	private String country;

	@SerializedName("postal")
	private String postal;

	@SerializedName("email")
	private String email;

	@SerializedName("contact_number")
	private String contactNumber;

	@SerializedName("description")
	private String description;

	@SerializedName("is_primary")
	private boolean isPrimary;

	@SerializedName("status")
	private boolean status;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("longitude")
	private String longitude;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("servicetype")
	private String servicetype;

	@SerializedName("user_type")
	private String userType;

	@SerializedName("industrytype")
	private Industrytype industrytype;

	@SerializedName("display_title")
	private String displayTitle;

	@SerializedName("display_contact_number")
	private String displayContactNumber;

	@SerializedName("currency")
	private String currency;

	@SerializedName("industrycountry")
	private Industrycountry industrycountry;

	@SerializedName("latest_app_version")
	private String latestAppVersion;

	@SerializedName("logged_date_time")
	private String loggedDateTime;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setValueappStoreId(int valueappStoreId){
		this.valueappStoreId = valueappStoreId;
	}

	public int getValueappStoreId(){
		return valueappStoreId;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
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

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setPostal(String postal){
		this.postal = postal;
	}

	public String getPostal(){
		return postal;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setContactNumber(String contactNumber){
		this.contactNumber = contactNumber;
	}

	public String getContactNumber(){
		return contactNumber;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setIsPrimary(boolean isPrimary){
		this.isPrimary = isPrimary;
	}

	public boolean isIsPrimary(){
		return isPrimary;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setServicetype(String servicetype){
		this.servicetype = servicetype;
	}

	public String getServicetype(){
		return servicetype;
	}

	public void setUserType(String userType){
		this.userType = userType;
	}

	public String getUserType(){
		return userType;
	}

	public void setIndustrytype(Industrytype industrytype){
		this.industrytype = industrytype;
	}

	public Industrytype getIndustrytype(){
		return industrytype;
	}

	public void setDisplayTitle(String displayTitle){
		this.displayTitle = displayTitle;
	}

	public String getDisplayTitle(){
		return displayTitle;
	}

	public void setDisplayContactNumber(String displayContactNumber){
		this.displayContactNumber = displayContactNumber;
	}

	public String getDisplayContactNumber(){
		return displayContactNumber;
	}

	public void setCurrency(String currency){
		this.currency = currency;
	}

	public String getCurrency(){
		return currency;
	}

	public void setIndustrycountry(Industrycountry industrycountry){
		this.industrycountry = industrycountry;
	}

	public Industrycountry getIndustrycountry(){
		return industrycountry;
	}

	public void setLatestAppVersion(String latestAppVersion){
		this.latestAppVersion = latestAppVersion;
	}

	public String getLatestAppVersion(){
		return latestAppVersion;
	}

	public void setLoggedDateTime(String loggedDateTime){
		this.loggedDateTime = loggedDateTime;
	}

	public String getLoggedDateTime(){
		return loggedDateTime;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"id = '" + id + '\'' + 
			",valueapp_store_id = '" + valueappStoreId + '\'' + 
			",title = '" + title + '\'' + 
			",address = '" + address + '\'' + 
			",city = '" + city + '\'' + 
			",state = '" + state + '\'' + 
			",country = '" + country + '\'' + 
			",postal = '" + postal + '\'' + 
			",email = '" + email + '\'' + 
			",contact_number = '" + contactNumber + '\'' + 
			",description = '" + description + '\'' + 
			",is_primary = '" + isPrimary + '\'' + 
			",status = '" + status + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",longitude = '" + longitude + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",servicetype = '" + servicetype + '\'' + 
			",user_type = '" + userType + '\'' + 
			",industrytype = '" + industrytype + '\'' + 
			",display_title = '" + displayTitle + '\'' + 
			",display_contact_number = '" + displayContactNumber + '\'' + 
			",currency = '" + currency + '\'' + 
			",industrycountry = '" + industrycountry + '\'' + 
			",latest_app_version = '" + latestAppVersion + '\'' + 
			",logged_date_time = '" + loggedDateTime + '\'' + 
			"}";
		}
}