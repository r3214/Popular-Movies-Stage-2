package com.muhrez.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.muhrez.popularmovies.Adapter.MoviesAdapter;
import com.muhrez.popularmovies.Data.MovieContract;
import com.muhrez.popularmovies.Model.MovieObject;
import com.muhrez.popularmovies.Model.MoviesResult;
import com.muhrez.popularmovies.Network.MovieApiClient;
import com.muhrez.popularmovies.Network.MovieApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> ,MoviesAdapter.MovieClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String KEY_MOVIE_LIST = "MOVIES KEY";
    private static final String FAV_MOVIE_LIST = "MOVIES FAV KEY";
    private static final String CURRENT_SCROLL_POSITION = "CURRENT SCROLL";
    private static final int LOADER_ID = 43;

    private RecyclerView mRv;
    private GridLayoutManager mLayoutManager;
    private MoviesAdapter mAdapter;
    private String mSort;
    private MovieApiService movieApiService;
    private List<MovieObject> mMovieList;

    private  int currentScroll;
    private Boolean offLineData;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mMovieList = savedInstanceState.getParcelableArrayList(KEY_MOVIE_LIST);
            offLineData = savedInstanceState.getBoolean(FAV_MOVIE_LIST);
            currentScroll = savedInstanceState.getInt(CURRENT_SCROLL_POSITION, 0);
        }
        setupSharedPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(){

        int columns = calculateNoOfColumns(this);
        mRv = (RecyclerView) findViewById(R.id.recyclerView);

        mLayoutManager = new GridLayoutManager(this, columns);
        mLayoutManager.scrollToPosition(currentScroll);

        mRv.setLayoutManager(mLayoutManager);

        mAdapter = new MoviesAdapter(mMovieList,this,offLineData);
        mRv.setAdapter(mAdapter);
    }

    @Override
    public void onClickMovie(MovieObject movieObject) {
        startActivity(MovieDetailActivity.getIntent(this, movieObject));
    }

    private void getMovies(String sortBy) {
        offLineData = false;
        String apikey = getString(R.string.api_key);
        movieApiService = MovieApiClient.getMovies().create(MovieApiService.class);
        Call<MoviesResult> call = movieApiService.getMovie(sortBy, apikey);
        fetchMovies(call);
    }

    private void fetchMovies(Call<MoviesResult> call) {
        call.enqueue(new Callback<MoviesResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResult> call, @NonNull Response<MoviesResult> response) {
                MoviesResult movieResult = response.body();
                mMovieList = movieResult != null ? movieResult.getResults() : null;
                setupRecyclerView();
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResult> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_MOVIE_LIST, (ArrayList<? extends Parcelable>)mMovieList);
        outState.putBoolean(FAV_MOVIE_LIST, offLineData);
        outState.putInt(CURRENT_SCROLL_POSITION, mLayoutManager.findFirstVisibleItemPosition());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.sort_movies_key))) {
            setSortType(sharedPreferences);
        }
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setSortType(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setSortType(SharedPreferences sharedPreferences) {
        mSort = sharedPreferences.getString(getString(R.string.sort_movies_key), getString(R.string.popular_values));
        updateMovies(mSort);
    }

    private void updateMovies(String sortUnit) {
        if (sortUnit.equals(getString(R.string.popular_values))) {
            getMovies(sortUnit);
        } else if (sortUnit.equals(getString(R.string.toprated_values))) {
            getMovies(sortUnit);
        } else if (sortUnit.equals(getString(R.string.fav_values))) {
            getDataFavorite();
        }
    }


    //Not Loader
    private void getDataFavorite() {
        offLineData = true;
        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URL,
                MovieContract.MovieEntry.MOVIE_COLUMNS,
                null,
                null,
                null);

        mMovieList = new ArrayList<>();
        if (cursor!=null && cursor.getCount() != 0) {
            while (cursor.moveToNext()){

                int idColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                int titleColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
                int ratingColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
                int overviewColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
                int releaseDateColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
                int posterColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
                int backdropColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH);


                long id = cursor.getLong(idColumnIndex);
                String title = cursor.getString(titleColumnIndex);
                String poster = cursor.getString(posterColumnIndex);
                String overview = cursor.getString(overviewColumnIndex);
                String userRating = cursor.getString(ratingColumnIndex);
                String releaseDate = cursor.getString(releaseDateColumnIndex);
                String backdrop = cursor.getString(backdropColumnIndex);
                mMovieList.add(new MovieObject(id, title, poster, overview, userRating, releaseDate, backdrop));
            }
            cursor.close();
            setupRecyclerView();
        } else {
            Toast.makeText(this, "You didn't have Favorite", Toast.LENGTH_LONG).show();
        }
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }
}
