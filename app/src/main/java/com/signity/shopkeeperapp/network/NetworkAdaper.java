package com.signity.shopkeeperapp.network;


import android.content.Context;

import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.Util;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Rajinder on 5/10/15.
 */
public class NetworkAdaper {
    public static NetworkAdaper cInstance;
    public static ApiService apiService;


    /* Static 'instance' method */
    public static NetworkAdaper getInstance() {
        return cInstance;
    }

    public static void initInstance(Context ctx) {

        if (cInstance == null) {
            cInstance = new NetworkAdaper();
            String store_id = Util.loadPreferenceValue(ctx, Constant.STORE_ID);
            String url = setBaseUrl(store_id);
            setupRetrofitClient(url);

        }
    }

    public static void setupRetrofitClient(String url) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(2, TimeUnit.MINUTES);
        client.setReadTimeout(2, TimeUnit.MINUTES);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(client)).setEndpoint(url).setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        apiService = restAdapter.create(ApiService.class);
    }

    public static ApiService getNetworkServices() {
        return apiService;
    }

    public static String setBaseUrl(String store_id) {
        String url = "";
        if (store_id.equalsIgnoreCase("")) {
            url = NetworkConstant.BASE + NetworkConstant.STORE_ID + NetworkConstant.APISTORE;
        } else {
            url = NetworkConstant.BASE + "/" + store_id + NetworkConstant.APISTORE;
        }
        return url;
    }

}
