package com.example.mowgliupgrade.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
//import com.example.mowgliupgrade.DetailActivity;
import com.example.mowgliupgrade.DetailActivity;
import com.example.mowgliupgrade.R;
import com.example.mowgliupgrade.models.Movie;
import com.example.mowgliupgrade.models.Movie;

//import org.parceler.Parcels;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    Context context;
    List<Movie> movies;


    public MovieAdapter(Context context, List<Movie> movies){
        this.context = context;
        this.movies = movies;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false );
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        //Get the movie at the passed position
        Movie movie = movies.get(position);
        //Bind the movie data into the View Holder
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout itemContainer;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);

            //            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            itemContainer = itemView.findViewById(R.id.itemContainer);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            String imageUrl;
            imageUrl = movie.getPosterPath();



            Glide.with(context).load(imageUrl).into(ivPoster);
            itemContainer.setOnClickListener(new View.OnClickListener()  {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(i); //Brings up second activity
                }
            });

        }
    }
}
