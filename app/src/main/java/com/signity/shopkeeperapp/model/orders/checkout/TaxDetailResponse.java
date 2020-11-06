package com.signity.shopkeeperapp.model.orders.checkout;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class TaxDetailResponse implements Serializable {

	@SerializedName("label")
	private String label;

	@SerializedName("rate")
	private String rate;

	@SerializedName("tax")
	private String tax;

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

	public void setTax(String tax){
		this.tax = tax;
	}

	public String getTax(){
		return tax;
	}

	@Override
 	public String toString(){
		return 
			"TaxDetailResponse{" + 
			"label = '" + label + '\'' + 
			",rate = '" + rate + '\'' + 
			",tax = '" + tax + '\'' + 
			"}";
		}
}