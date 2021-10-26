
package com.signity.shopkeeperapp.dashboard.orders.printSetting;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("StoreTimeSetting")
    @Expose
    private StoreTimeSetting storeTimeSetting;

    @SerializedName("store_print_logo")
    @Expose
    private String storePrintLogo;

    @SerializedName("store_300_200_print_logo")
    @Expose
    private String store300200PrintLogo;

    @SerializedName("store_100_80_print_logo")
    @Expose
    private String store10080PrintLogo;

    @SerializedName("PrintOrderSetting")
    @Expose
    private Object printOrderSettingObject;

    public StoreTimeSetting getStoreTimeSetting() {
        return storeTimeSetting;
    }

    public void setStoreTimeSetting(StoreTimeSetting storeTimeSetting) {
        this.storeTimeSetting = storeTimeSetting;
    }

    public PrintOrderSetting getPrintOrderSetting() {
        PrintOrderSetting printOrderSetting = null;
        try {
            Gson gson = new Gson();

            String response = gson.toJson(printOrderSettingObject);
            Log.e("nkjf", "getPrintOrderSetting: " + response);
            if (response.equalsIgnoreCase("[]")) {
                return null;
            }
            printOrderSetting = gson.fromJson(response, PrintOrderSetting.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return printOrderSetting;
    }

    public String getStorePrintLogo() {
        return storePrintLogo;
    }

    public void setStorePrintLogo(String storePrintLogo) {
        this.storePrintLogo = storePrintLogo;
    }

    public String getStore300200PrintLogo() {
        return store300200PrintLogo;
    }

    public void setStore300200PrintLogo(String store300200PrintLogo) {
        this.store300200PrintLogo = store300200PrintLogo;
    }

    public String getStore10080PrintLogo() {
        return store10080PrintLogo;
    }

    public void setStore10080PrintLogo(String store10080PrintLogo) {
        this.store10080PrintLogo = store10080PrintLogo;
    }
}
