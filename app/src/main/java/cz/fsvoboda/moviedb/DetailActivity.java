package cz.fsvoboda.moviedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cz.fsvoboda.moviedb.api.ApiConfig;
import cz.fsvoboda.moviedb.api.MovieDbApiClient;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        toolbar.setNavigationContentDescription("Movie list");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ApiConfig apiConfig = MovieDbApiClient.getApiConfig();

        TextView toolbarTitle = (TextView) findViewById(R.id.detail_title);
        ImageView movieImage = (ImageView) findViewById(R.id.detail_image);
        TextView releaseDate = (TextView) findViewById(R.id.detail_date);
        TextView rating = (TextView) findViewById(R.id.detail_rating);
        TextView description = (TextView) findViewById(R.id.detail_description);

        if(getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            toolbarTitle.setText(extras.getString(MoviesAdapter.EXTRAS_MOVIE_NAME));
            releaseDate.setText(extras.getString(MoviesAdapter.EXTRAS_RELEASE_DATE));
            String ratingText = String.format("%.2f (%dx)", extras.getFloat(MoviesAdapter.EXTRAS_MOVIE_RATING), extras.getInt(MoviesAdapter.EXTRAS_MOVIE_RATING_COUNT));
            rating.setText(ratingText);
            description.setText(extras.getString(MoviesAdapter.EXTRAS_MOVIE_DESCRIPTION));
            if (apiConfig != null) {
                Glide.with(this)
                        .load(apiConfig.getImages().getBase_url() + apiConfig.getImages().getBackdrop_sizes().get(0) + extras.get(MoviesAdapter.EXTRAS_IMAGE_PATH))
                        .into(movieImage);
            }
        }
    }

    public void onBackClick(View vie) {
        finish();
        overridePendingTransition(0, 0);
    }
}
