package com.signity.shopkeeperapp.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class DynamicField implements Parcelable {

    public static final Creator<DynamicField> CREATOR = new Creator<DynamicField>() {
        @Override
        public DynamicField createFromParcel(Parcel in) {
            return new DynamicField(in);
        }

        @Override
        public DynamicField[] newArray(int size) {
            return new DynamicField[size];
        }
    };
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
    @SerializedName("value")
    private Map<String, String> value;

    protected DynamicField(Parcel in) {
        label = in.readString();
        variantFieldName = in.readString();
        validation = in.readString();
        type = in.readString();
        min = in.readString();
    }

    public Map<String, String> getValue() {
        return value;
    }

    public void setValue(Map<String, String> value) {
        this.value = value;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(variantFieldName);
        dest.writeString(validation);
        dest.writeString(type);
        dest.writeString(min);
    }
}