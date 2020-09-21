package com.signity.shopkeeperapp.model.verify;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StoreResponse implements Parcelable {

    public static final Creator<StoreResponse> CREATOR = new Creator<StoreResponse>() {
        @Override
        public StoreResponse createFromParcel(Parcel in) {
            return new StoreResponse(in);
        }

        @Override
        public StoreResponse[] newArray(int size) {
            return new StoreResponse[size];
        }
    };
    @SerializedName("id")
    private String id;
    @SerializedName("api_key")
    private String apiKey;
    @SerializedName("store_name")
    private String storeName;
    @SerializedName("location")
    private String location;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("country")
    private String country;
    @SerializedName("timezone")
    private String timezone;
    @SerializedName("zipcode")
    private String zipcode;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lng")
    private String lng;
    @SerializedName("contact_person")
    private String contactPerson;
    @SerializedName("contact_number")
    private String contactNumber;
    @SerializedName("contact_email")
    private String contactEmail;
    @SerializedName("about_us")
    private String aboutUs;
    @SerializedName("term_conditions")
    private String termConditions;
    @SerializedName("privacy_policy")
    private String privacyPolicy;
    @SerializedName("refund_policy")
    private String refundPolicy;
    @SerializedName("otp_skip")
    private String otpSkip;
    @SerializedName("version")
    private String version;
    @SerializedName("currency")
    private String currency;
    @SerializedName("show_currency")
    private String showCurrency;
    @SerializedName("app_share_link")
    private String appShareLink;
    @SerializedName("android_share_link")
    private String androidShareLink;
    @SerializedName("iphone_share_link")
    private String iphoneShareLink;
    @SerializedName("theme")
    private String theme;
    @SerializedName("web_theme")
    private String webTheme;
    @SerializedName("pwa_theme")
    private String pwaTheme;
    @SerializedName("type")
    private String type;
    @SerializedName("cat_type")
    private String catType;
    @SerializedName("store_logo")
    private String storeLogo;
    @SerializedName("fav_icon")
    private String favIcon;
    @SerializedName("app_icon")
    private String appIcon;
    @SerializedName("banner_time")
    private String bannerTime;
    @SerializedName("web_cache")
    private String webCache;
    @SerializedName("sco_meta_title")
    private String scoMetaTitle;
    @SerializedName("sco_meta_description")
    private String scoMetaDescription;
    @SerializedName("sco_meta_keywords")
    private String scoMetaKeywords;
    @SerializedName("payment")
    private String payment;
    @SerializedName("payment_email_count")
    private String paymentEmailCount;
    @SerializedName("created")
    private String created;
    @SerializedName("modified")
    private String modified;

    protected StoreResponse(Parcel in) {
        id = in.readString();
        apiKey = in.readString();
        storeName = in.readString();
        location = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        timezone = in.readString();
        zipcode = in.readString();
        lat = in.readString();
        lng = in.readString();
        contactPerson = in.readString();
        contactNumber = in.readString();
        contactEmail = in.readString();
        aboutUs = in.readString();
        termConditions = in.readString();
        privacyPolicy = in.readString();
        refundPolicy = in.readString();
        otpSkip = in.readString();
        version = in.readString();
        currency = in.readString();
        showCurrency = in.readString();
        appShareLink = in.readString();
        androidShareLink = in.readString();
        iphoneShareLink = in.readString();
        theme = in.readString();
        webTheme = in.readString();
        pwaTheme = in.readString();
        type = in.readString();
        catType = in.readString();
        storeLogo = in.readString();
        favIcon = in.readString();
        appIcon = in.readString();
        bannerTime = in.readString();
        webCache = in.readString();
        scoMetaTitle = in.readString();
        scoMetaDescription = in.readString();
        scoMetaKeywords = in.readString();
        payment = in.readString();
        paymentEmailCount = in.readString();
        created = in.readString();
        modified = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public String getTermConditions() {
        return termConditions;
    }

    public void setTermConditions(String termConditions) {
        this.termConditions = termConditions;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    public String getRefundPolicy() {
        return refundPolicy;
    }

    public void setRefundPolicy(String refundPolicy) {
        this.refundPolicy = refundPolicy;
    }

    public String getOtpSkip() {
        return otpSkip;
    }

    public void setOtpSkip(String otpSkip) {
        this.otpSkip = otpSkip;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getShowCurrency() {
        return showCurrency;
    }

    public void setShowCurrency(String showCurrency) {
        this.showCurrency = showCurrency;
    }

    public String getAppShareLink() {
        return appShareLink;
    }

    public void setAppShareLink(String appShareLink) {
        this.appShareLink = appShareLink;
    }

    public String getAndroidShareLink() {
        return androidShareLink;
    }

    public void setAndroidShareLink(String androidShareLink) {
        this.androidShareLink = androidShareLink;
    }

    public String getIphoneShareLink() {
        return iphoneShareLink;
    }

    public void setIphoneShareLink(String iphoneShareLink) {
        this.iphoneShareLink = iphoneShareLink;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getWebTheme() {
        return webTheme;
    }

    public void setWebTheme(String webTheme) {
        this.webTheme = webTheme;
    }

    public String getPwaTheme() {
        return pwaTheme;
    }

    public void setPwaTheme(String pwaTheme) {
        this.pwaTheme = pwaTheme;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCatType() {
        return catType;
    }

    public void setCatType(String catType) {
        this.catType = catType;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getFavIcon() {
        return favIcon;
    }

    public void setFavIcon(String favIcon) {
        this.favIcon = favIcon;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getBannerTime() {
        return bannerTime;
    }

    public void setBannerTime(String bannerTime) {
        this.bannerTime = bannerTime;
    }

    public String getWebCache() {
        return webCache;
    }

    public void setWebCache(String webCache) {
        this.webCache = webCache;
    }

    public String getScoMetaTitle() {
        return scoMetaTitle;
    }

    public void setScoMetaTitle(String scoMetaTitle) {
        this.scoMetaTitle = scoMetaTitle;
    }

    public String getScoMetaDescription() {
        return scoMetaDescription;
    }

    public void setScoMetaDescription(String scoMetaDescription) {
        this.scoMetaDescription = scoMetaDescription;
    }

    public String getScoMetaKeywords() {
        return scoMetaKeywords;
    }

    public void setScoMetaKeywords(String scoMetaKeywords) {
        this.scoMetaKeywords = scoMetaKeywords;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPaymentEmailCount() {
        return paymentEmailCount;
    }

    public void setPaymentEmailCount(String paymentEmailCount) {
        this.paymentEmailCount = paymentEmailCount;
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

    @Override
    public String toString() {
        return
                "StoreResponse{" +
                        "id = '" + id + '\'' +
                        ",api_key = '" + apiKey + '\'' +
                        ",store_name = '" + storeName + '\'' +
                        ",location = '" + location + '\'' +
                        ",city = '" + city + '\'' +
                        ",state = '" + state + '\'' +
                        ",country = '" + country + '\'' +
                        ",timezone = '" + timezone + '\'' +
                        ",zipcode = '" + zipcode + '\'' +
                        ",lat = '" + lat + '\'' +
                        ",lng = '" + lng + '\'' +
                        ",contact_person = '" + contactPerson + '\'' +
                        ",contact_number = '" + contactNumber + '\'' +
                        ",contact_email = '" + contactEmail + '\'' +
                        ",about_us = '" + aboutUs + '\'' +
                        ",term_conditions = '" + termConditions + '\'' +
                        ",privacy_policy = '" + privacyPolicy + '\'' +
                        ",refund_policy = '" + refundPolicy + '\'' +
                        ",otp_skip = '" + otpSkip + '\'' +
                        ",version = '" + version + '\'' +
                        ",currency = '" + currency + '\'' +
                        ",show_currency = '" + showCurrency + '\'' +
                        ",app_share_link = '" + appShareLink + '\'' +
                        ",android_share_link = '" + androidShareLink + '\'' +
                        ",iphone_share_link = '" + iphoneShareLink + '\'' +
                        ",theme = '" + theme + '\'' +
                        ",web_theme = '" + webTheme + '\'' +
                        ",pwa_theme = '" + pwaTheme + '\'' +
                        ",type = '" + type + '\'' +
                        ",cat_type = '" + catType + '\'' +
                        ",store_logo = '" + storeLogo + '\'' +
                        ",fav_icon = '" + favIcon + '\'' +
                        ",app_icon = '" + appIcon + '\'' +
                        ",banner_time = '" + bannerTime + '\'' +
                        ",web_cache = '" + webCache + '\'' +
                        ",sco_meta_title = '" + scoMetaTitle + '\'' +
                        ",sco_meta_description = '" + scoMetaDescription + '\'' +
                        ",sco_meta_keywords = '" + scoMetaKeywords + '\'' +
                        ",payment = '" + payment + '\'' +
                        ",payment_email_count = '" + paymentEmailCount + '\'' +
                        ",created = '" + created + '\'' +
                        ",modified = '" + modified + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(apiKey);
        dest.writeString(storeName);
        dest.writeString(location);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(timezone);
        dest.writeString(zipcode);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(contactPerson);
        dest.writeString(contactNumber);
        dest.writeString(contactEmail);
        dest.writeString(aboutUs);
        dest.writeString(termConditions);
        dest.writeString(privacyPolicy);
        dest.writeString(refundPolicy);
        dest.writeString(otpSkip);
        dest.writeString(version);
        dest.writeString(currency);
        dest.writeString(showCurrency);
        dest.writeString(appShareLink);
        dest.writeString(androidShareLink);
        dest.writeString(iphoneShareLink);
        dest.writeString(theme);
        dest.writeString(webTheme);
        dest.writeString(pwaTheme);
        dest.writeString(type);
        dest.writeString(catType);
        dest.writeString(storeLogo);
        dest.writeString(favIcon);
        dest.writeString(appIcon);
        dest.writeString(bannerTime);
        dest.writeString(webCache);
        dest.writeString(scoMetaTitle);
        dest.writeString(scoMetaDescription);
        dest.writeString(scoMetaKeywords);
        dest.writeString(payment);
        dest.writeString(paymentEmailCount);
        dest.writeString(created);
        dest.writeString(modified);
    }
}