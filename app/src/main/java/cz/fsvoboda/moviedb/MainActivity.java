package cz.fsvoboda.moviedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import cz.fsvoboda.moviedb.api.MovieDbApi;
import cz.fsvoboda.moviedb.api.MovieDbApiClient;
import cz.fsvoboda.moviedb.api.PopularResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
