package com.muhrez.popularmovies;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.muhrez.popularmovies.Adapter.ReviewsAdapter;
import com.muhrez.popularmovies.Adapter.TrailersAdapter;
import com.muhrez.popularmovies.Data.MovieContract;
import com.muhrez.popularmovies.Model.MovieObject;
import com.muhrez.popularmovies.Model.MovieTrailer;
import com.muhrez.popularmovies.Model.MovieTrailerResult;
import com.muhrez.popularmovies.Model.MoviesReview;
import com.muhrez.popularmovies.Model.MoviesReviewResult;
import com.muhrez.popularmovies.Network.MovieApiClient;
import com.muhrez.popularmovies.Network.MovieApiService;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by emuhrez on 22/06/2017.
 */

public class MovieDetailActivity extends AppCompatActivity {
    private static final String EXTRA_MOVIE = "movie";

    private RecyclerView trailerRv;
    private RecyclerView reviewsRv;
    private LinearLayoutManager mLayoutManager;
    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;

    private MovieApiService movieApiService;
    private List<MovieTrailer> trailerList;
    private List<MoviesReview> reviewList;

    private MovieObject mMovie;
    private ImageView backdrop;
    private TextView title;
    private TextView user_rating;
    private TextView description;
    private ImageView poster;
    private TextView releaseDate;

    private boolean isFavorite;


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
            setFavoriteStatus();
        } else {
            throw new IllegalArgumentException("Detail activity must receive a movie parcelable");
        }

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(mMovie.getTitle());

        backdrop = (ImageView) findViewById(R.id.backdrop);
        title = (TextView) findViewById(R.id.movie_title);
        user_rating = (TextView) findViewById(R.id.movie_rating);
        description = (TextView) findViewById(R.id.movie_description);
        poster = (ImageView) findViewById(R.id.movie_poster);
        releaseDate = (TextView) findViewById(R.id.movie_release);

        title.setText(mMovie.getTitle());
        description.setText(mMovie.getDescription());
        user_rating.setText("Rating : " + mMovie.getUser_rating() + "/10");
        releaseDate.setText("Release Date : " + mMovie.getReleaseDate());

        if (isFavorite) {
            try {
                String filename = String.valueOf(mMovie.getId());
                File photofile = new File(getFilesDir(), filename);
                Bitmap freshBitMap = BitmapFactory.decodeStream(new FileInputStream(photofile));
                poster.setImageBitmap(freshBitMap);
                backdrop.setImageBitmap(freshBitMap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Picasso.with(this)
                    .load(mMovie.getPoster())
                    .into(poster);
            Picasso.with(this)
                    .load(mMovie.getBackdrop())
                    .into(backdrop);
        }

        final ImageButton favoriteButton = (ImageButton) findViewById(R.id.favoriteButton);
        final Drawable favoriteIcon = getDrawable(R.drawable.ic_favorite_red_24dp);
        final Drawable nonFavoriteIcon = getDrawable(R.drawable.ic_favorite_gray_24dp);

        favoriteButton.setImageDrawable(isFavorite ? favoriteIcon : nonFavoriteIcon);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorite = !isFavorite;
                favoriteButton.setImageDrawable(isFavorite ? favoriteIcon : nonFavoriteIcon);
                if (isFavorite) {
                    Bitmap bitmap = ((BitmapDrawable) poster.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    addMovie(byteArray);

                } else {
                    removeMovie();
                }
            }
        });

        getTrailer(String.valueOf(mMovie.getId()));
        getReview(String.valueOf(mMovie.getId()));

    }

    public static Intent getIntent(Context context, MovieObject movie){
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    private void setupTrailerView(List<MovieTrailer> trailers) {

        Context context = getBaseContext();
        trailerRv = (RecyclerView) findViewById(R.id.trailer_rv);

        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        trailerRv.setLayoutManager(mLayoutManager);

        trailersAdapter= new TrailersAdapter(this, trailers);
        trailerRv.setAdapter(trailersAdapter);
    }

    private void getTrailer(String id) {
        String apikey = getString(R.string.api_key);

        movieApiService = MovieApiClient.getMovies().create(MovieApiService.class);
        Call<MovieTrailerResult> call = movieApiService.getTrailer(id, apikey);
        fetchTrailer(call);
    }

    private void fetchTrailer(Call<MovieTrailerResult> call) {
        call.enqueue(new Callback<MovieTrailerResult>() {
            @Override
            public void onResponse(@NonNull Call<MovieTrailerResult> call, @NonNull Response<MovieTrailerResult> response) {
                MovieTrailerResult trailerResult = response.body();
                trailerList = trailerResult != null ? trailerResult.getTrailerResults() : null;
                setupTrailerView(trailerList);
            }

            @Override
            public void onFailure(@NonNull Call<MovieTrailerResult> call, @NonNull Throwable t) {

            }
        });
    }

    private void setupReviewsView(List<MoviesReview> reviews) {
        reviewsRv = (RecyclerView) findViewById(R.id.reviews_rv);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewsRv.setLayoutManager(mLayoutManager);

        reviewsAdapter= new ReviewsAdapter(this, reviews);
        reviewsRv.setAdapter(reviewsAdapter);
    }

    private void getReview(String id) {
        String apikey = getString(R.string.api_key);

        movieApiService = MovieApiClient.getMovies().create(MovieApiService.class);
        Call<MoviesReviewResult> call = movieApiService.getReview(id, apikey);
        fetchReviews(call);
    }

    private void fetchReviews(Call<MoviesReviewResult> call) {
        call.enqueue(new Callback<MoviesReviewResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviesReviewResult> call, @NonNull Response<MoviesReviewResult> response) {
                MoviesReviewResult reviewResult = response.body();
                reviewList = reviewResult != null ? reviewResult.getReviewResults() : null;
                setupReviewsView(reviewList);
            }

            @Override
            public void onFailure(@NonNull Call<MoviesReviewResult> call, @NonNull Throwable t) {

            }
        });
    }

    private void setFavoriteStatus() {
        Cursor cursor = getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URL,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + mMovie.getId(),
                null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            int cursorCount = cursor.getCount();
            isFavorite = cursorCount > 0;
            cursor.close();
        }
    }

    private void removeMovie() {
        String currentMovieId = String.valueOf(mMovie.getId());
        String whereClause = MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?" ;
        String[] whereArgs = new String[]{currentMovieId};
        int rowsDeleted = getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URL, whereClause, whereArgs);

        File photofile = new File(getFilesDir(), currentMovieId);
        if (photofile.exists()) {
            photofile.delete();
        }
    }

    private void addMovie(byte[] byteArray) {

        ContentResolver movieContentResolver = getContentResolver();
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getId());
        movieValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, mMovie.getTitle());
        movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getDescription());
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, mMovie.getPoster());
        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, mMovie.getUser_rating());
        movieValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, mMovie.getBackdrop());
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
        movieContentResolver.insert(MovieContract.MovieEntry.CONTENT_URL, movieValues);


        String filename = String.valueOf(mMovie.getId());
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(byteArray);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

