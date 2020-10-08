package com.signity.shopkeeperapp.model.Product;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class StoreAttributes implements Serializable {

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private List<DynamicField> data;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setData(List<DynamicField> data){
		this.data = data;
	}

	public List<DynamicField> getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"StoreAttributes{" + 
			"success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			",data = '" + data + '\'' + 
			"}";
		}
}