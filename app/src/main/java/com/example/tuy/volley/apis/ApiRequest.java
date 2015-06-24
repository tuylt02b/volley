package com.example.tuy.volley.apis;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by tientun on 3/6/15.
 */
public class ApiRequest extends Request<NetworkResponse> {
    private static final String TAG = "ApiRequest";
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final Response.Listener<NetworkResponse> listener;
    private final ApiErrorListener errorListener;
    private ApiError apiError;

    public ApiRequest(int method, String url, Map<String, String> params, Map<String, String> headers, Response.Listener<NetworkResponse> listener, ApiErrorListener errorListener) {
        super(method, url, null);
        this.headers = headers;
        this.params = params;
        this.listener = listener;
        this.errorListener = errorListener;

        Log.d(TAG, "params: "+this.params);
        Log.d(TAG, "url: "+url);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
        }catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }

    }

    @Override
    public Cache.Entry getCacheEntry() {
        return super.getCacheEntry();
    }

    @Override
    public String getCacheKey() {
        return super.getCacheKey();
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        Log.d(TAG, new String(response.data));
        listener.onResponse(response);
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        String msg = null;
        int statusCode = 0;
        ApiError apiError;
        try {
            if (error.networkResponse != null && error.networkResponse.data != null) {
                JsonParser parser = new JsonParser();
                JsonObject jo = parser.parse(new String(error.networkResponse.data)).getAsJsonObject();
                msg = jo.get("message").getAsString();

                if (jo.get("code") != null) {
                    statusCode = jo.get("code").getAsInt();
                } else {
                    statusCode = jo.get("status_code").getAsInt();
                }

            } else if (error instanceof TimeoutError) {
                statusCode = 408;
                msg = "ネットワークエラーは、後でもう一度お試しください!";
            } else if (error.getCause().getClass() == IOException.class) {
                statusCode = 401;
                msg = "";
            }
        } catch (Exception e) {
            statusCode = 401;
            msg = "";
        }

        apiError = new ApiError(error.networkResponse, msg, statusCode);
        if (errorListener != null) {
            errorListener.onErrorResponse(apiError);
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> h = headers != null ? headers : super.getHeaders();
        return h;
    }



    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params != null ? params : super.getParams();
    }

    @Override
    public String getUrl() {
        if (getMethod() == Method.GET) {
            try {
                return super.getUrl() + "?" + encodeUrl(getParams());
            } catch (AuthFailureError authFailureError) {
                authFailureError.printStackTrace();
            }
        }
        return super.getUrl();
    }

    public static String encodeUrl(Map<String, String> parameters) {
        if (parameters == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null && value != null) {
                if (first) {
                    first = false;
                } else {
                    sb.append("&");
                }
                sb.append(URLEncoder.encode(key) + "=" + URLEncoder.encode(value));
            }
        }
        return sb.toString();
    }
}
