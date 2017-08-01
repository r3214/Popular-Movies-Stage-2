package com.muhrez.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by emuhrez on 21/06/2017.
 */

public class MovieObject implements Parcelable{

    @SerializedName("original_title")
    private String title;
    @SerializedName("id")
    private long id;
    @SerializedName("poster_path")
    private String poster;
    @SerializedName("overview")
    private String description;
    @SerializedName("backdrop_path")
    private String backdrop;
    @SerializedName("vote_average")
    private String user_rating;
    @SerializedName("release_date")
    private String releaseDate;

    private boolean isFavorite;

    public MovieObject(long id, String title, String poster, String overview, String userRating,
                       String releaseDate, String backdrop) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.user_rating = userRating;
        this.description= overview;
        this.backdrop = backdrop;
        this.id = id;
    }

    private final String TMDB_Img = "http://image.tmdb.org/t/p/w500";

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public String getPoster() {
        return TMDB_Img + poster;
    }

    public String getDescription() {
        return description;
    }

    public String getBackdrop() {
        return TMDB_Img  + backdrop;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    private MovieObject(Parcel in) {
        title = in.readString();
        id = in.readLong();
        poster = in.readString();
        description = in.readString();
        backdrop = in.readString();
        user_rating = in.readString();
        releaseDate = in.readString();
        this.isFavorite = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeLong(id);
        dest.writeString(poster);
        dest.writeString(description);
        dest.writeString(backdrop);
        dest.writeString(user_rating);
        dest.writeString(releaseDate);
        dest.writeByte(isFavorite ? (byte) 1 : (byte) 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieObject> CREATOR = new Creator<MovieObject>() {
        @Override
        public MovieObject createFromParcel(Parcel in) {
            return new MovieObject(in);
        }

        @Override
        public MovieObject[] newArray(int size) {
            return new MovieObject[size];
        }
    };
}
