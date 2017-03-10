package cz.fsvoboda.moviedb.api;

import java.io.IOException;

import cz.fsvoboda.moviedb.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Filip Svoboda
 */
public class MovieDbApiClient {

    private static MovieDbApiClient instance;
    private static ApiConfig apiConfig;
    private MovieDbApi api;

    /**
     * Private constructor to prevent unwanted instantiation.
     */
    private MovieDbApiClient() {
        OkHttpClient httpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                HttpUrl originalUrl = originalRequest.url();

                HttpUrl url = originalUrl.newBuilder()
                        .addQueryParameter("api_key", BuildConfig.API_KEY)
                        .build();

                Request request = originalRequest.newBuilder()
                        .url(url)
                        .build();

                return chain.proceed(request);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(MovieDbApi.class);
    }

    /**
     * Returns singleton instance of {@link MovieDbApiClient}
     * Instance is initialized if it does not exist.
     *
     * @return singleton instance of {@link MovieDbApiClient}
     */
    public static synchronized MovieDbApiClient getInstance() {
        if (instance == null) {
            instance = new MovieDbApiClient();
        }

        return instance;
    }

    /**
     * @return The Movie Db API.
     */
    public MovieDbApi getApi() {
        return api;
    }

    /**
     * @return API configuration.
     */
    public static ApiConfig getApiConfig() {
        return apiConfig;
    }

    public static void setApiConfig(ApiConfig apiConfig) {
        MovieDbApiClient.apiConfig = apiConfig;
    }
}
