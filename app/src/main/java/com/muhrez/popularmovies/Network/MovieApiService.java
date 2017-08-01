package com.muhrez.popularmovies.Network;

import com.muhrez.popularmovies.Model.MovieTrailerResult;
import com.muhrez.popularmovies.Model.MoviesResult;
import com.muhrez.popularmovies.Model.MoviesReviewResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by emuhrez on 21/06/2017.
 */

public interface MovieApiService {
    @GET("movie/{sort}")
    Call<MoviesResult> getMovie(@Path("sort") String sort, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<MovieTrailerResult> getTrailer(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MoviesReviewResult> getReview(@Path("id") String id, @Query("api_key") String apiKey);



}
