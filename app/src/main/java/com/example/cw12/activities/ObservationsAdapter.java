package com.example.cw12.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cw12.R;

import java.util.ArrayList;

public class ObservationsAdapter extends RecyclerView.Adapter<ObservationsAdapter.MyViewHolder> {
    private final Context context;
    Activity activity;
    private final ArrayList<String> observationId;
    private final ArrayList<String> observation;
    private final ArrayList<String> dateOfObservation;
    private final ArrayList<String> comments;
    int position;
    String hikeId;

    public ObservationsAdapter(Context context,
                               Activity activity,
                               ArrayList<String> observationId,
                               ArrayList<String> observation,
                               ArrayList<String> comments,
                               ArrayList<String> dateOfObservation,
                               String hikeId){
        this.context = context;
        this.activity = activity;
        this.observationId = observationId;
        this.observation = observation;
        this.comments = comments;
        this.dateOfObservation = dateOfObservation;
        this.hikeId = hikeId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_observation, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        this.position = position;
        holder.observation_text.setText(String.valueOf(observation.get(position)));
        holder.date_of_observation_text.setText(String.valueOf(dateOfObservation.get(position)));
        holder.comments_text.setText(String.valueOf(comments.get(position)));
    }

    @Override
    public int getItemCount() {
        return observationId.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView observation_text, date_of_observation_text, comments_text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            observation_text = itemView.findViewById(R.id.textViewObservation);
            date_of_observation_text = itemView.findViewById(R.id.textViewDateOfObservation);
            comments_text = itemView.findViewById(R.id.textViewComments);
        }
    }
}
