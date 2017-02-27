package cz.fsvoboda.moviedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cz.fsvoboda.moviedb.api.MovieDbApi;
import cz.fsvoboda.moviedb.api.MovieDbApiClient;
import cz.fsvoboda.moviedb.api.PopularResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieDbApi api = MovieDbApiClient.getInstance().getApi();
        Call<PopularResponse> call = api.getPopularMovies(1);
        call.enqueue(new Callback<PopularResponse>() {
            @Override
            public void onResponse(Call<PopularResponse> call, Response<PopularResponse> response) {
                Log.d("juchu", response.body().toString());
            }

            @Override
            public void onFailure(Call<PopularResponse> call, Throwable t) {
                Log.e("ajaj", t.getMessage());
            }
        });
    }
}
