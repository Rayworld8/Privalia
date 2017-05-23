package com.simbiotic.privalia.common.api;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.simbiotic.privalia.AppBase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class JsonArrRequest extends JsonArrayRequest
{
    private boolean retrySent;

    public JsonArrRequest(int method, String url, String requestBody, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
        init();
    }

    public JsonArrRequest(String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        init();
    }

    public JsonArrRequest(int method, String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        init();
    }

    public JsonArrRequest(int method, String url, JSONArray jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        init();
    }

    public JsonArrRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        init();
    }

    public JsonArrRequest(String url, JSONArray jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
        init();
    }

    public JsonArrRequest(String url, JSONObject jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
        init();
    }

    private void init() {
        ApiUtil.buildDefaultRetryPolicy(this);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = new HashMap<String, String>();
        ApiUtil.addHeaderAuthorization(headers);
        return headers;
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        ApiError er = ApiError.getByStatusCode(AppBase.getInstance(), volleyError);
        if (er.mType == ApiError.Type.UNAUTHORIZED && !retrySent){
            retrySent = true;
        }
        return super.parseNetworkError(volleyError);
    }

    public static JSONArray buildSyncRequest(String url) {
        return buildSyncRequest(url, null);
    }

    public static JSONArray buildSyncRequest(String url, String authorizationToken) {
        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrRequest(url, future, future);

        RequestQueue mRequestQueue = Volley.newRequestQueue(AppBase.getInstance().getApplicationContext());
        mRequestQueue.add(request);

        JSONArray json = null;
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
}
