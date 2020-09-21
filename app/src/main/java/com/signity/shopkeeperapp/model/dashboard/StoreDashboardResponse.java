package com.signity.shopkeeperapp.model.dashboard;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class StoreDashboardResponse implements Serializable {

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private DataResponse data;

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

	public void setData(DataResponse data){
		this.data = data;
	}

	public DataResponse getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"StoreDashboardResponse{" + 
			"success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			",data = '" + data + '\'' + 
			"}";
		}
}