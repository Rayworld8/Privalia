package com.simbiotic.privalia.domain.events;

import com.simbiotic.privalia.model.entities.Movie;

import java.util.ArrayList;

/**
 * Created by Rayworld on 22/05/2017.
 */

public class GetSearchMoviesEvent implements BusEvent {

    private ArrayList<Movie> movies;
    private int token;

    public GetSearchMoviesEvent (ArrayList<Movie> movies, int token) {
        this.movies = movies;
        this.token = token;
    }

    public ArrayList<Movie> getSearchMovies() {
        return movies;
    }

    public int getToken() {
        return token;
    }
}
