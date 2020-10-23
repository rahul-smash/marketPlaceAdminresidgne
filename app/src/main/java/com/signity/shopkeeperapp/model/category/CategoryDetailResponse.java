package com.signity.shopkeeperapp.model.category;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.signity.shopkeeperapp.model.Categories.GetCategoryData;
import com.signity.shopkeeperapp.model.Categories.SubCategory;

import java.util.List;

public class CategoryDetailResponse {

    @Expose
    @SerializedName("data")
    private CategoryData data;

    public CategoryData getData() {
        return data;
    }

    public void setData(CategoryData data) {
        this.data = data;
    }

    public static class CategoryData extends GetCategoryData {

        public static final Creator<CategoryData> CREATOR = new Creator<CategoryData>() {
            @Override
            public CategoryData createFromParcel(Parcel source) {
                return new CategoryData(source);
            }

            @Override
            public CategoryData[] newArray(int size) {
                return new CategoryData[size];
            }
        };

        @SerializedName("ChildCategory")
        @Expose
        private List<SubCategory> subCategory;

        protected CategoryData(Parcel in) {
            super(in);
            this.subCategory = in.createTypedArrayList(SubCategory.CREATOR);
        }

        @Override
        public List<SubCategory> getSubCategory() {
            return subCategory;
        }

        @Override
        public void setSubCategory(List<SubCategory> subCategory) {
            this.subCategory = subCategory;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeTypedList(this.subCategory);
        }
    }
}
