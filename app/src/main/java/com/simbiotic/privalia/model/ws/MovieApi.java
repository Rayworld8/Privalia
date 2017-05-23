package com.simbiotic.privalia.model.ws;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.simbiotic.privalia.AppBase;
import com.simbiotic.privalia.common.Constants;
import com.simbiotic.privalia.common.api.Api;
import com.simbiotic.privalia.common.utils.ParseUtil;
import com.simbiotic.privalia.domain.events.GetMoviesErrorEvent;
import com.simbiotic.privalia.domain.events.GetMoviesEvent;
import com.simbiotic.privalia.domain.events.GetSearchMoviesErrorEvent;
import com.simbiotic.privalia.domain.events.GetSearchMoviesEvent;
import com.simbiotic.privalia.model.entities.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Rayworld on 19/05/2017.
 */

public class MovieApi {

    public static MovieApi INSTANCE;
    private ArrayList<Movie> movies = new ArrayList<>();
    public static int total_pages;
    public static int page;
    public static boolean searching;
    public static String wordQuery;

    private MovieApi() {

    }

    public static MovieApi getInstance() {

        if (INSTANCE == null)
            INSTANCE = new MovieApi();

        return INSTANCE;
    }


    public void getMovies(final int token) {
        movies.clear();
        String url;
        searching = false;
        if (token==1) {
            url = ApiUtils.getMoviesQuery();

        } else {
            url = ApiUtils.getMoviesTokenQuery(token);
        }

        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                movies.addAll(ParseUtil.parseMovies(response));

                if (response.has(Constants.TOTAL_PAGES) && response.has(Constants.PAGE)) {
                    total_pages = 0;
                    page = 0;
                    try {
                        page = Integer.parseInt(response.getString(Constants.PAGE));
                        total_pages = Integer.parseInt(response.getString(Constants.TOTAL_PAGES));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    total_pages = 0;
                    page = 0;
                }

                EventBus.getDefault().postSticky(new GetMoviesEvent(movies, token));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky(new GetMoviesErrorEvent());
            }
        });

        Api.getInstance(AppBase.getInstance()).addToRequestQueue(req);
    }


    public void getSearchedMovies(final String query, final int token) {
        movies.clear();
        String url;
        searching = true;
        wordQuery = query;
        if (token==1) {
            url = ApiUtils.getSearchQuery(query);
        } else {
            url = ApiUtils.getSearchTokenQuery(query, token);
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                movies.addAll(ParseUtil.parseMovies(response));

                if (response.has(Constants.TOTAL_PAGES) && response.has(Constants.PAGE)) {
                    total_pages = 0;
                    page = 0;
                    try {
                        page = Integer.parseInt(response.getString(Constants.PAGE));
                        total_pages = Integer.parseInt(response.getString(Constants.TOTAL_PAGES));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    total_pages = 0;
                    page = 0;
                }

                EventBus.getDefault().postSticky(new GetSearchMoviesEvent(movies, token));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky(new GetSearchMoviesErrorEvent());
            }
        });

        Api.getInstance(AppBase.getInstance()).addToRequestQueue(req);
    }

}
