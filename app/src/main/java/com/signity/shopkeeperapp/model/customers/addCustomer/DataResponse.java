package com.signity.shopkeeperapp.model.customers.addCustomer;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DataResponse implements Serializable {

	@SerializedName("User")
	private UserResponse user;

	@SerializedName("StoreUser")
	private StoreUserResponse storeUser;

	@SerializedName("address")
	private AddressResponse address;

	public void setUser(UserResponse user){
		this.user = user;
	}

	public UserResponse getUser(){
		return user;
	}

	public void setStoreUser(StoreUserResponse storeUser){
		this.storeUser = storeUser;
	}

	public StoreUserResponse getStoreUser(){
		return storeUser;
	}

	public void setAddress(AddressResponse address){
		this.address = address;
	}

	public AddressResponse getAddress(){
		return address;
	}

	@Override
 	public String toString(){
		return 
			"DataResponse{" + 
			"user = '" + user + '\'' + 
			",storeUser = '" + storeUser + '\'' + 
			",address = '" + address + '\'' + 
			"}";
		}
}