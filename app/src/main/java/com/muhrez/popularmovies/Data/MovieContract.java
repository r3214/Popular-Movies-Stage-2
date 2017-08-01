package com.muhrez.popularmovies.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by emuhrez on 28/07/2017.
 */

public class MovieContract {
    public static final String AUTHORITY = "com.muhrez.popularmovies";

    private static final Uri BASE_URL = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIE = "movies";

    public static final class MovieEntry implements BaseColumns {


        public static final Uri CONTENT_URL = BASE_URL.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIE;

        /* Used internally as the name of our movies table. */
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URL, id);
        }

        public static final String[] MOVIE_COLUMNS = {
                COLUMN_MOVIE_ID,
                COLUMN_ORIGINAL_TITLE,
                COLUMN_POSTER_PATH,
                COLUMN_OVERVIEW,
                COLUMN_VOTE_AVERAGE,
                COLUMN_RELEASE_DATE,
                COLUMN_BACKDROP_PATH
        };

        public static final int COL_MOVIE_ID = 0;
        public static final int COL_MOVIE_TITLE = 1;
        public static final int COL_MOVIE_POSTER_PATH = 2;
        public static final int COL_MOVIE_OVERVIEW = 3;
        public static final int COL_MOVIE_VOTE_AVERAGE = 4;
        public static final int COL_MOVIE_RELEASE_DATE = 5;
        public static final int COL_MOVIE_BACKDROP_PATH = 6;

    }
}
