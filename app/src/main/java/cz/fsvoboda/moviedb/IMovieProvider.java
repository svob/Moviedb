package cz.fsvoboda.moviedb;

/**
 * @author Filip Svoboda
 */

public interface IMovieProvider {

    void getMovies(int page, int pageSize, MainActivity.CallbackInterface callback);

}
