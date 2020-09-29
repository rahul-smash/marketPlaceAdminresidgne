package com.signity.shopkeeperapp.model.notification;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DataResponse implements Serializable {

	@SerializedName("total")
	private int total;

	@SerializedName("page")
	private int page;

	@SerializedName("pagelength")
	private int pagelength;

	@SerializedName("notification")
	private List<NotificationResponse> notification;

	public void setTotal(int total){
		this.total = total;
	}

	public int getTotal(){
		return total;
	}

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
	}

	public void setPagelength(int pagelength){
		this.pagelength = pagelength;
	}

	public int getPagelength(){
		return pagelength;
	}

	public void setNotification(List<NotificationResponse> notification){
		this.notification = notification;
	}

	public List<NotificationResponse> getNotification(){
		return notification;
	}

	@Override
 	public String toString(){
		return 
			"DataResponse{" + 
			"total = '" + total + '\'' + 
			",page = '" + page + '\'' + 
			",pagelength = '" + pagelength + '\'' + 
			",notification = '" + notification + '\'' + 
			"}";
		}
}