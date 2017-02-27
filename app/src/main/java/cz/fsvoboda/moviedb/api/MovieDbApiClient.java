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

    private MovieDbApi api;

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

    public static synchronized MovieDbApiClient getInstance() {
        if (instance == null) {
            instance = new MovieDbApiClient();
        }

        return instance;
    }

    public MovieDbApi getApi() {
        return api;
    }
}
