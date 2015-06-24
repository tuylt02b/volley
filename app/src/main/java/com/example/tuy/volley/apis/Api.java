package com.example.tuy.volley.apis;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tientun on 3/9/15.
 */
public class Api {
    public static String API_BASE_URL = "http://api.androidhive.info/";

    public static final String PATH_DEMO = "contacts";

    private static final String API_DEVICE_TYPE = "1";
    private static Api instance;
    private static Context mContext;
    private RequestQueue mRequestQueue;
    private String mAccessToken = null;
    private String mApplicationID = "AuB8KlQWfoioQhmL3wJ22WDxI8Do2k3t";
    private String mRestApiKey = "di1RIcUYFwwmMJZaaTk01DO73txOvp1c";
    CookieStore cookieStore = new BasicCookieStore();
    DefaultHttpClient httpClient = new DefaultHttpClient();

    private Api(Context context) {
        this.mContext = context;
    }

    public static void init(Context context) {
        instance = new Api(context);
    }

    public static Api getInstance() {
        if (mContext == null) {
            throw new IllegalStateException("You must call init(Context context) first.");
        }
        return instance;
    }

    public ApiRequest post(final String pathMethod, final Map<String, String> params, Response.Listener<NetworkResponse> responseListener, ApiErrorListener errorListener) {
        return request(Request.Method.POST, pathMethod, params, responseListener, errorListener);
    }

    public ApiRequest get(final String pathMethod, final Map<String, String> params, Response.Listener<NetworkResponse> responseListener, ApiErrorListener errorListener) {
        return request(Request.Method.GET, pathMethod, params, responseListener, errorListener);
    }

    public ApiRequest upload(final String pathMethod, final Map<String, String> params, Response.Listener<NetworkResponse> responseListener, ApiErrorListener errorListener) {
        return request(Request.Method.POST, pathMethod, params, responseListener, errorListener);
    }

    public ApiRequest put(final String pathMethod, final Map<String, String> params, Response.Listener<NetworkResponse> responseListener, ApiErrorListener errorListener) {
        return request(Request.Method.PUT, pathMethod, params, responseListener, errorListener);
    }

    public ApiRequest delete(final String pathMethod, final Map<String, String> params, Response.Listener<NetworkResponse> responseListener, ApiErrorListener errorListener) {
        return request(Request.Method.DELETE, pathMethod, params, responseListener, errorListener);
    }

    public ApiRequest request(int method, final String pathMethod, final Map<String, String> params, Response.Listener<NetworkResponse> responseListener, ApiErrorListener errorListener) {
        httpClient.setCookieStore(cookieStore);
        HttpStack httpStack = new HttpClientStack(httpClient);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext, httpStack);

        String url = API_BASE_URL + pathMethod;
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Application-Id", mApplicationID);
        headers.put("REST-API-Key", mRestApiKey);

        ApiRequest request = new ApiRequest(method, url, params, headers, responseListener, errorListener);
        request.setShouldCache(true);
        requestQueue.add(request);
        return request;
    }



    public void getUserInfo(Map<String, String> params, Response.Listener<NetworkResponse> responseListener, ApiErrorListener errorListener) {
        get(PATH_DEMO, params, responseListener, errorListener);
    }

}