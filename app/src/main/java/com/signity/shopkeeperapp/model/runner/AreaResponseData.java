package com.signity.shopkeeperapp.model.runner;

import java.io.Serializable;

public class AreaResponseData implements Serializable {

    private String area;
    private String areaId;
    private boolean isChecked;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "AreaResponseData{" +
                "area='" + area + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}