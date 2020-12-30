package com.signity.shopkeeperapp.model.verify;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StoreResponse implements Parcelable {

    public static final Creator<StoreResponse> CREATOR = new Creator<StoreResponse>() {
        @Override
        public StoreResponse createFromParcel(Parcel source) {
            return new StoreResponse(source);
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
    @SerializedName("currency_abbr")
    private String currencyCode;
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
    @SerializedName("store_url")
    private String storeUrl;
    @SerializedName("phonecode")
    private String phoneCode;
    @SerializedName("brand_id")
    private String brandId;

    public StoreResponse() {
    }

    protected StoreResponse(Parcel in) {
        this.id = in.readString();
        this.apiKey = in.readString();
        this.storeName = in.readString();
        this.location = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.country = in.readString();
        this.timezone = in.readString();
        this.zipcode = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
        this.contactPerson = in.readString();
        this.contactNumber = in.readString();
        this.contactEmail = in.readString();
        this.aboutUs = in.readString();
        this.termConditions = in.readString();
        this.privacyPolicy = in.readString();
        this.refundPolicy = in.readString();
        this.otpSkip = in.readString();
        this.version = in.readString();
        this.currency = in.readString();
        this.showCurrency = in.readString();
        this.currencyCode = in.readString();
        this.appShareLink = in.readString();
        this.androidShareLink = in.readString();
        this.iphoneShareLink = in.readString();
        this.theme = in.readString();
        this.webTheme = in.readString();
        this.pwaTheme = in.readString();
        this.type = in.readString();
        this.catType = in.readString();
        this.storeLogo = in.readString();
        this.favIcon = in.readString();
        this.appIcon = in.readString();
        this.bannerTime = in.readString();
        this.webCache = in.readString();
        this.scoMetaTitle = in.readString();
        this.scoMetaDescription = in.readString();
        this.scoMetaKeywords = in.readString();
        this.payment = in.readString();
        this.paymentEmailCount = in.readString();
        this.created = in.readString();
        this.modified = in.readString();
        this.storeUrl = in.readString();
        this.phoneCode = in.readString();
        this.brandId = in.readString();
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
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
        dest.writeString(this.id);
        dest.writeString(this.apiKey);
        dest.writeString(this.storeName);
        dest.writeString(this.location);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.country);
        dest.writeString(this.timezone);
        dest.writeString(this.zipcode);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
        dest.writeString(this.contactPerson);
        dest.writeString(this.contactNumber);
        dest.writeString(this.contactEmail);
        dest.writeString(this.aboutUs);
        dest.writeString(this.termConditions);
        dest.writeString(this.privacyPolicy);
        dest.writeString(this.refundPolicy);
        dest.writeString(this.otpSkip);
        dest.writeString(this.version);
        dest.writeString(this.currency);
        dest.writeString(this.showCurrency);
        dest.writeString(this.currencyCode);
        dest.writeString(this.appShareLink);
        dest.writeString(this.androidShareLink);
        dest.writeString(this.iphoneShareLink);
        dest.writeString(this.theme);
        dest.writeString(this.webTheme);
        dest.writeString(this.pwaTheme);
        dest.writeString(this.type);
        dest.writeString(this.catType);
        dest.writeString(this.storeLogo);
        dest.writeString(this.favIcon);
        dest.writeString(this.appIcon);
        dest.writeString(this.bannerTime);
        dest.writeString(this.webCache);
        dest.writeString(this.scoMetaTitle);
        dest.writeString(this.scoMetaDescription);
        dest.writeString(this.scoMetaKeywords);
        dest.writeString(this.payment);
        dest.writeString(this.paymentEmailCount);
        dest.writeString(this.created);
        dest.writeString(this.modified);
        dest.writeString(this.storeUrl);
        dest.writeString(this.phoneCode);
        dest.writeString(this.brandId);
    }
}