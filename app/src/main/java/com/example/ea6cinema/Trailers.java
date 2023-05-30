package com.example.ea6cinema;

import java.util.ArrayList;

/*This class defines a new data type which called Trailers.
 * This Trailers data type used for calling the GET methods of the created api interface, and for the responses.*/
public class Trailers {

    private ArrayList<TrailerModel> results;

    public Trailers(ArrayList<TrailerModel> results) {
        this.results = results;
    }

    public ArrayList<TrailerModel> getVideos() {
        return results;
    }
}
