package com.signity.shopkeeperapp.model.market.industry;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Industrytype implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("status")
    private boolean status;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return
                "Industrytype{" +
                        "id = '" + id + '\'' +
                        ",name = '" + name + '\'' +
                        ",status = '" + status + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",updated_at = '" + updatedAt + '\'' +
                        "}";
    }
}