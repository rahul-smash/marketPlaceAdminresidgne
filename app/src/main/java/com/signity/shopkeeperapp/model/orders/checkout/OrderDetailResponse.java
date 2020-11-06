package com.signity.shopkeeperapp.model.orders.checkout;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class OrderDetailResponse implements Serializable {

	@SerializedName("discount")
	private String discount;

	@SerializedName("isTaxEnable")
	private String isTaxEnable;

	@SerializedName("mrp_price")
	private String mrpPrice;

	@SerializedName("price")
	private String price;

	@SerializedName("product_id")
	private String productId;

	@SerializedName("product_name")
	private String productName;

	@SerializedName("product_type")
	private int productType;

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("unit_type")
	private String unitType;

	@SerializedName("variant_id")
	private String variantId;

	@SerializedName("weight")
	private String weight;

	@SerializedName("product_status")
	private String productStatus;

	@SerializedName("hsn_code")
	private String hsnCode;

	@SerializedName("gst_tax_rate")
	private String gstTaxRate;

	public void setDiscount(String discount){
		this.discount = discount;
	}

	public String getDiscount(){
		return discount;
	}

	public void setIsTaxEnable(String isTaxEnable){
		this.isTaxEnable = isTaxEnable;
	}

	public String getIsTaxEnable(){
		return isTaxEnable;
	}

	public void setMrpPrice(String mrpPrice){
		this.mrpPrice = mrpPrice;
	}

	public String getMrpPrice(){
		return mrpPrice;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setProductId(String productId){
		this.productId = productId;
	}

	public String getProductId(){
		return productId;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setProductType(int productType){
		this.productType = productType;
	}

	public int getProductType(){
		return productType;
	}

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setUnitType(String unitType){
		this.unitType = unitType;
	}

	public String getUnitType(){
		return unitType;
	}

	public void setVariantId(String variantId){
		this.variantId = variantId;
	}

	public String getVariantId(){
		return variantId;
	}

	public void setWeight(String weight){
		this.weight = weight;
	}

	public String getWeight(){
		return weight;
	}

	public void setProductStatus(String productStatus){
		this.productStatus = productStatus;
	}

	public String getProductStatus(){
		return productStatus;
	}

	public void setHsnCode(String hsnCode){
		this.hsnCode = hsnCode;
	}

	public String getHsnCode(){
		return hsnCode;
	}

	public void setGstTaxRate(String gstTaxRate){
		this.gstTaxRate = gstTaxRate;
	}

	public String getGstTaxRate(){
		return gstTaxRate;
	}

	@Override
 	public String toString(){
		return 
			"OrderDetailResponse{" + 
			"discount = '" + discount + '\'' + 
			",isTaxEnable = '" + isTaxEnable + '\'' + 
			",mrp_price = '" + mrpPrice + '\'' + 
			",price = '" + price + '\'' + 
			",product_id = '" + productId + '\'' + 
			",product_name = '" + productName + '\'' + 
			",product_type = '" + productType + '\'' + 
			",quantity = '" + quantity + '\'' + 
			",unit_type = '" + unitType + '\'' + 
			",variant_id = '" + variantId + '\'' + 
			",weight = '" + weight + '\'' + 
			",product_status = '" + productStatus + '\'' + 
			",hsn_code = '" + hsnCode + '\'' + 
			",gst_tax_rate = '" + gstTaxRate + '\'' + 
			"}";
		}
}