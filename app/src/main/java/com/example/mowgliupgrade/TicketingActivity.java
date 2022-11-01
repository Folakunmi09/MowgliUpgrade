package com.example.mowgliupgrade;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.mowgliupgrade.adapters.CastAdapter;
import com.example.mowgliupgrade.adapters.DateAdapter;
import com.example.mowgliupgrade.adapters.GenreAdapter;
import com.example.mowgliupgrade.models.Cast;
import com.example.mowgliupgrade.models.Movie;
import com.google.android.material.button.MaterialButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Headers;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;


public class TicketingActivity extends YouTubeBaseActivity {

    static final String TAG = "DetailActivity";
    public static final String YOUTUBE_API_KEY = "AIzaSyA8utPxf2T0q3NGDns-EVsRAUtGtV98mXg";
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private ImageView ivPoster;
    private Button btnConfirm;
    private TextView tvTitle, tvOverview, tvRating, tvRuntime;
    private RecyclerView rvGenres, rvCast, rvDates;
    private YouTubePlayerView youTubePlayerView;

    List<String> genres;
    List<Cast> castList;
    List<Date> dates;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing);

        tvTitle = findViewById(R.id.dtTvTitle);
        tvRating = findViewById(R.id.tvRating);
        tvRuntime = findViewById(R.id.tvRuntime);
        ivPoster = findViewById(R.id.ivPoster);

        rvGenres = findViewById(R.id.rvGenres);
        rvDates = findViewById(R.id.rvDates);

        btnConfirm = findViewById(R.id.btnConfirm);

        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        tvTitle.setText(movie.getTitle());
        tvRating.setText(String.valueOf(movie.getRating()));
        Glide.with(this).load(movie.getPosterPath()).into(ivPoster);


        genres =new ArrayList<>();
        GenreAdapter genreAdapter = new GenreAdapter(this, genres);
        rvGenres.setAdapter(genreAdapter);
        rvGenres.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        dates = new ArrayList<>();
        DateAdapter dateAdapter = new DateAdapter(this, dates);
        rvDates.setAdapter(dateAdapter);
        rvDates.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        // get genres from api
        AsyncHttpClient client = new AsyncHttpClient();
        String movieDetailUrl = "https://api.themoviedb.org/3/movie/" + movie.getMovieId() + "?api_key=76fcd97be54364699973bbd32a79ac96";
        client.get(movieDetailUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "Got movie detail!: " + json.toString());
                JSONObject movieObject = json.jsonObject;
                Movie movie = new Movie();
                try {
                    String runtime = movieObject.getInt("runtime") + " mins";
                    tvRuntime.setText(runtime);
                    movie.setRunTime(movieObject.getInt("runtime"));
                    JSONArray genreArray = movieObject.getJSONArray("genres");
                    for (int i = 0; i < genreArray.length(); i++) {
                        genres.add(genreArray.getJSONObject(i).getString("name"));
                        genreAdapter.notifyItemInserted(genres.size()-1);
                    }
                    movie.setGenres(genres);

                    Log.d(TAG, "Movie: " + movie);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON exception", e);//for debugging purposes

                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "Failed to get movie genres");
            }
        });

        // get dates for today till 7-days from now
        LocalDate today = LocalDate.now();
        for (int i =0; i<8; i++) {
            Date showDate = Date.from(today.plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant());
            dates.add(showDate);
            dateAdapter.notifyItemInserted(dates.size()-1);
        }



    }


}