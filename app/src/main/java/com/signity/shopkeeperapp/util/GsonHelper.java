package com.signity.shopkeeperapp.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.signity.shopkeeperapp.model.DashBoardModelStoreDetail;
import com.signity.shopkeeperapp.model.ItemListModel;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by rajesh on 27/4/16.
 */
public class GsonHelper {

    Gson gson = null;

    public GsonHelper() {
        gson = new Gson();
    }

    public String getStore(DashBoardModelStoreDetail outstanding) {
        String result = null;
        Type type = new TypeToken<DashBoardModelStoreDetail>() {
        }.getType();
        try {
            result = gson.toJson(outstanding, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public DashBoardModelStoreDetail getStore(String strJson) {
        DashBoardModelStoreDetail dashBoardModelStoreDetail = null;
        Type type = new TypeToken<DashBoardModelStoreDetail>() {
        }.getType();
        try {
            dashBoardModelStoreDetail = gson.fromJson(strJson, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return dashBoardModelStoreDetail;
    }

    public String getItems(List<ItemListModel> items) {
        String result = null;
        Type type = new TypeToken<List<ItemListModel>>() {
        }.getType();
        try {
            result = gson.toJson(items, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<ItemListModel> getItems(String result) {
        List<ItemListModel> itemsList = null;
        Type type = new TypeToken<List<ItemListModel>>() {
        }.getType();
        try {
            itemsList = gson.fromJson(result, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return itemsList;
    }
}
