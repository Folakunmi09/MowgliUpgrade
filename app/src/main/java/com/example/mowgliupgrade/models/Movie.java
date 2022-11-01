package com.example.mowgliupgrade.models;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Headers;

@Parcel
public class Movie {
        int movieId;
    String backdropPath;
    String posterPath;
    String title;
    String overview;
    List<String> genres;
    double rating;
    int runTime;

    // Empty constructor needed by the Parceler library
    public Movie(){

    }

    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");//Path to movie's poster
        title = jsonObject.getString("title"); //movie's title
        overview = jsonObject.getString("overview");//movie's description
        rating = jsonObject.getDouble("vote_average");
        movieId = jsonObject.getInt("id");
        genres = new ArrayList<>();
    }

    public static List<Movie> fromJsonArray(JSONArray jsonArray) throws JSONException {
        //Method creates a list of movies from the given JSON array
        List<Movie> movies = new ArrayList<>();
        JSONObject m;
        Log.i("Movie, json", String.valueOf(jsonArray.length()));
        for (int i =0; i<jsonArray.length(); i++ ){
            Log.i("Movie, json", String.valueOf(i));
            m = jsonArray.getJSONObject(i);
            movies.add(new Movie(m));
        }
        return movies;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath(){
        //if we simply return posterPath as is, it's going to be unusable. We won't be able to
        // actually get the poster from it because the current posterPath data is a shortened
        // (relative) form of what we actually need
//        return posterPath;

        return String.format("https://image.tmdb.org/t/p/w342%s", posterPath);

    }
//
    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s",backdropPath);
    }

    public String getTitle(){
        return title;
    }

    public String getOverview(){
        return overview;
    }

    public double getRating(){ return rating; }

    public int getMovieId(){ return movieId; }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", backdropPath='" + backdropPath + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", genres=" + genres +
                ", rating=" + rating +
                '}';
    }
}
