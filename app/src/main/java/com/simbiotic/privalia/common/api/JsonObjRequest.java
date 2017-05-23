package com.simbiotic.privalia.common.api;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.simbiotic.privalia.AppBase;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class JsonObjRequest extends JsonObjectRequest
{
    private boolean retrySent;

    public JsonObjRequest(int method, String url, String requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
        init();
    }

    public JsonObjRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        init();
    }

    public JsonObjRequest(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        init();
    }

    public JsonObjRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        init();
    }

    public JsonObjRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
        init();
    }

    private void init() {
        ApiUtil.buildDefaultRetryPolicy(this);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        CookieManager.checkSessionCookie(AppBase.getInstance(), response.headers);
        return super.parseNetworkResponse(response);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        ApiError er = ApiError.getByStatusCode(AppBase.getInstance(), volleyError);
        if (er.mType == ApiError.Type.UNAUTHORIZED && !retrySent){
            retrySent = true;

        }
        return super.parseNetworkError(volleyError);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = new HashMap<String, String>();
        ApiUtil.addHeaderAuthorization(headers);
        return headers;
    }

    public static JSONObject buildSyncRequest(String url) {
        return buildSyncRequest(Method.GET, url, null);
    }

    public static JSONObject buildSyncRequest(int method, String url) {
        return buildSyncRequest(method, url, null);
    }

    public static JSONObject buildSyncRequest(int method, String url, JSONObject paramsJson) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjRequest(method, url, paramsJson, future, future);

        RequestQueue mRequestQueue = Volley.newRequestQueue(AppBase.getInstance().getApplicationContext());
        mRequestQueue.add(request);

        JSONObject json = null;
        try {
            json = future.get(ApiUtil.TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // exception handling
            //ExceptionHandlerHelper.handler(e);
        } catch (ExecutionException e) {
            // exception handling
            //ExceptionHandlerHelper.handler(e);
        } catch (TimeoutException e) {
            // exception handling
            //ExceptionHandlerHelper.handler(e);
        }

        return json;
    }

    public static JSONObject buildSyncRequest(String url, String authorizationToken, JSONObject paramsJson) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, paramsJson, future, future);

        RequestQueue mRequestQueue = Volley.newRequestQueue(AppBase.getInstance().getApplicationContext());
        mRequestQueue.add(request);

        JSONObject json = null;
        try {
            json = future.get(ApiUtil.TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // exception handling
           // ExceptionHandlerHelper.handler(e);
        } catch (ExecutionException e) {
            // exception handling
           // ExceptionHandlerHelper.handler(e);
        } catch (TimeoutException e) {
            // exception handling
           // ExceptionHandlerHelper.handler(e);
        }

        return json;
    }
}
