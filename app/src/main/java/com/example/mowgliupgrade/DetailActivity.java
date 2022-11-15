package com.example.mowgliupgrade;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.mowgliupgrade.adapters.CastAdapter;
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

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;


public class DetailActivity extends YouTubeBaseActivity {

    static final String TAG = "DetailActivity";
    public static final String YOUTUBE_API_KEY = "AIzaSyA8utPxf2T0q3NGDns-EVsRAUtGtV98mXg";
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private ImageView ivPoster;
    private MaterialButton btnGetTicket;
    private TextView tvTitle, tvOverview, tvRating, tvRuntime;
    private RecyclerView rvGenres, rvCast;
    private YouTubePlayerView youTubePlayerView;

    List<String> genres;
    List<Cast> castList;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.dtTvTitle);
        tvOverview = findViewById(R.id.dtTvOverview);
        tvRating = findViewById(R.id.tvRating);
        tvRuntime = findViewById(R.id.tvRuntime);
        ivPoster = findViewById(R.id.ivPoster);
        rvCast = findViewById(R.id.rvCast);
        rvGenres = findViewById(R.id.rvGenres);
        btnGetTicket = findViewById(R.id.btnGetTicket);
        //        youTubePlayerView = findViewById(R.id.youtubePlayer);

        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvRating.setText(String.valueOf(movie.getRating()));
        Glide.with(this).load(movie.getPosterPath()).into(ivPoster);

        genres = new ArrayList<>();
        GenreAdapter genreAdapter = new GenreAdapter(this, genres);
        rvGenres.setAdapter(genreAdapter);
        rvGenres.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        castList = new ArrayList<>();
        CastAdapter castAdapter = new CastAdapter(this, castList);
        rvCast.setAdapter(castAdapter);
        rvCast.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        btnGetTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, movie.toString());
                Intent intent = new Intent(DetailActivity.this, TicketingActivity.class);
                intent.putExtra("movie", Parcels.wrap(movie));
                startActivity(intent);
            }
        });

        //TODO: Play trailer when play button is clicked

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

        // get cast from api
        String castUrl = "https://api.themoviedb.org/3/movie/" + movie.getMovieId() + "/credits?api_key=76fcd97be54364699973bbd32a79ac96";
        client.get(castUrl, new JsonHttpResponseHandler() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "Got cast!: " + json.toString());
                JSONObject castObject = json.jsonObject;
                try {
                    JSONArray cast = castObject.getJSONArray("cast");

//                    Log.i(TAG, "Cast: " + cast.toString()); //For debugging purposes

                    for (int i =0; i<cast.length(); i++ ){
                        if (cast.getJSONObject(i).getString("known_for_department").equals("Acting")) {
                            Cast c = new Cast();
                            c.setCastId(cast.getJSONObject(i).getInt("id"));
                            c.setCastName(cast.getJSONObject(i).getString("name"));
                            c.setProfilePath(cast.getJSONObject(i).getString("profile_path"));
                            c.setCharacter(cast.getJSONObject(i).getString("character"));
                            castList.add(c);
                            castAdapter.notifyItemInserted(castList.size()-1);

                            Log.i(TAG, "Cast at " + i + ": " + c.toString());
                        }
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON exception", e);//for debugging purposes
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "Failed to get movie cast");
            }
        });
    }

    private void initializeYoutube(final String youtubeKey) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("DetailActivity", "onInitializationSuccess");
                youTubePlayer.cueVideo(youtubeKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("DetailActivity", "onInitializationFailure");
            }
        });
    }
}