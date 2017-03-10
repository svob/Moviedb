package cz.fsvoboda.moviedb;

import android.util.Log;

import java.util.List;

import cz.fsvoboda.moviedb.api.Movie;
import cz.fsvoboda.moviedb.api.MovieDbApi;
import cz.fsvoboda.moviedb.api.MovieDbApiClient;
import cz.fsvoboda.moviedb.api.PopularResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Filip Svoboda
 */

public class ApiMovieProvider implements IMovieProvider {
    private MovieDbApi api;

    public ApiMovieProvider() {
        api = MovieDbApiClient.getInstance().getApi();
    }

    @Override
    public void getMovies(final int page, final int pageSize, final MainActivity.CallbackInterface callback) {
        Call<PopularResponse> call = api.getPopularMovies(page);
        call.enqueue(new Callback<PopularResponse>() {
            @Override
            public void onResponse(Call<PopularResponse> call, Response<PopularResponse> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = response.body().getResults();
                    callback.onMoviesDownloaded(movies, response.body().getTotal_pages());
                    callback.persistMovies(movies, page, pageSize);
                }
            }

            @Override
            public void onFailure(Call<PopularResponse> call, Throwable t) {
                Log.e("ApiMovieProvider", "PopularCall fail", t);
            }
        });
    }
}
