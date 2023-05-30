package com.example.ea6cinema;

import java.util.ArrayList;

/*This class defines a new data type which called Movies.
* This Movies data type used for calling the GET methods of the created api interface, and for the responses.*/
public class Movies {

    private String stat;
    private String total;
    private ArrayList<Model> results;

    public Movies(String stat, String total, ArrayList<Model> results) {
        this.stat = stat;
        this.total = total;
        this.results = results;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<Model> getMovies() {
        return results;
    }

    public void setMovies(ArrayList<Model> results) {
        this.results = results;
    }
}
