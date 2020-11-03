package com.signity.shopkeeperapp.model.orders;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class CustomerData implements Serializable {

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private Data data;

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

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"CustomerData{" + 
			"success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			",data = '" + data + '\'' + 
			"}";
		}
}