package com.signity.shopkeeperapp.model.orders.checkout;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DataResponse implements Serializable {

	@SerializedName("total")
	private String total;

	@SerializedName("wallet_refund")
	private String walletRefund;

	@SerializedName("item_sub_total")
	private String itemSubTotal;

	@SerializedName("tax")
	private String tax;

	@SerializedName("discount")
	private String discount;

	@SerializedName("shipping")
	private String shipping;

	@SerializedName("fixed_tax_amount")
	private String fixedTaxAmount;

	@SerializedName("tax_detail")
	private List<TaxDetailResponse> taxDetail;

	@SerializedName("tax_label")
	private List<TaxLabelResponse> taxLabel;

	@SerializedName("fixed_Tax")
	private List<Object> fixedTax;

	@SerializedName("order_detail")
	private List<OrderDetailResponse> orderDetail;

	@SerializedName("is_changed")
	private boolean isChanged;

	public void setTotal(String total){
		this.total = total;
	}

	public String getTotal(){
		return total;
	}

	public void setWalletRefund(String walletRefund){
		this.walletRefund = walletRefund;
	}

	public String getWalletRefund(){
		return walletRefund;
	}

	public void setItemSubTotal(String itemSubTotal){
		this.itemSubTotal = itemSubTotal;
	}

	public String getItemSubTotal(){
		return itemSubTotal;
	}

	public void setTax(String tax){
		this.tax = tax;
	}

	public String getTax(){
		return tax;
	}

	public void setDiscount(String discount){
		this.discount = discount;
	}

	public String getDiscount(){
		return discount;
	}

	public void setShipping(String shipping){
		this.shipping = shipping;
	}

	public String getShipping(){
		return shipping;
	}

	public void setFixedTaxAmount(String fixedTaxAmount){
		this.fixedTaxAmount = fixedTaxAmount;
	}

	public String getFixedTaxAmount(){
		return fixedTaxAmount;
	}

	public void setTaxDetail(List<TaxDetailResponse> taxDetail){
		this.taxDetail = taxDetail;
	}

	public List<TaxDetailResponse> getTaxDetail(){
		return taxDetail;
	}

	public void setTaxLabel(List<TaxLabelResponse> taxLabel){
		this.taxLabel = taxLabel;
	}

	public List<TaxLabelResponse> getTaxLabel(){
		return taxLabel;
	}

	public void setFixedTax(List<Object> fixedTax){
		this.fixedTax = fixedTax;
	}

	public List<Object> getFixedTax(){
		return fixedTax;
	}

	public void setOrderDetail(List<OrderDetailResponse> orderDetail){
		this.orderDetail = orderDetail;
	}

	public List<OrderDetailResponse> getOrderDetail(){
		return orderDetail;
	}

	public void setIsChanged(boolean isChanged){
		this.isChanged = isChanged;
	}

	public boolean isIsChanged(){
		return isChanged;
	}

	@Override
 	public String toString(){
		return 
			"DataResponse{" + 
			"total = '" + total + '\'' + 
			",wallet_refund = '" + walletRefund + '\'' + 
			",item_sub_total = '" + itemSubTotal + '\'' + 
			",tax = '" + tax + '\'' + 
			",discount = '" + discount + '\'' + 
			",shipping = '" + shipping + '\'' + 
			",fixed_tax_amount = '" + fixedTaxAmount + '\'' + 
			",tax_detail = '" + taxDetail + '\'' + 
			",tax_label = '" + taxLabel + '\'' + 
			",fixed_Tax = '" + fixedTax + '\'' + 
			",order_detail = '" + orderDetail + '\'' + 
			",is_changed = '" + isChanged + '\'' + 
			"}";
		}
}