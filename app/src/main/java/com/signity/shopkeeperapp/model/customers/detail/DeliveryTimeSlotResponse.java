package com.signity.shopkeeperapp.model.customers.detail;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DeliveryTimeSlotResponse implements Serializable {

	@SerializedName("zone_id")
	private String zoneId;

	@SerializedName("is24x7_open")
	private String is24x7Open;

	public void setZoneId(String zoneId){
		this.zoneId = zoneId;
	}

	public String getZoneId(){
		return zoneId;
	}

	public void setIs24x7Open(String is24x7Open){
		this.is24x7Open = is24x7Open;
	}

	public String getIs24x7Open(){
		return is24x7Open;
	}

	@Override
 	public String toString(){
		return 
			"DeliveryTimeSlotResponse{" + 
			"zone_id = '" + zoneId + '\'' + 
			",is24x7_open = '" + is24x7Open + '\'' + 
			"}";
		}
}