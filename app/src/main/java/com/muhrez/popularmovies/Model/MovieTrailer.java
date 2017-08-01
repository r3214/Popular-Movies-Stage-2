package com.muhrez.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by emuhrez on 25/07/2017.
 */

public class MovieTrailer implements Parcelable {

    @SerializedName("site")
    private final String site;
    @SerializedName("name")
    private final String name;
    @SerializedName("id")
    private final String idTrailer;
    @SerializedName("type")
    private final String type;
    @SerializedName("key")
    private final String key; //the Youtube key

    public String getKey() {
        return this.key;
    }


    private MovieTrailer(Parcel in) {
        site = in.readString();
        name = in.readString();
        idTrailer = in.readString();
        type = in.readString();
        key = in.readString();
    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(site);
        dest.writeString(name);
        dest.writeString(idTrailer);
        dest.writeString(type);
        dest.writeString(key);
    }
}
