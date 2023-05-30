package com.example.ea6cinema;


/*This class define a new data type which is called TrailerModel. We using this type of data
 for retrieving the trailers of the movie from the HTTP response. The model type build based on the MOVIE-API. */
public class TrailerModel {

    private final String key;

    public TrailerModel(String Key) {
        this.key = Key;
    }

    public String getKey() {
        return key;
    }
}
