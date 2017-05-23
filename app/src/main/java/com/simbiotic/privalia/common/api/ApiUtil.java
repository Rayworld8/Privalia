package com.simbiotic.privalia.common.api;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;


public class ApiUtil {

    public static final int TIMEOUT = 10000;
    private static final int MAX_RETRIES = 0;
    private static final float BACKOFF_MULT = 1f;

    public static void addHeaderAuthorization(Map<String, String> headers) {
        headers.put("Accept", "application/json");
        /*if(UserManager.getToken()!=null || !UserManager.getToken().isEmpty())
            headers.put("Authorization", "Bearer "+ UserManager.getToken());
        String creds = String.format("%s:%s", BrandCore.getPublicKey(), BrandCore.getPrivateKey());
        headers.put("X-BB-Auth", creds);
        CookieManager.addSessionCookie(AppBase.getInstance(), headers);*/
    }

    public static void buildDefaultRetryPolicy(Request request) {
        request.setRetryPolicy(
                new DefaultRetryPolicy(
                        TIMEOUT,
                        MAX_RETRIES,
                        BACKOFF_MULT
                )
        );
    }

    public static Map<String, String> buildSortParams(boolean asc) {
        String order = "-1";
        if (asc)
            order = "1";

        Map<String, String> params = new HashMap<>();
        params.put("order", "{\"sort\":{\"create\":"+order+"}}");
        return params;
    }

    public static Map<String, String> buildSortParams() {
        return buildSortParams(false);
    }
}
