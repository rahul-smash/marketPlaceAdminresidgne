package com.signity.shopkeeperapp.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProductData implements Parcelable {
    public static final Creator<GetProductData> CREATOR = new Creator<GetProductData>() {
        @Override
        public GetProductData createFromParcel(Parcel source) {
            return new GetProductData(source);
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
    @Expose(deserialize = false)
    private int selectedVariantIndex;

    public GetProductData() {
    }

    protected GetProductData(Parcel in) {
        this.id = in.readString();
        this.storeId = in.readString();
        this.categoryIds = in.readString();
        this.title = in.readString();
        this.brand = in.readString();
        this.nutrient = in.readString();
        this.description = in.readString();
        this.tags = in.readString();
        this.image = in.readString();
        this.imageType = in.readString();
        this.imageUrl = in.readString();
        this.showPrice = in.readString();
        this.isTaxEnable = in.readString();
        this.gstTaxType = in.readString();
        this.gstTaxRate = in.readString();
        this.status = in.readString();
        this.sort = in.readString();
        this.isExportFromFile = in.readString();
        this.deleted = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.image10080 = in.readString();
        this.image300200 = in.readString();
        this.variants = in.createTypedArrayList(Variant.CREATOR);
        this.selectedVariant = in.readParcelable(SelectedVariant.class.getClassLoader());
        this.categoryName = in.readString();
        this.isSelected = in.readByte() != 0;
        this.count = in.readInt();
        this.selectedVariantIndex = in.readInt();
    }

    public int getSelectedVariantIndex() {
        return selectedVariantIndex;
    }

    public void setSelectedVariantIndex(int selectedVariantIndex) {
        this.selectedVariantIndex = selectedVariantIndex;
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
        dest.writeString(this.id);
        dest.writeString(this.storeId);
        dest.writeString(this.categoryIds);
        dest.writeString(this.title);
        dest.writeString(this.brand);
        dest.writeString(this.nutrient);
        dest.writeString(this.description);
        dest.writeString(this.tags);
        dest.writeString(this.image);
        dest.writeString(this.imageType);
        dest.writeString(this.imageUrl);
        dest.writeString(this.showPrice);
        dest.writeString(this.isTaxEnable);
        dest.writeString(this.gstTaxType);
        dest.writeString(this.gstTaxRate);
        dest.writeString(this.status);
        dest.writeString(this.sort);
        dest.writeString(this.isExportFromFile);
        dest.writeValue(this.deleted);
        dest.writeString(this.image10080);
        dest.writeString(this.image300200);
        dest.writeTypedList(this.variants);
        dest.writeParcelable(this.selectedVariant, flags);
        dest.writeString(this.categoryName);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.count);
        dest.writeInt(this.selectedVariantIndex);
    }
}
