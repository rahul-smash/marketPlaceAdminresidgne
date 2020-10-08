package com.signity.shopkeeperapp.model.Product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DynamicField implements Serializable {

    @SerializedName("label")
    private String label;

    @SerializedName("variant_field_name")
    private String variantFieldName;

    @SerializedName("validation")
    private String validation;

    @SerializedName("type")
    private String type;

    @SerializedName("min")
    private String min;

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getVariantFieldName() {
        return variantFieldName;
    }

    public void setVariantFieldName(String variantFieldName) {
        this.variantFieldName = variantFieldName;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DataItem{" +
                "label='" + label + '\'' +
                ", variantFieldName='" + variantFieldName + '\'' +
                ", validation='" + validation + '\'' +
                ", type='" + type + '\'' +
                ", min='" + min + '\'' +
                '}';
    }
}