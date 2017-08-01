package com.muhrez.popularmovies.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by emuhrez on 25/07/2017.
 */

public class MoviesReviewResult {

    @SerializedName("results")
    private List<MoviesReview> results;

    public List<MoviesReview> getReviewResults() {
        return results;
    }
}
