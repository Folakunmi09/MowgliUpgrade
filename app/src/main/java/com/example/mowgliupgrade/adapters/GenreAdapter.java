package com.example.mowgliupgrade.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mowgliupgrade.DetailActivity;
import com.example.mowgliupgrade.R;
import com.example.mowgliupgrade.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    Context context;
    List<String> genres;

    public GenreAdapter(Context context, List<String> genres){
        this.context = context;
        this.genres = genres;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("GenreAdapter", "onCreateViewHolder");
        View genreView = LayoutInflater.from(context).inflate(R.layout.item_genre, parent, false );
        return new ViewHolder(genreView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("GenreAdapter", "onBindViewHolder " + position);
        //Get the genre at the passed position
        String genre = genres.get(position);
        //Bind the movie data into the View Holder
        holder.bind(genre);
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvGenre;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvGenre = itemView.findViewById(R.id.tvGenre);
        }

        public void bind(String genre) {
            tvGenre.setText(genre);
        }
    }
}
