package com.muhrez.popularmovies.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.muhrez.popularmovies.Model.MovieTrailer;
import com.muhrez.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by emuhrez on 25/07/2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private final List<MovieTrailer> trailerList;
    private final LayoutInflater mInflater;
    private final Context mContext;

    private final String YOUTUBE_IMG = "http://img.youtube.com/vi/";
    private final String YOUTUBE_URI_PREFIX = "https://www.youtube.com/watch?v=";

    public TrailersAdapter(Context context, List<MovieTrailer> trailerList) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.trailerList = trailerList;
            }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public TrailerViewHolder (View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.thumbnail_trailer);
        }

    }

    @Override
    public TrailersAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.image_trailer, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        String trailerImageUri;
        MovieTrailer movie = trailerList.get(position);
        final String youtubeUri = YOUTUBE_URI_PREFIX + movie.getKey();
        ImageView trailerImage = holder.imageView;
        trailerImageUri = YOUTUBE_IMG + movie.getKey() + "/0.jpg";
        Picasso.with(mContext)
                .load(trailerImageUri)
                .placeholder(R.color.colorB)
                .into(trailerImage);

        trailerImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUri)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }
}
