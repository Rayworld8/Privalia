package com.simbiotic.privalia.common.api;

import android.content.Context;

import com.android.volley.VolleyError;
import com.simbiotic.privalia.R;

import java.io.Serializable;


public class ApiError implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int NO_INDEX = -1;

    public enum Type {
        OTHER(
                R.string.fatal_error_title,
                R.string.fatal_error
        ),
        PARSE(
                R.string.parse_error_title,
                R.string.parse_error_message
        ),
        CONNECTION(
                R.string.no_connection_title,
                R.string.no_connection
        ),
        INVALID_USER(
                R.string.error,
                R.string.login_error
        ),
        FORBIDDEN( // 403
                R.string.error,
                R.string.forbidden_error,
                false,
                false
        ),
        NOT_FOUND( // 404
                R.string.error,
                R.string.not_found_error,
                false,
                true
        ),
        CONFLICT( // 409
                R.string.invalid_revision_error_title,
                R.string.invalid_revision_error,
                false,
                false
        ),
        VALIDATION(
                R.string.validation_error_title,
                R.string.validation_error_body,
                false,
                false
        ),
        UNAUTHORIZED( // 401
                // These values shouldn't ever be used because this is merely a signal
                // to open the login dialog.
                R.string.error,
                R.string.fatal_error
        ),
        INTERNAL_SERVER( // 500
                // These values shouldn't ever be used because this is merely a signal
                // to open the login dialog.
                R.string.error,
                R.string.fatal_error
        );

        protected int mTitle;
        protected int mMessage;
        protected boolean mTryAgain;
        protected boolean mFinishActivity;

        Type(int title, int message) {
            this(title, message, true, false);
        }

        Type(int title, int message, boolean tryAgain, boolean finishActivity) {
            mTitle = title;
            mMessage = message;
            mTryAgain = tryAgain;
            mFinishActivity = finishActivity;
        }
    }

    public static int API_TOKEN_EXPIRED = 3;

    public String mTitle;
    public String mMessage;
    public Type mType;
    public int mIndex;

    public ApiError(Context context, Type type) {
        this(context, type.mTitle, type.mMessage, type);
    }

    public ApiError(Context context, int title, int message, Type type) {
        this(context.getString(title), context.getString(message), type);
    }

    public ApiError(String title, String message, Type type) {
        this(title, message, type, NO_INDEX);
    }

    public ApiError(String title, String message, Type type, int index) {
        mTitle = title;
        mMessage = message;
        mType = type;
        mIndex = index;
    }

    public static ApiError getByStatusCode(Context context, int code) {
        Type error;

        switch (code) {
            case 403:
                error = Type.FORBIDDEN;
                break;
            case 404:
                error = Type.NOT_FOUND;
                break;
            case 409:
                error = Type.CONFLICT;
                break;
            case 422:
                error = Type.VALIDATION;
                break;
            case 401:
                error = Type.UNAUTHORIZED;
                break;
            case 500:
                error = Type.INTERNAL_SERVER;
                break;
            default:
                error = Type.OTHER;
        }

        return new ApiError(context, error);
    }

    public static ApiError getByStatusCode(Context context, VolleyError vError) {
        if (vError.networkResponse != null) {
            return getByStatusCode(context, vError.networkResponse.statusCode);
        }

        return getByStatusCode(context, 0);
    }
}
