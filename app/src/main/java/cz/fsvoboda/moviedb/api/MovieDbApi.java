package cz.fsvoboda.moviedb.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Filip Svoboda
 */
public interface MovieDbApi {

    @GET("movie/popular")
    Call<PopularResponse> getPopularMovies(@Query("page") int page);

    @GET("configuration")
    Call<ApiConfig> getApiConfig();
}
