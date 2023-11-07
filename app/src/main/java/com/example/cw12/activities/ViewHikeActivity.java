package com.example.cw12.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cw12.R;
import com.example.cw12.database.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ViewHikeActivity extends AppCompatActivity {

    private TextView textViewHikeName, textViewLocation, textViewDate, textViewParkingAvailable,
            textViewHikeLength, textViewDifficulty, textViewDescription;
    private RecyclerView recyclerViewObservations;
    private Button buttonAddObservation;
    private MyDatabaseHelper databaseHelper;
    private List<String> observationList;
    private ObservationsAdapter observationsAdapter;
    private String hikeId, hikeName;
    ArrayList<String> observation_id, observation, date_of_observation, comment;

    private List<String> hike_id, name, location, date, parkingAvailable, desc;
    AlertDialog.Builder builder;
    Spinner difficultyLevelSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hike_detail);

        // Initialize views
        textViewHikeName = findViewById(R.id.textViewHikeName);
        textViewLocation = findViewById(R.id.textViewLocation);
        textViewDate = findViewById(R.id.textViewDate);
        textViewParkingAvailable = findViewById(R.id.textViewParkingAvailable);
        textViewHikeLength = findViewById(R.id.textViewHikeLength);
        textViewDifficulty = findViewById(R.id.textViewDifficulty);
        textViewDescription = findViewById(R.id.textViewDescription);

        recyclerViewObservations = findViewById(R.id.recyclerViewObservations);
        Button buttonEditHike = findViewById(R.id.buttonEditHike);
        buttonEditHike.setOnClickListener(view -> {
            Intent observationIntent = new Intent(ViewHikeActivity.this, UpdateActivity.class);
            observationIntent.putExtra("hike_id", hikeId);
            startActivityForResult(observationIntent, 2);
        });
        buttonAddObservation = findViewById(R.id.buttonAddObservation);

        // Initialize database helper
        databaseHelper = new MyDatabaseHelper(this);

        // Get hike ID from intent
        Intent intent = getIntent();
        if (intent.hasExtra("hike_id")) {
            hikeId = intent.getStringExtra("hike_id");
            loadHikeDetails(hikeId);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }

        // Set up RecyclerView and Adapter for observations
        databaseHelper = new MyDatabaseHelper(ViewHikeActivity.this);
        observation_id = new ArrayList<>();
        hike_id = new ArrayList<>();
        observation = new ArrayList<>();
        date_of_observation = new ArrayList<>();
        comment = new ArrayList<>();

        storeDataInArrays(hikeId);

        observationsAdapter = new ObservationsAdapter(ViewHikeActivity.this,
                this,
                observation_id,
                observation,
                date_of_observation,
                comment,
                String.valueOf(hikeId));

        recyclerViewObservations.setAdapter(observationsAdapter);
        recyclerViewObservations.setLayoutManager(new LinearLayoutManager(ViewHikeActivity.this));

        // Set a click listener for the "Add Observation" button
        buttonAddObservation.setOnClickListener(view -> {
            Intent observationIntent = new Intent(ViewHikeActivity.this, AddObservationActivity.class);
            observationIntent.putExtra("hike_id", hikeId);
            startActivityForResult(observationIntent, 1);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all_button) {
            deleteHike();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Load hike details from the database and update the UI
    private void loadHikeDetails(String hikeId) {
        Cursor cursor = databaseHelper.readAllData();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(databaseHelper.getColumnId()));
                if (id.equals(hikeId)) {
                    String name = cursor.getString(1);
                    String location = cursor.getString(2);
                    String date = cursor.getString(3);
                    String parkingAvailable = cursor.getString(5);
                    String hikeLength = cursor.getString(4);
                    String difficulty = cursor.getString(6);; // Replace with the actual difficulty level
                    String description = cursor.getString(7);

                    textViewHikeName.setText("Hike Name: " + name);
                    textViewLocation.setText("Location: " + location);
                    textViewDate.setText("Date: " + date);
                    textViewParkingAvailable.setText("Parking Available: " + parkingAvailable);
                    textViewHikeLength.setText("Length of the Hike: " + hikeLength);
                    textViewDifficulty.setText("Difficulty Level: " + difficulty);
                    textViewDescription.setText("Description: " + description);

                    break;
                }
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    // Load observations for the specified hike from the database and update the RecyclerView
    void storeDataInArrays(String hikeId) {
        Cursor cursor = databaseHelper.readAllExpenses(hikeId);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                observation_id.add(cursor.getString(0));
                hike_id.add(cursor.getString(1));
                observation.add(cursor.getString(2));
                date_of_observation.add(cursor.getString(3));
                comment.add(cursor.getString(4));
            }
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }
    void deleteHike() {
        builder.setTitle("Delete " + hikeName + "?");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            MyDatabaseHelper databaseHelper = new MyDatabaseHelper(ViewHikeActivity.this);
            databaseHelper.deleteOneRow(hikeId);
            finish();
        });

        builder.setNegativeButton("No", (dialogInterface, i) -> {
        });

        builder.create().show();
    }
}
