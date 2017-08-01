package com.muhrez.popularmovies.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muhrez.popularmovies.Model.MoviesReview;
import com.muhrez.popularmovies.R;

import java.util.List;

/**
 * Created by emuhrez on 30/07/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private final List<MoviesReview> reviewList;
    private final LayoutInflater mInflater;
    private final Context mContext;

    public ReviewsAdapter(Context context, List<MoviesReview> reviewList) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.reviewList = reviewList;
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        public final TextView author;
        public final TextView contents;
        public ReviewsViewHolder(View itemView) {
            super(itemView);

            author = (TextView) itemView.findViewById(R.id.reviews_author);
            contents = (TextView) itemView.findViewById(R.id.reviews_content);
        }
    }

    @Override
    public ReviewsAdapter.ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_reviews, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        MoviesReview movie = reviewList.get(position);
        TextView author = holder.author;
        author.setText(movie.getAuthor());
        TextView contents = holder.contents;
        contents.setText(movie.getContent());

        final String finalReviewImageUri = movie.getUrl();
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(finalReviewImageUri)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
