package com.signity.shopkeeperapp.model.customers;

import com.google.gson.annotations.Expose;

import retrofit.http.Body;

public class CustomerDataResponse {

    @Expose
    private boolean success;

    @Expose
    private String message;

    @Expose
    private DataResponse data;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataResponse getData() {
        return data;
    }

    public void setData(DataResponse data) {
        this.data = data;
    }
}
