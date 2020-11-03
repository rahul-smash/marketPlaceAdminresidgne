package com.signity.shopkeeperapp.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProductData implements Parcelable {
    public static final Creator<GetProductData> CREATOR = new Creator<GetProductData>() {
        @Override
        public GetProductData createFromParcel(Parcel in) {
            return new GetProductData(in);
        }

        @Override
        public GetProductData[] newArray(int size) {
            return new GetProductData[size];
        }
    };
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("category_ids")
    @Expose
    private String categoryIds;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("nutrient")
    @Expose
    private String nutrient;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("image_type")
    @Expose
    private String imageType;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("show_price")
    @Expose
    private String showPrice;
    @SerializedName("isTaxEnable")
    @Expose
    private String isTaxEnable;
    @SerializedName("gst_tax_type")
    @Expose
    private String gstTaxType;
    @SerializedName("gst_tax_rate")
    @Expose
    private String gstTaxRate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("sort")
    @Expose
    private String sort;
    @SerializedName("is_export_from_file")
    @Expose
    private String isExportFromFile;
    @SerializedName("deleted")
    @Expose
    private Boolean deleted;
    @SerializedName("image_100_80")
    @Expose
    private String image10080;
    @SerializedName("image_300_200")
    @Expose
    private String image300200;
    @SerializedName("variants")
    @Expose
    private List<Variant> variants = null;
    @SerializedName("selectedVariant")
    @Expose
    private SelectedVariant selectedVariant;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @Expose(deserialize = false)
    private boolean isSelected;
    @Expose(deserialize = false)
    private int count;

    protected GetProductData(Parcel in) {
        id = in.readString();
        storeId = in.readString();
        categoryIds = in.readString();
        title = in.readString();
        brand = in.readString();
        nutrient = in.readString();
        description = in.readString();
        tags = in.readString();
        image = in.readString();
        imageType = in.readString();
        imageUrl = in.readString();
        showPrice = in.readString();
        isTaxEnable = in.readString();
        gstTaxType = in.readString();
        gstTaxRate = in.readString();
        status = in.readString();
        sort = in.readString();
        isExportFromFile = in.readString();
        byte tmpDeleted = in.readByte();
        deleted = tmpDeleted == 0 ? null : tmpDeleted == 1;
        image10080 = in.readString();
        image300200 = in.readString();
        variants = in.createTypedArrayList(Variant.CREATOR);
        categoryName = in.readString();
        isSelected = in.readByte() != 0;
        count = in.readInt();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

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

    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getNutrient() {
        return nutrient;
    }

    public void setNutrient(String nutrient) {
        this.nutrient = nutrient;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(String showPrice) {
        this.showPrice = showPrice;
    }

    public String getIsTaxEnable() {
        return isTaxEnable;
    }

    public void setIsTaxEnable(String isTaxEnable) {
        this.isTaxEnable = isTaxEnable;
    }

    public String getGstTaxType() {
        return gstTaxType;
    }

    public void setGstTaxType(String gstTaxType) {
        this.gstTaxType = gstTaxType;
    }

    public String getGstTaxRate() {
        return gstTaxRate;
    }

    public void setGstTaxRate(String gstTaxRate) {
        this.gstTaxRate = gstTaxRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getIsExportFromFile() {
        return isExportFromFile;
    }

    public void setIsExportFromFile(String isExportFromFile) {
        this.isExportFromFile = isExportFromFile;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getImage10080() {
        return image10080;
    }

    public void setImage10080(String image10080) {
        this.image10080 = image10080;
    }

    public String getImage300200() {
        return image300200;
    }

    public void setImage300200(String image300200) {
        this.image300200 = image300200;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    public SelectedVariant getSelectedVariant() {
        return selectedVariant;
    }

    public void setSelectedVariant(SelectedVariant selectedVariant) {
        this.selectedVariant = selectedVariant;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(storeId);
        dest.writeString(categoryIds);
        dest.writeString(title);
        dest.writeString(brand);
        dest.writeString(nutrient);
        dest.writeString(description);
        dest.writeString(tags);
        dest.writeString(image);
        dest.writeString(imageType);
        dest.writeString(imageUrl);
        dest.writeString(showPrice);
        dest.writeString(isTaxEnable);
        dest.writeString(gstTaxType);
        dest.writeString(gstTaxRate);
        dest.writeString(status);
        dest.writeString(sort);
        dest.writeString(isExportFromFile);
        dest.writeByte((byte) (deleted == null ? 0 : deleted ? 1 : 2));
        dest.writeString(image10080);
        dest.writeString(image300200);
        dest.writeTypedList(variants);
        dest.writeString(categoryName);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeInt(count);
    }
}
