package com.example.mowgliupgrade.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mowgliupgrade.R;
import com.example.mowgliupgrade.models.Cast;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {
    Context context;
    List<Cast> castList;

    public CastAdapter(Context context, List<Cast> castList){
        this.context = context;
        this.castList = castList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("CastAdapter", "onCreateViewHolder");
        View castView = LayoutInflater.from(context).inflate(R.layout.item_cast, parent, false );
        return new ViewHolder(castView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("CastAdapter", "onBindViewHolder " + position);
        //Get the cast at the passed position
        Cast cast = castList.get(position);
        //Bind the movie data into the View Holder
        holder.bind(cast);
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivCastImage;
        TextView tvCastName;
        TextView tvCharacterName;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            ivCastImage = itemView.findViewById(R.id.ivCastImage);
            tvCastName = itemView.findViewById(R.id.tvCastName);
            tvCharacterName = itemView.findViewById(R.id.tvCharacterName);


        }

        public void bind(Cast cast) {
            tvCastName.setText(cast.getCastName());
            tvCharacterName.setText(cast.getCharacter());
            Glide.with(context).load(cast.getProfilePath()).into(ivCastImage);

        }
    }
}
