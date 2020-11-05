package com.signity.shopkeeperapp.model.customers.addCustomer;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class StoreUserResponse implements Serializable {

	@SerializedName("user_id")
	private String userId;

	@SerializedName("store_id")
	private String storeId;

	@SerializedName("role_id")
	private String roleId;

	@SerializedName("modified")
	private String modified;

	@SerializedName("created")
	private String created;

	@SerializedName("id")
	private String id;

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setStoreId(String storeId){
		this.storeId = storeId;
	}

	public String getStoreId(){
		return storeId;
	}

	public void setRoleId(String roleId){
		this.roleId = roleId;
	}

	public String getRoleId(){
		return roleId;
	}

	public void setModified(String modified){
		this.modified = modified;
	}

	public String getModified(){
		return modified;
	}

	public void setCreated(String created){
		this.created = created;
	}

	public String getCreated(){
		return created;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"StoreUserResponse{" + 
			"user_id = '" + userId + '\'' + 
			",store_id = '" + storeId + '\'' + 
			",role_id = '" + roleId + '\'' + 
			",modified = '" + modified + '\'' + 
			",created = '" + created + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}