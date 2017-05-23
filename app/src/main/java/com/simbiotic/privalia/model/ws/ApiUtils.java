package com.simbiotic.privalia.model.ws;

import com.simbiotic.privalia.AppBase;
import com.simbiotic.privalia.R;
import com.simbiotic.privalia.common.Constants;

/**
 * Created by Rayworld on 19/05/2017.
 */

public class ApiUtils {

    public static String getMoviesQuery() {
        StringBuilder sb = new StringBuilder(Constants.QUERY);
        sb.append("&api_key=" + AppBase.getInstance().getString(R.string.api_key));

        return String.valueOf(sb);
    }


    public static String getMoviesTokenQuery(int token) {
        StringBuilder sb = new StringBuilder(Constants.QUERY);
        sb.append("&api_key=" + AppBase.getInstance().getString(R.string.api_key));
        sb.append("&page=" + token);

        return String.valueOf(sb);
    }


    public static String getSearchQuery(String query) {
        StringBuilder sb = new StringBuilder(Constants.SEARCH);
        sb.append("&api_key=" + AppBase.getInstance().getString(R.string.api_key));
        sb.append("&query=" + query);

        return String.valueOf(sb);
    }


    public static String getSearchTokenQuery(String query, int token) {
        StringBuilder sb = new StringBuilder(Constants.SEARCH);
        sb.append("&api_key=" + AppBase.getInstance().getString(R.string.api_key));
        sb.append("&query=" + query);
        sb.append("&page=" + token);

        return String.valueOf(sb);
    }
}

