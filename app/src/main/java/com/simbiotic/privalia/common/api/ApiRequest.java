package com.simbiotic.privalia.common.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.simbiotic.privalia.AppBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class ApiRequest<T> extends Request<T> {

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Listener<T> listener;
    private final ApiErrorListener errorListener;
    private Map<String, String> params;

    /**
     * Make a request and return a parsed object from JSON.
     *
     * @param url           url
     * @param method        {@link Method}
     * @param clazz         class
     * @param params        params
     * @param listener      listener
     * @param errorListener listener
     */
    public ApiRequest(String url, int method, Class<T> clazz, Map<String, String> params, Listener<T> listener, ApiErrorListener errorListener) {
        super(method, url, null);
        this.clazz = clazz;
        this.listener = listener;
        this.errorListener = errorListener;
        this.params = params;
        this.headers = new HashMap<String, String>();

        ApiUtil.buildDefaultRetryPolicy(this);
        ApiUtil.addHeaderAuthorization(headers);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        ApiUtil.addHeaderAuthorization(headers);
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (params != null)
            return params;

        return super.getParams();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data);
            //HttpHeaderParser.parseCharset(response.headers));
            //CookieManager.checkSessionCookie(AppBase.getInstance(), response.headers);

            //Allow null
            if (json == null || json.length() == 0) {
                return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
            }

            JSONObject jsonObject = new JSONObject(json);
            T object;

            if (jsonObject.has("response")) {
                object = gson.fromJson(jsonObject.get("response").toString(), clazz);
            } else {
                object = gson.fromJson(jsonObject.toString(), clazz);
            }

            if (object instanceof ApiBaseResponse) {
                ApiBaseResponse resp = (ApiBaseResponse) object;
                if (response.statusCode != HttpURLConnection.HTTP_OK) {
                    return Response.error(new ApiVolleyError(resp));
                }
            }

            return Response.success(
                    object,
                    HttpHeaderParser.parseCacheHeaders(response));
//        } catch (UnsupportedEncodingException e) {
//            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    public static String buildSignature(String key, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            return new String(sha256_HMAC.doFinal(data.getBytes("UTF-8")));//Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
//            return new String(Hex.encodeHex(sha256_HMAC.doFinal(data.getBytes("UTF-8"))));
//            return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean retrySent = false;

    @Override
    public void deliverError(VolleyError error) {
        ApiError er = ApiError.getByStatusCode(AppBase.getInstance(), error);

        if (er.mType == ApiError.Type.UNAUTHORIZED) {
            return;
        }

        if (er.mType == ApiError.Type.INTERNAL_SERVER && !retrySent) {
            retrySent = true;

        }

        ApiErrorResponse apiError = null;
        if (error.networkResponse != null && error.networkResponse.data != null) {
            String json = new String(error.networkResponse.data);
            Gson gson = new Gson();
            try {
                apiError = gson.fromJson(json, ApiErrorResponse.class);
            } catch (JsonSyntaxException e) {
                //ExceptionHandlerHelper.handler(e);
            }
        }

        if (errorListener != null)
            errorListener.onErrorResponse(error, apiError);
    }

    public interface ApiErrorListener {
        /**
         * Callback method that an error has been occurred with the
         * provided error code and optional user-readable message.
         */
        void onErrorResponse(VolleyError error, ApiErrorResponse apiError);
    }

    private static Context getContext() {
        return AppBase.getInstance();
    }
}
