package com.simbiotic.privalia.domain.usecases;


import com.simbiotic.privalia.model.ws.MovieApi;

/**
 * Created by Rayworld on 19/05/2017.
 */

public class GetMoviesUsecase implements UseCase {

        private int token;

        public GetMoviesUsecase(int token) {
            this.token = token;
        }

        @Override
        public void execute() {
            MovieApi.getInstance().getMovies(token);
        }
}
