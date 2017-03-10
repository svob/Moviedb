package cz.fsvoboda.moviedb;

import android.content.Context;

import java.util.List;

import cz.fsvoboda.moviedb.api.Movie;

/**
 * @author Filip Svoboda
 */
public class MovieProvider {
    public static final int pageSize = 20;
    private IMovieProvider dataSource;
    private ApiMovieProvider apiMovieProvider;
    private DbMovieProvider dbMovieProvider;
    private Context context;

    public MovieProvider(Context context, boolean internet) {
        this.context = context;
        changeProvider(internet);
    }

    public void changeProvider(boolean internet) {
        if (internet) {
            dataSource = getApiMovieProvider();
        } else {
            dataSource = getDbMovieProvider();
        }
    }

    public void getMovies(int page, MainActivity.CallbackInterface callback) {
        dataSource.getMovies(page, pageSize, callback);
    }

    public void saveMovies(List<Movie> movies, int page, int pageSize) {
        getDbMovieProvider().saveMovies(movies, page, pageSize);
    }

    public ApiMovieProvider getApiMovieProvider() {
        if (apiMovieProvider == null) {
            apiMovieProvider = new ApiMovieProvider();
        }
        return apiMovieProvider;
    }

    public DbMovieProvider getDbMovieProvider() {
        if (dbMovieProvider == null) {
            dbMovieProvider = new DbMovieProvider(context);
        }
        return dbMovieProvider;
    }
}
