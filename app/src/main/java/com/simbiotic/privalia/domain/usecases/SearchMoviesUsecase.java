package com.simbiotic.privalia.domain.usecases;

import com.simbiotic.privalia.model.ws.MovieApi;

/**
 * Created by Rayworld on 19/05/2017.
 */

public class SearchMoviesUsecase implements UseCase {

    private String query;
    private int token;

    public SearchMoviesUsecase(String query, int token) {
        this.query = query;
        this.token = token;
    }

    @Override
    public void execute() {
        MovieApi.getInstance().getSearchedMovies(query, token);
    }
}
