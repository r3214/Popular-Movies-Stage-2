package com.muhrez.popularmovies.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by emuhrez on 20/07/2017.
 */

public class MoviesResult {
    @SerializedName("results")
    private List<MovieObject> results;

    public List<MovieObject> getResults() {
        return results;
    }
}
