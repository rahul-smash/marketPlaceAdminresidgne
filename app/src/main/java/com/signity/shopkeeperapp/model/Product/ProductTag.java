package com.signity.shopkeeperapp.model.Product;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ProductTag implements Serializable {

	@SerializedName("name")
	private String name;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	@Override
 	public String toString(){
		return 
			"ProductTag{" + 
			"name = '" + name + '\'' + 
			"}";
		}
}