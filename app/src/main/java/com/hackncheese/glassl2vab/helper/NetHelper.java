package com.hackncheese.glassl2vab.helper;

import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Hashtable;


public class NetHelper {

    // for logs
    private static final String TAG = NetHelper.class.getSimpleName();

    /**
     * Retrieves the content of a URL
     *
     * @param url     : the url of the web page
     * @param headers : additional request headers
     * @return the content as a {@link String}
     */
    public static String getDataFromUrl(String url, Hashtable<String, String> headers, String method) {
        OkHttpClient client = new OkHttpClient();
        String result;

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        if (method.equals("PUT")) {
            requestBuilder.put(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), ""));
        }
        for (String key : headers.keySet()) {
            requestBuilder.addHeader(key, headers.get(key));
        }
        Request request = requestBuilder.build();

        try {
            Response response = client.newCall(request).execute();
            Log.d(TAG, response.networkResponse().toString());
            result = response.body().string();
        } catch (IOException e) {
            Log.e(TAG, String.format("timed out while trying to get data from url %s", url));
            result = null;
        }

        return result;
    }
}
