package com.signity.shopkeeperapp.model.customers.addCustomer;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AddCustomerResponse implements Serializable {

	@SerializedName("success")
	private boolean success;

	@SerializedName("data")
	private DataResponse data;

	@SerializedName("message")
	private String message;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setData(DataResponse data){
		this.data = data;
	}

	public DataResponse getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"AddCustomerResponse{" + 
			"success = '" + success + '\'' + 
			",data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}