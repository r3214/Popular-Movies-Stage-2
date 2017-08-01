package com.muhrez.popularmovies.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.muhrez.popularmovies.Model.MovieObject;
import com.muhrez.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emuhrez on 20/07/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<MovieObject> mMovieList;
    private LayoutInflater mInflater;
    private Context mContext;
    private final MovieClickListener mOnClickListener;
    private boolean showingFavorites;

    private Cursor cursor;

    public interface MovieClickListener {
        void onClickMovie(MovieObject movieObject);
    }

    public MoviesAdapter(List<MovieObject> mMovieList, MovieClickListener mOnClickListener, boolean showingFavorites) {
        this.mMovieList = mMovieList;
        this.mOnClickListener = mOnClickListener;
        this.showingFavorites = showingFavorites;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.image_poster, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onViewRecycled(MovieViewHolder holder) {
        super.onViewRecycled(holder);
        holder.cleanUp();
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        String posterPath;
        MovieObject movie = mMovieList.get(position);
        posterPath = movie.getPoster();

        if (showingFavorites) {
            try {
                String fileName = String.valueOf(mMovieList.get(position).getId());
                File photofile = new File(mContext.getFilesDir(), fileName);
                Bitmap posterBitMap = BitmapFactory.decodeStream(new FileInputStream(photofile));
                holder.imageView.setImageBitmap(posterBitMap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Picasso.with(mContext)
                    .load(posterPath)
                    .placeholder(R.color.colorAccent)
                    .into(holder.imageView);
        }
    }

    public void setMovieList(List<MovieObject> movieList) {
        this.mMovieList = new ArrayList<>();
        this.mMovieList.addAll(movieList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mMovieList == null) ? 0 : mMovieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public final ImageView imageView;
        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_poster);
            itemView.setOnClickListener(this);
        }

        public void cleanUp() {
            final Context context = itemView.getContext();
            Picasso.with(context).cancelRequest(imageView);
            imageView.setImageBitmap(null);
            imageView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            MovieObject movie;
            movie = mMovieList.get(getAdapterPosition());
            mOnClickListener.onClickMovie(movie);
        }
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }
}
