package com.signity.shopkeeperapp.model.customers.addCustomer;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserResponse implements Serializable {

	@SerializedName("phone")
	private String phone;

	@SerializedName("full_name")
	private String fullName;

	@SerializedName("email")
	private String email;

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setFullName(String fullName){
		this.fullName = fullName;
	}

	public String getFullName(){
		return fullName;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"UserResponse{" + 
			"phone = '" + phone + '\'' + 
			",full_name = '" + fullName + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}