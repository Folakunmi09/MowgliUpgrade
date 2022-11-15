package com.example.mowgliupgrade;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.mowgliupgrade.models.MovieShowing;
import com.example.mowgliupgrade.models.Reservation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


public class TicketingActivity extends YouTubeBaseActivity implements DateAdapter.DateTransferInterface {

    static final String TAG = "DetailActivity";
    public static final String YOUTUBE_API_KEY = "AIzaSyA8utPxf2T0q3NGDns-EVsRAUtGtV98mXg";
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private ImageView ivPoster;
    private Button btnConfirm;
    private MaterialButton btnNine, btnNoon, btnFour, btnSeven, btnTen;
    private TextView tvTitle, tvOverview, tvRating, tvRuntime;
    private RecyclerView rvGenres, rvCast, rvDates;
    private YouTubePlayerView youTubePlayerView;
    private Slider ticketSlider;


    int ticketCount = 1;
    List<String> genres;
    List<Cast> castList;
    List<Date> dates;

    private Movie movie;


    MovieShowing movieShowing;
    DatabaseReference movieShowingDatabase, reservationDatabase;
    FirebaseUser currentUser;

    private String bookingDate, bookingTime;

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
        btnNine = findViewById(R.id.btnNine);
        btnNoon = findViewById(R.id.btnNoon);
        btnFour = findViewById(R.id.btnFour);
        btnSeven = findViewById(R.id.btnSeven);
        btnTen = findViewById(R.id.btnTen);

        ticketSlider = findViewById(R.id.ticketNumSlider);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        movieShowingDatabase = FirebaseDatabase.getInstance().getReference("MovieShowings");
        reservationDatabase = FirebaseDatabase.getInstance().getReference("Reservations");

        movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        tvTitle.setText(movie.getTitle());
        tvRating.setText(String.valueOf(movie.getRating()));
        Glide.with(this).load(movie.getPosterPath()).into(ivPoster);


        genres =new ArrayList<>();
        GenreAdapter genreAdapter = new GenreAdapter(this, genres);
        rvGenres.setAdapter(genreAdapter);
        rvGenres.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        dates = new ArrayList<>();
        DateAdapter dateAdapter = new DateAdapter(this, dates, this);
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

        //change time color when clicked, to show selection
        final boolean[] selectedTime = {false};
        btnNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedTime[0]){
                    btnNine.setStrokeColorResource(R.color.ivory);
                    btnNine.setTextColor(getResources().getColor(R.color.ivory, getTheme()));
                    selectedTime[0] = true;
                    bookingTime = "9:00 am";
                }

            }
        });

        btnNoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedTime[0]){
                    btnNoon.setStrokeColorResource(R.color.ivory);
                    btnNoon.setTextColor(getResources().getColor(R.color.ivory, getTheme()));
                    selectedTime[0] = true;
                    bookingTime = "12:00 pm";

                }

            }
        });

        btnFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedTime[0]){
                    btnFour.setStrokeColorResource(R.color.ivory);
                    btnFour.setTextColor(getResources().getColor(R.color.ivory, getTheme()));
                    selectedTime[0] = true;
                    bookingTime = "4:00 pm";

                }

            }
        });

        btnSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedTime[0]){
                    btnSeven.setStrokeColorResource(R.color.ivory);
                    btnSeven.setTextColor(getResources().getColor(R.color.ivory, getTheme()));
                    selectedTime[0] = true;
                    bookingTime = "7:00 pm";

                }

            }
        });

        btnTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedTime[0]){
                    btnTen.setStrokeColorResource(R.color.ivory);
                    btnTen.setTextColor(getResources().getColor(R.color.ivory, getTheme()));
                    selectedTime[0] = true;
                    bookingTime = "10:00 pm";

                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookingTime != null && bookingDate != null) {
                    //TODO: Update database with user's ticket
                    bookReservation();
                }
            }
        });

        ticketSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                ticketCount = (int) value;
            }
        });









    }

    private void bookReservation() {
        String intRunTime = tvRuntime.getText().toString();
        intRunTime = intRunTime.substring(0, intRunTime.length()-5);
        movie.setRunTime(Integer.parseInt(intRunTime));
        movie.setGenres(genres);
        String genreString;
        if (genres.size() >= 2 )
            genreString = genres.get(0) + " | " + genres.get(1);
        else if (genres.size() == 1)
            genreString = genres.get(0);
        else
            genreString = "";
        movieShowing = new MovieShowing(movie, bookingDate, bookingTime, 20
                , genreString);

        movieShowingDatabase.child(movieShowing.getShowingId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    MovieShowing mShowing = task.getResult().getValue(MovieShowing.class);
                    if (mShowing != null) {
                        if (ticketCount > mShowing.getAvailableTickets()) {
                            Toast.makeText(TicketingActivity.this, "Selected number of tickets unable. Failed to get tickets.", Toast.LENGTH_SHORT).show();
                            ticketSlider.setValue(1);
                        }
                        else{
                            mShowing.updateTicketCount(ticketCount);
                            movieShowingDatabase.child(mShowing.getShowingId()).setValue(mShowing);
                        }
                        mShowing.updateTicketCount(ticketCount);
                        movieShowingDatabase.child(mShowing.getShowingId()).setValue(mShowing);
//
                    }
                    else{
                        Log.d(TAG, String.valueOf(movieShowing));
                        movieShowingDatabase.child(movieShowing.getShowingId()).setValue(movieShowing);
                    }

                    String reservationInfo = "1 ticket for " +
                            movieShowing.getMovie().getTitle() + " at " + movieShowing.getShowTime() +
                            ", " +  movieShowing.getShowDate() + " for " +
                            currentUser.getEmail();

                    Reservation reservation =
                            new Reservation(
                                    currentUser.getEmail(),
                                    movieShowing,
                                    ticketCount,
                                    reservationInfo);

                    reservationDatabase.child(currentUser.getUid()).child(reservation.getReservationId()).setValue(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Reservation added to database");
                                Toast.makeText(TicketingActivity.this, "Tickets reserved successfully.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(TicketingActivity.this, ViewReservationsActivity.class));
                            }else{
                                Log.e(TAG, "Failed to add reservation to database");
                                Toast.makeText(TicketingActivity.this, "Failed to book reservation!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    Log.d(TAG, "Failed to get movieShowing from database");
                }

            }
        });




    }


    @Override
    public void setBookingDate(String date) {
        bookingDate = date;
        Log.d("TicketingActivity", bookingDate);
    }
}