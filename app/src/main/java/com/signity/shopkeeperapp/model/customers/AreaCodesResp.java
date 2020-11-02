package com.signity.shopkeeperapp.model.customers;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AreaCodesResp implements Serializable {

	@SerializedName("success")
	private boolean success;

	@SerializedName("data")
	private List<DataResp> data;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setData(List<DataResp> data){
		this.data = data;
	}

	public List<DataResp> getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"AreaCodesResp{" + 
			"success = '" + success + '\'' + 
			",data = '" + data + '\'' + 
			"}";
		}
}