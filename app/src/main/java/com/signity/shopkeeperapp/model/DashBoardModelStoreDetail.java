package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajesh on 30/10/15.
 */
public class DashBoardModelStoreDetail {

    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("paid")
    @Expose
    private String paid;
    @SerializedName("subscribed_plan")
    @Expose
    private String subscribedPlan;
    @SerializedName("next_subscribtion_date")
    @Expose
    private String nextSubscribtionDate;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("about_us")
    @Expose
    private String aboutUs;
    @SerializedName("store_status")
    @Expose
    private String storeStatus;
    @SerializedName("store_msg")
    @Expose
    private String storeMessage;
    @SerializedName("android_app_share")
    @Expose
    private String androidAppShare;

    public String getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(String storeStatus) {
        this.storeStatus = storeStatus;
    }

    /**
     * @return The storeName
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * @param storeName The store_name
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    /**
     * @return The city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return The contactNumber
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * @param contactNumber The contact_number
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * @return The paid
     */
    public String getPaid() {
        return paid;
    }

    /**
     * @param paid The paid
     */
    public void setPaid(String paid) {
        this.paid = paid;
    }

    /**
     * @return The subscribedPlan
     */
    public String getSubscribedPlan() {
        return subscribedPlan;
    }

    /**
     * @param subscribedPlan The subscribed_plan
     */
    public void setSubscribedPlan(String subscribedPlan) {
        this.subscribedPlan = subscribedPlan;
    }

    /**
     * @return The nextSubscribtionDate
     */
    public String getNextSubscribtionDate() {
        return nextSubscribtionDate;
    }

    /**
     * @param nextSubscribtionDate The next_subscribtion_date
     */
    public void setNextSubscribtionDate(String nextSubscribtionDate) {
        this.nextSubscribtionDate = nextSubscribtionDate;
    }

    /**
     * @return The banner
     */
    public String getBanner() {
        return banner;
    }

    /**
     * @param banner The banner
     */
    public void setBanner(String banner) {
        this.banner = banner;
    }

    /**
     * @return The aboutUs
     */
    public String getAboutUs() {
        return aboutUs;
    }

    /**
     * @param aboutUs The about_us
     */
    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    /**
     * @return The androidAppShare
     */
    public String getAndroidAppShare() {
        return androidAppShare;
    }

    /**
     * @param androidAppShare The android_app_share
     */
    public void setAndroidAppShare(String androidAppShare) {
        this.androidAppShare = androidAppShare;
    }

    public String getStoreMessage() {
        return storeMessage;
    }

    public void setStoreMessage(String storeMessage) {
        this.storeMessage = storeMessage;
    }
}
