package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rajesh on 30/6/16.
 */
public class StoreTaxModel implements Serializable {

    @SerializedName("fixed_tax_amount")
    private String fixedTaxAmount;
    @SerializedName("fixed_tax_label")
    private String fixedTaxLabel;
    @SerializedName("is_discount_applicable")
    private String isDiscountApplicable;
    @SerializedName("is_tax_enable")
    private String isTaxEnable;


    public String getFixedTaxAmount() {
        return fixedTaxAmount;
    }

    public void setFixedTaxAmount(String fixedTaxAmount) {
        this.fixedTaxAmount = fixedTaxAmount;
    }

    public String getFixedTaxLabel() {
        return fixedTaxLabel;
    }

    public void setFixedTaxLabel(String fixedTaxLabel) {
        this.fixedTaxLabel = fixedTaxLabel;
    }

    public String getIsDiscountApplicable() {
        return isDiscountApplicable;
    }

    public void setIsDiscountApplicable(String isDiscountApplicable) {
        this.isDiscountApplicable = isDiscountApplicable;
    }

    public String getIsTaxEnable() {
        return isTaxEnable;
    }

    public void setIsTaxEnable(String isTaxEnable) {
        this.isTaxEnable = isTaxEnable;
    }
}
