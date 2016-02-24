package com.signity.shopkeeperapp.app;

import com.signity.shopkeeperapp.model.ItemListModel;

import java.util.List;

/**
 * Created by Rajinder on 7/10/15.
 */
public class DataAdapter {


    public static DataAdapter cInstance;


    public List<ItemListModel> listItem;

    /* Static 'instance' method */
    public static DataAdapter getInstance() {
        return cInstance;
    }

    public static void initInstance() {

        if (cInstance == null) {
            cInstance = new DataAdapter();
        }
    }

    public List<ItemListModel> getListItem() {
        return listItem;
    }

    public void setListItem(List<ItemListModel> listItem) {
        this.listItem = listItem;
    }
}
