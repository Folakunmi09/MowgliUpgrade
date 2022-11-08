package com.example.mowgliupgrade.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mowgliupgrade.R;
import com.example.mowgliupgrade.models.MovieShowing;
import com.example.mowgliupgrade.models.Reservation;

import org.parceler.Parcels;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {
    Context context;
    List<Reservation> reservations;


    public ReservationAdapter(Context context, List<Reservation> reservations){
        this.context = context;
        this.reservations = reservations;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ReservationAdapter", "onCreateViewHolder");
        View reservationView = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false );
        return new ViewHolder(reservationView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("ReservationAdapter", "onBindViewHolder " + position);
        Reservation reservation = reservations.get(position);
        holder.bind(reservation);
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout reservationContainer;
        TextView tvTitle, tvShowingDateTime, tvOrderNumber, tvRuntime, tvGenres;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvShowingDateTime = itemView.findViewById(R.id.tvShowingDateTime);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            tvRuntime = itemView.findViewById(R.id.tvRuntime);
            tvGenres = itemView.findViewById(R.id.tvGenres);
            reservationContainer = itemView.findViewById(R.id.reservationContainer);
            ivPoster = itemView.findViewById(R.id.ivPoster);

        }

        public void bind(Reservation reservation) {
            MovieShowing movieShowing = reservation.getMovieShowing();

            if (movieShowing != null) {
                if (movieShowing.getMovie() != null) {
                    tvTitle.setText(movieShowing.getMovie().getTitle());
                    tvOrderNumber.setText("#" + reservation.getReservationId().substring(1, 9));

                    String runtime = movieShowing.getMovie().getRunTime() + " mins";
                    tvRuntime.setText(runtime);


                    String showingDateTime = movieShowing.getShowDate().substring(0, movieShowing.getShowDate().length()-6) + "  |  " + movieShowing.getShowTime();
                    tvShowingDateTime.setText(showingDateTime);

                    tvGenres.setText(movieShowing.getGenres());

                    Glide.with(context).load(movieShowing.getMovie().getPosterPath()).into(ivPoster);

                    //todo: show full movie detail when user clicks on a reservation
//                    reservationContainer.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            //Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show();
//
//
//                            //Intent- First parameter is the context; second is the class of the activity to launch
//                            Intent i = new Intent(context, ReservationDetailActivity.class);
//                            i.putExtra("reservation", Parcels.wrap(reservation));
//                            context.startActivity(i); //Brings up second activity
//                        }
//                    });
                }

            }

        }
    }
}
