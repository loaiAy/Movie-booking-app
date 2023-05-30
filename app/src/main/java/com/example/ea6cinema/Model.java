package com.example.ea6cinema;

/*This class define a new data type which is called Model. We using this type of data
 for retrieving the movie data from the HTTP response. The model type build based on the MOVIE-API. */
public class Model {

    private final String release_date;
    private int id;
    private final String backdrop_path;
    private double vote_average;
    private boolean adult, video;
    private String overview, title, poster_path, original_language, original_title;

    public Model(String overview, String title, boolean adult, double vote, String pic, String date,
                 String originaltitle, String language, String backdrop, int id) {
        this.overview = overview;
        this.title = title;
        this.adult = adult;
        this.vote_average = vote;
        this.poster_path = pic;
        this.release_date = date;
        this.original_language = language;
        this.backdrop_path = backdrop;
        this.id = id;
        this.original_title = originaltitle;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public void setVote(int vote) {
        this.vote_average = vote;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
