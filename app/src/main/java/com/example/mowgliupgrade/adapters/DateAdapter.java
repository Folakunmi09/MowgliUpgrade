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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    Context context;
    List<Date> dates;

    public DateAdapter(Context context, List<Date> dates){
        this.context = context;
        this.dates = dates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("DateAdapter", "onCreateViewHolder");
        View dateView = LayoutInflater.from(context).inflate(R.layout.item_date, parent, false );
        return new ViewHolder(dateView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("DateAdapter", "onBindViewHolder " + position);
        Date date = dates.get(position);
        //Bind the movie data into the View Holder
        holder.bind(date);
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvDayOfTheWeek;
        TextView tvDayOfTheMonth;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvDayOfTheMonth = itemView.findViewById(R.id.tvDayOfMonth);
            tvDayOfTheWeek = itemView.findViewById(R.id.tvDayOfWeek);


        }

        public void bind(Date date) {
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            tvDayOfTheWeek.setText(dayFormat.format(date).substring(0, 3).toUpperCase(Locale.ROOT));

            SimpleDateFormat monthFormat = new SimpleDateFormat("dd");
            tvDayOfTheMonth.setText(monthFormat.format(date));

        }
    }
}
