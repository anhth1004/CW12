package com.example.cw12.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cw12.R;

import java.util.ArrayList;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.MyViewHolder> {
    private final Context context;
    Activity activity;
    private final ArrayList<String> hikeId;
    private final ArrayList<String> hikeName;
    private final ArrayList<String> hikeLocation;
    private final ArrayList<String> hikeDate;
    private final ArrayList<String> hikeParkingAvailable;
    private final ArrayList<String> hikeDescription;
    int position;

    public HikeAdapter(Context context,
                       Activity activity,
                       ArrayList<String> hikeId,
                       ArrayList<String> hikeName,
                       ArrayList<String> hikeLocation,
                       ArrayList<String> hikeDate,
                       ArrayList<String> hikeParkingAvailable,
                       ArrayList<String> hikeDescription){
        this.context = context;
        this.activity = activity;
        this.hikeId = hikeId;
        this.hikeName = hikeName;
        this.hikeLocation = hikeLocation;
        this.hikeDate = hikeDate;
        this.hikeParkingAvailable = hikeParkingAvailable;
        this.hikeDescription = hikeDescription;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater .from(context);
        View view = inflater.inflate(R.layout.item_hike, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        this.position = position;
        holder.hike_id_text.setText(String.valueOf(hikeId.get(position)));
        holder.hike_name_text.setText(String.valueOf(hikeName.get(position)));
        holder.hike_location_text.setText(String.valueOf(hikeLocation.get(position)));
        holder.hike_date_text.setText(String.valueOf(hikeDate.get(position)));
        holder.hike_parking_available_text.setText("Parking Available: " + String.valueOf(hikeParkingAvailable.get(position)));

        holder.mainLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, ViewHikeActivity.class);

            intent.putExtra("hike_id", String.valueOf(hikeId.get(position)));
            intent.putExtra("name", String.valueOf(hikeName.get(position)));
            intent.putExtra("destination", String.valueOf(hikeLocation.get(position)));
            intent.putExtra("date", String.valueOf(hikeDate.get(position)));
            intent.putExtra("parking_available", String.valueOf(hikeParkingAvailable.get(position)));
            intent.putExtra("desc", String.valueOf(hikeDescription.get(position)));
            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return hikeId.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView hike_id_text, hike_name_text, hike_location_text, hike_date_text, hike_parking_available_text;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hike_id_text = itemView.findViewById(R.id.hike_id_text);
            hike_name_text = itemView.findViewById(R.id.hike_name_text);
            hike_location_text = itemView.findViewById(R.id.hike_location_text);
            hike_date_text = itemView.findViewById(R.id.hike_date_text);
            hike_parking_available_text = itemView.findViewById(R.id.trip_risk_text);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
