package com.muhrez.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by emuhrez on 25/07/2017.
 */

public class MoviesReview implements Parcelable {

    @SerializedName("author")
    private final String author;
    @SerializedName("id")
    private final String idReviews;
    @SerializedName("content")
    private final String content;
    @SerializedName("url")
    private final String url;

    public String getAuthor() {
        return this.author;
    }

    public String getContent() {
        return this.content;
    }

    public String getUrl() {
        return this.url;
    }


    private MoviesReview(Parcel in) {
        author = in.readString();
        idReviews = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public static final Creator<MoviesReview> CREATOR = new Creator<MoviesReview>() {
        @Override
        public MoviesReview createFromParcel(Parcel in) {
            return new MoviesReview(in);
        }

        @Override
        public MoviesReview[] newArray(int size) {
            return new MoviesReview[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(idReviews);
        dest.writeString(content);
        dest.writeString(url);
    }
}
