package com.muhrez.popularmovies.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by emuhrez on 20/07/2017.
 */

public class MovieApiClient {
    private static final String TMDB_URL = "http://api.themoviedb.org/3/";

    private static Retrofit retrofit = null;
    public static Retrofit getMovies() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(TMDB_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
