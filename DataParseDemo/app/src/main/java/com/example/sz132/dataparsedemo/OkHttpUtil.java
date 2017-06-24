package com.example.sz132.dataparsedemo;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by sz132 on 2017/6/24.
 */

public class OkHttpUtil {

    private static OkHttpClient client;
    private static Request request;

    public static void sendRequest(String address, Callback callback){
        client = new OkHttpClient();
        request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
