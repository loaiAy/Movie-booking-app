package com.example.ea6cinema;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/* Api interface which include to GET call methods, One for retrieving current
    movies and the seconds for retrieving trailers of the movies. */

public interface ApiInterface {

    String base_url = "https://api.themoviedb.org/3/movie/";

    @GET("now_playing")
    Call<Movies> getMovies(
            @Query("api_key") String api_key
    );

    @GET("videos")
    Call<Trailers> getVideos(
            @Query("api_key") String api_key
    );
}