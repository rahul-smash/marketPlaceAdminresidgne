package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajesh on 16/10/15.
 */
public class ItemListModel {

    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cid")
    @Expose
    private String cid;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("unit_type")
    @Expose
    private String unitType;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("image_100_80")
    @Expose
    private String imageSmall;
    @SerializedName("image_300_200")
    @Expose
    private String imageMedium;
    @SerializedName("mrp_price")
    @Expose
    private Double mrpPrice;
    @SerializedName("price")
    @Expose
    private Double price;


    public boolean isChecked = true;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageSmall() {
        return imageSmall;
    }

    public void setImageSmall(String imageSmall) {
        this.imageSmall = imageSmall;
    }

    public String getImageMedium() {
        return imageMedium;
    }

    public void setImageMedium(String imageMedium) {
        this.imageMedium = imageMedium;
    }

    /**
     * @return The pid
     */
    public String getPid() {
        return pid;
    }

    /**
     * @param pid The pid
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The cid
     */
    public String getCid() {
        return cid;
    }

    /**
     * @param cid The cid
     */
    public void setCid(String cid) {
        this.cid = cid;
    }

    /**
     * @return The quantity
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * @param quantity The quantity
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The mrpPrice
     */
    public Double getMrpPrice() {
        return mrpPrice;
    }

    /**
     * @param mrpPrice The mrp_price
     */
    public void setMrpPrice(Double mrpPrice) {
        this.mrpPrice = mrpPrice;
    }

    /**
     * @return The price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price The price
     */
    public void setPrice(Double price) {
        this.price = price;
    }


    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }
}
