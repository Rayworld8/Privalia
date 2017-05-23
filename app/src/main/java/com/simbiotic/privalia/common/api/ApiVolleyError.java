package com.simbiotic.privalia.common.api;

import com.android.volley.VolleyError;


public class ApiVolleyError extends VolleyError {

    private int status;
    private String message;
    private int reply;

    public ApiVolleyError(ApiBaseResponse apiBaseResponse) {
        super(apiBaseResponse.getMessage());
        status = apiBaseResponse.getStatus();
        message = apiBaseResponse.getMessage();
        reply = apiBaseResponse.getReply();
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getReply() { return reply; }
}
