package com.signity.shopkeeperapp.model.Product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StoreAttributes implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return
                "StoreAttributes{" +
                        "success = '" + success + '\'' +
                        ",message = '" + message + '\'' +
                        ",data = '" + data + '\'' +
                        "}";
    }

    public class Data {
        @SerializedName("attributes")
        private List<DynamicField> dynamicFields;

        @SerializedName("tagList")
        private List<ProductTag> productTags;

        public List<DynamicField> getDynamicFields() {
            return dynamicFields;
        }

        public void setDynamicFields(List<DynamicField> dynamicFields) {
            this.dynamicFields = dynamicFields;
        }

        public List<ProductTag> getProductTags() {
            return productTags;
        }

        public void setProductTags(List<ProductTag> productTags) {
            this.productTags = productTags;
        }
    }
}