
package com.signity.shopkeeperapp.dashboard.orders.printSetting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrintOrderSetting {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("is_gst_on")
    @Expose
    private String isGstOn;
    @SerializedName("gst")
    @Expose
    private String gst;
    @SerializedName("is_tan_on")
    @Expose
    private String isTanOn;
    @SerializedName("tan")
    @Expose
    private String tan;
    @SerializedName("is_fssi_on")
    @Expose
    private String isFssiOn;
    @SerializedName("fssi")
    @Expose
    private String fssi;
    @SerializedName("is_logo_on")
    @Expose
    private String isLogoOn;
    @SerializedName("is_website_on")
    @Expose
    private String isWebsiteOn;
    @SerializedName("is_url_on")
    @Expose
    private String isUrlOn;
    @SerializedName("is_address")
    @Expose
    private String isAddress;
    @SerializedName("qr_code_img")
    @Expose
    private String qrCodeImg;
    @SerializedName("free_text_heading")
    @Expose
    private String freeTextHeading;
    @SerializedName("free_text_content")
    @Expose
    private String freeTextContent;
    @SerializedName("upi_code")
    @Expose
    private String upiCode;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getIsGstOn() {
        return isGstOn;
    }

    public void setIsGstOn(String isGstOn) {
        this.isGstOn = isGstOn;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getIsTanOn() {
        return isTanOn;
    }

    public void setIsTanOn(String isTanOn) {
        this.isTanOn = isTanOn;
    }

    public String getTan() {
        return tan;
    }

    public void setTan(String tan) {
        this.tan = tan;
    }

    public String getIsFssiOn() {
        return isFssiOn;
    }

    public void setIsFssiOn(String isFssiOn) {
        this.isFssiOn = isFssiOn;
    }

    public String getFssi() {
        return fssi;
    }

    public void setFssi(String fssi) {
        this.fssi = fssi;
    }

    public String getIsLogoOn() {
        return isLogoOn;
    }

    public void setIsLogoOn(String isLogoOn) {
        this.isLogoOn = isLogoOn;
    }

    public String getIsWebsiteOn() {
        return isWebsiteOn;
    }

    public void setIsWebsiteOn(String isWebsiteOn) {
        this.isWebsiteOn = isWebsiteOn;
    }

    public String getIsUrlOn() {
        return isUrlOn;
    }

    public void setIsUrlOn(String isUrlOn) {
        this.isUrlOn = isUrlOn;
    }

    public String getIsAddress() {
        return isAddress;
    }

    public void setIsAddress(String isAddress) {
        this.isAddress = isAddress;
    }

    public String getQrCodeImg() {
        return qrCodeImg;
    }

    public void setQrCodeImg(String qrCodeImg) {
        this.qrCodeImg = qrCodeImg;
    }

    public String getFreeTextHeading() {
        return freeTextHeading;
    }

    public void setFreeTextHeading(String freeTextHeading) {
        this.freeTextHeading = freeTextHeading;
    }

    public String getFreeTextContent() {
        return freeTextContent;
    }

    public void setFreeTextContent(String freeTextContent) {
        this.freeTextContent = freeTextContent;
    }

    public String getUpiCode() {
        return upiCode;
    }

    public void setUpiCode(String upiCode) {
        this.upiCode = upiCode;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

}
