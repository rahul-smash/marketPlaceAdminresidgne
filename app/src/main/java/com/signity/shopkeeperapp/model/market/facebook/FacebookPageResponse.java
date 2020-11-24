package com.signity.shopkeeperapp.model.market.facebook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacebookPageResponse {

    @SerializedName("facebook_page")
    private String facebookPage;
    @Expose
    private String message;
    @Expose
    private Boolean status;

    public String getFacebookPage() {
        return facebookPage;
    }

    public void setFacebookPage(String facebookPage) {
        this.facebookPage = facebookPage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
