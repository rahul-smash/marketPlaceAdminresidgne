package com.signity.shopkeeperapp.model.market;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SharePermissionResponse implements Serializable {

    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("data")
    private Data data;
    @Expose
    @SerializedName("isShare")
    private boolean isshare;
    @Expose
    @SerializedName("status")
    private boolean status;

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

    public boolean getIsshare() {
        return isshare;
    }

    public void setIsshare(boolean isshare) {
        this.isshare = isshare;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class Data {
        @Expose
        @SerializedName("monthy_video_count")
        private int monthyVideoCount;
        @Expose
        @SerializedName("monthy_frame_count")
        private int monthyFrameCount;
        @Expose
        @SerializedName("monthy_creative_count")
        private int monthyCreativeCount;
        @Expose
        @SerializedName("daily_video_count")
        private int dailyVideoCount;
        @Expose
        @SerializedName("daily_custom_creative_count")
        private int dailyFrameCount;
        @Expose
        @SerializedName("daily_creative_count")
        private int dailyCreativeCount;
        @Expose
        @SerializedName("id")
        private int id;

        public int getMonthyVideoCount() {
            return monthyVideoCount;
        }

        public void setMonthyVideoCount(int monthyVideoCount) {
            this.monthyVideoCount = monthyVideoCount;
        }

        public int getMonthyFrameCount() {
            return monthyFrameCount;
        }

        public void setMonthyFrameCount(int monthyFrameCount) {
            this.monthyFrameCount = monthyFrameCount;
        }

        public int getMonthyCreativeCount() {
            return monthyCreativeCount;
        }

        public void setMonthyCreativeCount(int monthyCreativeCount) {
            this.monthyCreativeCount = monthyCreativeCount;
        }

        public int getDailyVideoCount() {
            return dailyVideoCount;
        }

        public void setDailyVideoCount(int dailyVideoCount) {
            this.dailyVideoCount = dailyVideoCount;
        }

        public int getDailyFrameCount() {
            return dailyFrameCount;
        }

        public void setDailyFrameCount(int dailyFrameCount) {
            this.dailyFrameCount = dailyFrameCount;
        }

        public int getDailyCreativeCount() {
            return dailyCreativeCount;
        }

        public void setDailyCreativeCount(int dailyCreativeCount) {
            this.dailyCreativeCount = dailyCreativeCount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}