package com.signity.shopkeeperapp.model.helpMediaModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoThumb {

    @SerializedName("created_at")
    private String createdAt;
    @Expose
    private String ext;
    @Expose
    private String hash;
    @Expose
    private Long id;
    @Expose
    private String mime;
    @Expose
    private String name;
    @Expose
    private String provider;
    @SerializedName("provider_metadata")
    private Object providerMetadata;
    @Expose
    private String sha256;
    @Expose
    private Double size;
    @SerializedName("updated_at")
    private String updatedAt;
    @Expose
    private String url;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Object getProviderMetadata() {
        return providerMetadata;
    }

    public void setProviderMetadata(Object providerMetadata) {
        this.providerMetadata = providerMetadata;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
