package com.simbiotic.privalia.domain.events;

import com.simbiotic.privalia.model.entities.Movie;

import java.util.ArrayList;

/**
 * Created by Rayworld on 19/05/2017.
 */

public class GetMoviesEvent implements BusEvent {

    private ArrayList<Movie> movies;
    private int token;

    public GetMoviesEvent (ArrayList<Movie> movies, int token) {
        this.movies = movies;
        this.token = token;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public int getToken() {
        return token;
    }
}
