package com.signity.shopkeeperapp.model.orders.checkout;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderCalculationResponse implements Serializable {

	@SerializedName("success")
	private boolean success;

	@SerializedName("data")
	private DataResponse data;

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

	@Override
 	public String toString(){
		return 
			"OrderCalculationResponse{" + 
			"success = '" + success + '\'' + 
			",data = '" + data + '\'' + 
			"}";
		}
}