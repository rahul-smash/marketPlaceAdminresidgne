package com.signity.shopkeeperapp.model.dashboard;

import com.google.gson.annotations.SerializedName;

public class Data {

	@SerializedName("message")
	private String message;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("action")
	private String action;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("message_type")
	private String messageType;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	@SerializedName("status")
	private String status;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setAction(String action){
		this.action = action;
	}

	public String getAction(){
		return action;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setMessageType(String messageType){
		this.messageType = messageType;
	}

	public String getMessageType(){
		return messageType;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"message = '" + message + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",action = '" + action + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",message_type = '" + messageType + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}