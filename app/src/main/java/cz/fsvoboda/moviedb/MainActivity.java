package cz.fsvoboda.moviedb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.List;

import cz.fsvoboda.moviedb.api.ApiConfig;
import cz.fsvoboda.moviedb.api.Movie;
import cz.fsvoboda.moviedb.api.MovieDbApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static RecyclerView recyclerView;
    private MovieProvider movieProvider;
    private int totalPagesCount;

    interface CallbackInterface {
        void onMoviesDownloaded(List<Movie> movies, int totalCount);
        void persistMovies(List<Movie> movies, int page, int pageSize);
    }

    private BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                movieProvider.changeProvider(isNetworkAvailable());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        initRecyclerView();
        initApiConfig();

        movieProvider = new MovieProvider(this, isNetworkAvailable());
        movieProvider.getMovies(1, callback);

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(connectivityReceiver);
        super.onDestroy();
    }

    /**
     * Initializes the recycler view for movies.
     */
    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MoviesAdapter adapter = new MoviesAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new MoviesAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int dataSetSize) {
                if (dataSetSize / MovieProvider.pageSize < totalPagesCount) {
                    movieProvider.getMovies(dataSetSize / MovieProvider.pageSize + 1, callback);
                }
            }
        });
    }

    /**
     * Initializes the API configuration for later use.
     */
    public static void initApiConfig() {
        final MovieDbApiClient apiClient = MovieDbApiClient.getInstance();

        Call<ApiConfig> configCall = apiClient.getApi().getApiConfig();
        configCall.enqueue(new Callback<ApiConfig>() {
            @Override
            public void onResponse(Call<ApiConfig> call, Response<ApiConfig> response) {
                if (response.isSuccessful()) {
                    apiClient.setApiConfig(response.body());
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiConfig> call, Throwable t) {
                Log.e("MainActivity", "ApiConfigCall fail", t);
            }
        });
    }


    private CallbackInterface callback = new CallbackInterface() {
        @Override
        public void onMoviesDownloaded(List<Movie> movies, int totalCount) {
            MoviesAdapter adapter = (MoviesAdapter) recyclerView.getAdapter();
            adapter.addMovies(movies);
            totalPagesCount = totalCount;
        }

        @Override
        public void persistMovies(List<Movie> movies, int page, int pageSize) {
            movieProvider.saveMovies(movies, page, pageSize);
        }
    };

    /**
     * Checks if internet connection is available.
     *
     * @return true if internet si available.
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
