package com.signity.shopkeeperapp.model.dashboard;

import com.google.gson.annotations.SerializedName;

public class WelcomeResponse {

	@SerializedName("messages")
	private Data data;

	@SerializedName("status")
	private boolean status;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"WelcomeResponse{" + 
			"data = '" + data + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}