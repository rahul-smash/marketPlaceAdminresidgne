package com.signity.shopkeeperapp.model.orders.loyalty;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class LoyaltyPointsResponse implements Serializable {

	@SerializedName("success")
	private boolean success;

	@SerializedName("loyality_points")
	private String loyalityPoints;

	@SerializedName("data")
	private List<DataResponse> data;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setLoyalityPoints(String loyalityPoints){
		this.loyalityPoints = loyalityPoints;
	}

	public String getLoyalityPoints(){
		return loyalityPoints;
	}

	public void setData(List<DataResponse> data){
		this.data = data;
	}

	public List<DataResponse> getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"LoyaltyPointsResponse{" + 
			"success = '" + success + '\'' + 
			",loyality_points = '" + loyalityPoints + '\'' + 
			",data = '" + data + '\'' + 
			"}";
		}
}