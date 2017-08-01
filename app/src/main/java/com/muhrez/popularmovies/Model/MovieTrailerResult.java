package com.muhrez.popularmovies.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by emuhrez on 25/07/2017.
 */

public class MovieTrailerResult {

    @SerializedName("results")
    private List<MovieTrailer> results;

    public List<MovieTrailer> getTrailerResults() {
        return results;
    }

}
