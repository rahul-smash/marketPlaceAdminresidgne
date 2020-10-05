package com.signity.shopkeeperapp.model.category;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddCategoryResponse implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSuccess() {
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

    @Override
    public String toString() {
        return
                "AddCategoryResponse{" +
                        "success = '" + success + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }

    static class Data {
        @SerializedName("catid")
        private String categoryId;

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }
    }
}