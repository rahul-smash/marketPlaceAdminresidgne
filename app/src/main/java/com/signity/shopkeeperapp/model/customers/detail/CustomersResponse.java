package com.signity.shopkeeperapp.model.customers.detail;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class CustomersResponse implements Serializable {

	@SerializedName("id")
	private String id;

	@SerializedName("full_name")
	private String fullName;

	@SerializedName("phone")
	private String phone;

	@SerializedName("email")
	private String email;

	@SerializedName("status")
	private String status;

	@SerializedName("profile_image")
	private String profileImage;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setFullName(String fullName){
		this.fullName = fullName;
	}

	public String getFullName(){
		return fullName;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setProfileImage(String profileImage){
		this.profileImage = profileImage;
	}

	public String getProfileImage(){
		return profileImage;
	}

	@Override
 	public String toString(){
		return 
			"CustomersResponse{" + 
			"id = '" + id + '\'' + 
			",full_name = '" + fullName + '\'' + 
			",phone = '" + phone + '\'' + 
			",email = '" + email + '\'' + 
			",status = '" + status + '\'' + 
			",profile_image = '" + profileImage + '\'' + 
			"}";
		}
}