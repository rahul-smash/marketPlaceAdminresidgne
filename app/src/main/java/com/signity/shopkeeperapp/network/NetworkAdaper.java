package com.signity.shopkeeperapp.network;


import android.content.Context;

import com.signity.shopkeeperapp.util.prefs.AppPreference;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Rajinder on 5/10/15.
 */
public class NetworkAdaper {
    public static NetworkAdaper instance;
    public static ApiService apiService;

    public static NetworkAdaper getInstance() {
        return instance;
    }

    public static void initInstance(Context ctx) {
        if (instance == null) {
            instance = new NetworkAdaper();
            String store_id = AppPreference.getInstance().getStoreId();
            String url = setBaseUrl(store_id);
            setupRetrofitClient(url);
        }
    }

    public static void setupRetrofitClient(String url) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.MINUTES);
        client.setReadTimeout(1, TimeUnit.MINUTES);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint(url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        apiService = restAdapter.create(ApiService.class);
    }

    public static ApiService getNetworkServices() {
        return apiService;
    }

    public static String setBaseUrl(String store_id) {
        String url = "";
        if (store_id.equalsIgnoreCase("")) {
            url = NetworkConstant.BASE + NetworkConstant.APISTORE;
        } else {
            url = NetworkConstant.BASE + "/" + store_id + NetworkConstant.APISTORE;
        }
        return url;
    }

}
