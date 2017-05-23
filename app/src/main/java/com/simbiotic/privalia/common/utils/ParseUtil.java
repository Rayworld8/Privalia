package com.simbiotic.privalia.common.utils;

import com.simbiotic.privalia.common.Constants;
import com.simbiotic.privalia.model.entities.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rayworld on 20/05/2017.
 */

public class ParseUtil {

    public static ArrayList<Movie> parseMovies(JSONObject response) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONArray results = response.getJSONArray(Constants.RESULTS);
            for (int i = 0; i < results.length(); i++) {
                Movie movie = new Movie();
                JSONObject result = results.getJSONObject(i);

                String id = result.getString("id");
                movie.setId(id);

                String title = result.getString("title");
                movie.setTitle(title);

                String date = result.getString("release_date");
                if (!date.equalsIgnoreCase("")){
                    String year = date.substring(0, Math.min(date.length(), 4));
                    //int year = Integer.parseInt(strYear);
                    movie.setYear(year);
                }

                String resume = result.getString("overview");
                movie.setResume(resume);

                String picture = result.getString("poster_path");
                movie.setPhoto(picture.substring(1));

                movies.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }
}
