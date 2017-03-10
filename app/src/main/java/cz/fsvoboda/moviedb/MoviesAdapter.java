package cz.fsvoboda.moviedb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import cz.fsvoboda.moviedb.api.ApiConfig;
import cz.fsvoboda.moviedb.api.ImagesConfig;
import cz.fsvoboda.moviedb.api.Movie;
import cz.fsvoboda.moviedb.api.MovieDbApiClient;

/**
 * @author Filip Svoboda
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    public static final String EXTRAS_MOVIE_NAME = "EXTRAS_MOVIE_NAME";
    public static final String EXTRAS_RELEASE_DATE = "EXTRAS_RELEASE_DATE";
    public static final String EXTRAS_MOVIE_RATING = "EXTRAS_MOVIE_RATING";
    public static final String EXTRAS_MOVIE_RATING_COUNT = "EXTRAS_MOVIE_RATING_COUNT";
    public static final String EXTRAS_MOVIE_DESCRIPTION = "EXTRAS_MOVIE_DESCRIPTION";
    public static final String EXTRAS_IMAGE_PATH = "EXTRAS_IMAGE_PATH";
    private List<Movie> movies;
    private Context context;
    private LayoutInflater inflater;
    private OnLoadMoreListener onLoadMoreListener;

    public MoviesAdapter(Context context) {
        this.context = context;
        movies = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    /**
     * Appends movies to the list of adapter.
     *
     * @param movies List of movies to be added.
     */
    public void addMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        };
        handler.post(r);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View movieView = inflater.inflate(R.layout.movie_list_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(movieView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        holder.rating.setText(String.format("%.1f / 10", movie.getVote_average()));
        ApiConfig apiConfig = MovieDbApiClient.getApiConfig();
        if (apiConfig == null) {
           MainActivity.initApiConfig();
        } else {
            ImagesConfig imagesConfig = apiConfig.getImages();
            Glide.with(context)
                    .load(imagesConfig.getBase_url() + imagesConfig.getBackdrop_sizes().get(0) + movie.getBackdrop_path())
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(holder.image) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.image.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }

        if ((position+1) == movies.size()) {
            onLoadMoreListener.onLoadMore(movies.size());
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView image;
        public TextView title;
        public TextView rating;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.list_row_movie_image);
            title = (TextView) itemView.findViewById(R.id.list_row_movie_name);
            rating = (TextView) itemView.findViewById(R.id.list_row_movie_rating);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Movie clickedItem = movies.get(position);
            Intent intent = new Intent(MoviesAdapter.this.context, DetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra(EXTRAS_MOVIE_NAME, clickedItem.getTitle());
            intent.putExtra(EXTRAS_RELEASE_DATE, clickedItem.getRelease_date());
            intent.putExtra(EXTRAS_MOVIE_RATING, clickedItem.getVote_average());
            intent.putExtra(EXTRAS_MOVIE_RATING_COUNT, clickedItem.getVote_count());
            intent.putExtra(EXTRAS_MOVIE_DESCRIPTION, clickedItem.getOverview());
            intent.putExtra(EXTRAS_IMAGE_PATH, clickedItem.getBackdrop_path());
            MoviesAdapter.this.context.startActivity(intent);
        }
    }

    /**
     * A listener that is called, when adapter needs more items.
     * @param listener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int dataSetSize);
    }
}
