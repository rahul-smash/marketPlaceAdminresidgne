
package com.signity.shopkeeperapp.dashboard.orders.printSetting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreTimeSetting {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("is24x7_open")
    @Expose
    private String is24x7Open;
    @SerializedName("openhours_from")
    @Expose
    private String openhoursFrom;
    @SerializedName("openhours_to")
    @Expose
    private String openhoursTo;
    @SerializedName("slotOne_from")
    @Expose
    private String slotOneFrom;
    @SerializedName("slotOne_to")
    @Expose
    private String slotOneTo;
    @SerializedName("slottwo_from")
    @Expose
    private String slottwoFrom;
    @SerializedName("slottwo_to")
    @Expose
    private String slottwoTo;
    @SerializedName("slotthree_from")
    @Expose
    private String slotthreeFrom;
    @SerializedName("slotthree_to")
    @Expose
    private String slotthreeTo;
    @SerializedName("dynamic_slot_from")
    @Expose
    private String dynamicSlotFrom;
    @SerializedName("dynamic_slot_to")
    @Expose
    private String dynamicSlotTo;
    @SerializedName("closehours_message")
    @Expose
    private String closehoursMessage;
    @SerializedName("store_open_days")
    @Expose
    private String storeOpenDays;
    @SerializedName("time_zone")
    @Expose
    private String timeZone;
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

    public String getIs24x7Open() {
        return is24x7Open;
    }

    public void setIs24x7Open(String is24x7Open) {
        this.is24x7Open = is24x7Open;
    }

    public String getOpenhoursFrom() {
        return openhoursFrom;
    }

    public void setOpenhoursFrom(String openhoursFrom) {
        this.openhoursFrom = openhoursFrom;
    }

    public String getOpenhoursTo() {
        return openhoursTo;
    }

    public void setOpenhoursTo(String openhoursTo) {
        this.openhoursTo = openhoursTo;
    }

    public String getSlotOneFrom() {
        return slotOneFrom;
    }

    public void setSlotOneFrom(String slotOneFrom) {
        this.slotOneFrom = slotOneFrom;
    }

    public String getSlotOneTo() {
        return slotOneTo;
    }

    public void setSlotOneTo(String slotOneTo) {
        this.slotOneTo = slotOneTo;
    }

    public String getSlottwoFrom() {
        return slottwoFrom;
    }

    public void setSlottwoFrom(String slottwoFrom) {
        this.slottwoFrom = slottwoFrom;
    }

    public String getSlottwoTo() {
        return slottwoTo;
    }

    public void setSlottwoTo(String slottwoTo) {
        this.slottwoTo = slottwoTo;
    }

    public String getSlotthreeFrom() {
        return slotthreeFrom;
    }

    public void setSlotthreeFrom(String slotthreeFrom) {
        this.slotthreeFrom = slotthreeFrom;
    }

    public String getSlotthreeTo() {
        return slotthreeTo;
    }

    public void setSlotthreeTo(String slotthreeTo) {
        this.slotthreeTo = slotthreeTo;
    }

    public String getDynamicSlotFrom() {
        return dynamicSlotFrom;
    }

    public void setDynamicSlotFrom(String dynamicSlotFrom) {
        this.dynamicSlotFrom = dynamicSlotFrom;
    }

    public String getDynamicSlotTo() {
        return dynamicSlotTo;
    }

    public void setDynamicSlotTo(String dynamicSlotTo) {
        this.dynamicSlotTo = dynamicSlotTo;
    }

    public String getClosehoursMessage() {
        return closehoursMessage;
    }

    public void setClosehoursMessage(String closehoursMessage) {
        this.closehoursMessage = closehoursMessage;
    }

    public String getStoreOpenDays() {
        return storeOpenDays;
    }

    public void setStoreOpenDays(String storeOpenDays) {
        this.storeOpenDays = storeOpenDays;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
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
