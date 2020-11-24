package com.signity.shopkeeperapp.model.market.facebook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacebookPageRequest {

    @Expose
    private String brand;
    @SerializedName("page_access_token")
    private String pageAccessToken;
    @SerializedName("page_id")
    private String pageId;
    @SerializedName("page_name")
    private String pageName;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPageAccessToken() {
        return pageAccessToken;
    }

    public void setPageAccessToken(String pageAccessToken) {
        this.pageAccessToken = pageAccessToken;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

}
