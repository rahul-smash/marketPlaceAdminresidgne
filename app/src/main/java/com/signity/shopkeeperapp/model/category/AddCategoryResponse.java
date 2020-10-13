package com.signity.shopkeeperapp.model.category;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddCategoryResponse implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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
        return "AddCategoryResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Data {
        @SerializedName("catid")
        private String categoryId;

        @SerializedName("parent_id")
        private String parentId;

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "categoryId='" + categoryId + '\'' +
                    ", parentId='" + parentId + '\'' +
                    '}';
        }
    }
}