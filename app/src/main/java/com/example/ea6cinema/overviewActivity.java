package com.example.ea6cinema;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*overviewActivity is responsible for displaying the movie data.*/
public class overviewActivity extends AppCompatActivity {

    private TextView title, overview, release1, language1, originalmoviename1;
    private YouTubePlayerView youTube;
    private String api = "your api", baseUrl;
    Button purchase;
    ArrayList<TrailerModel> modelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Overview");
    }

    /*After creating the instance of the class, It receives the intent which contains all the data
    * of the movie which its card have been clicked on the main activity class.*/
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();

        int id = intent.getIntExtra("id", 0);

        overview = findViewById(R.id.movieoverview);
        title = findViewById(R.id.movieTitle);
        release1 = findViewById(R.id.releasedate1);
        language1 = findViewById(R.id.language1);
        originalmoviename1 = findViewById(R.id.originalmoviename1);
        purchase = findViewById(R.id.purchasebttn);

        language1.setText(intent.getStringExtra("language"));
        release1.setText(intent.getStringExtra("date"));
        overview.setText(intent.getStringExtra("description"));
        title.setText(intent.getStringExtra("title"));
        originalmoviename1.setText(intent.getStringExtra("originalname"));

        /*base url for the movie trailers.*/
        baseUrl = "https://api.themoviedb.org/3/movie/" + Integer.toString(intent.getIntExtra("id",0)) + "/";
        getTrailer();


        /*A youtube player to show the trailer.*/
        youTube = findViewById(R.id.youtubeplayerview);
        getLifecycle().addObserver(youTube);

        youTube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(modelArrayList.get(0).getKey(), 0);
            }
        });

        /*A button which takes the user to purchase activity for booking seats.*/
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(overviewActivity.this, purchaseActivity.class);
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("movieId", id);
                startActivity(intent);
            }
        });
    }

    /*This procedure create a api interface based retrofit to retrieve the available movie trailers. */
    private void getTrailer() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.getVideos(api).enqueue(new Callback<Trailers>() {
            @Override
            public void onResponse(Call<Trailers> call, Response<Trailers> response) {
                if(response.isSuccessful()) {
                    modelArrayList.addAll(response.body().getVideos());
                }
            }

            @Override
            public void onFailure(Call<Trailers> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
