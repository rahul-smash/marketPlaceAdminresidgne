package com.signity.shopkeeperapp.model.orders.checkout;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class TaxLabelResponse implements Serializable {

	@SerializedName("label")
	private String label;

	@SerializedName("rate")
	private String rate;

	public void setLabel(String label){
		this.label = label;
	}

	public String getLabel(){
		return label;
	}

	public void setRate(String rate){
		this.rate = rate;
	}

	public String getRate(){
		return rate;
	}

	@Override
 	public String toString(){
		return 
			"TaxLabelResponse{" + 
			"label = '" + label + '\'' + 
			",rate = '" + rate + '\'' + 
			"}";
		}
}