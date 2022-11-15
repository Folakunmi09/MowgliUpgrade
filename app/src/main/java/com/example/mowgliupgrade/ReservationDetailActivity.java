package com.example.mowgliupgrade;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mowgliupgrade.models.Movie;
import com.example.mowgliupgrade.models.MovieShowing;
import com.example.mowgliupgrade.models.Reservation;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;

import org.parceler.Parcels;

public class ReservationDetailActivity extends AppCompatActivity {
    private static final String TAG = "ReservationDetailActivity";
    private ImageView ivPoster, ivBarcode;
    private TextView tvShowingDate, tvShowingTime, tvOrderNumber, tvRuntime, tvGenres, tvRating, tvTitle, tvTicketNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);

        ivPoster = findViewById(R.id.ivPoster);
        tvShowingDate = findViewById(R.id.tvShowingDate);
        tvShowingTime = findViewById(R.id.tvShowingTime);
        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvRuntime = findViewById(R.id.tvRuntime);
        tvGenres = findViewById(R.id.tvGenres);
        tvRating = findViewById(R.id.tvRating);
        tvTitle = findViewById(R.id.tvTitle);
        tvTicketNum = findViewById(R.id.tvTicketNum);

        Reservation reservation = Parcels.unwrap(getIntent().getParcelableExtra("reservation"));
        MovieShowing movieShowing = reservation.getMovieShowing();
        Movie movie = movieShowing.getMovie();

        Glide.with(this).load(reservation.getMovieShowing().getMovie().getPosterPath()).into(ivPoster);
        if (reservation.getReservationId().length() > 9)
            tvOrderNumber.setText("#" + reservation.getReservationId().substring(1, 9));
        else
            tvOrderNumber.setText("#" + reservation.getReservationId());
        tvTitle.setText(movie.getTitle());
//        if (movie.getRating() != 0)
        tvRating.setText(String.valueOf(movie.getRating()));
        String runtime = movieShowing.getMovie().getRunTime() + " mins";
        tvRuntime.setText(runtime);

        tvShowingDate.setText(movieShowing.getShowDate());
        tvShowingTime.setText(movieShowing.getShowTime());

        tvGenres.setText(movieShowing.getGenres());
        tvTicketNum.setText(String.valueOf(reservation.getNumOfTickets()));

        ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReservationDetailActivity.this, DetailActivity.class);
                i.putExtra("movie", Parcels.wrap(movie));
                startActivity(i); //Brings up second activity
            }
        });




//        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//        try {
//            String info = reservation.getReservationInfo();
//            if (info.length() > 80) {
//                info = info.substring(0, 80);
//            }
//            BitMatrix bitMatrix = multiFormatWriter.encode(info, BarcodeFormat.CODE_128,
//                    600, 200 );
//            Bitmap bitmap = Bitmap.createBitmap(600, 200, Bitmap.Config.RGB_565);
//            for (int i = 0; i < ivBarcode.getWidth(); i++){
//                for (int j = 0; j < ivBarcode.getHeight(); j++){
//                    bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
//                }
//            }
//            ivBarcode.setImageBitmap(bitmap);
//        } catch (WriterException e) {
//            e.printStackTrace();
//            Log.d(TAG, "Failed to generate Barcode");
//
//        }


    }
}