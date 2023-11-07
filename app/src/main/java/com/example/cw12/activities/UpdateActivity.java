package com.example.cw12.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.cw12.R;
import com.example.cw12.database.MyDatabaseHelper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class UpdateActivity extends AppCompatActivity {

    EditText hikeNameInput, locationInput, selectDate, descriptionInput;
    Button updateButton, seeObservationsButton;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textViewSelectDate;

    String hikeId, hikeName, location, date, parkingAvailable, hikeDescription;
    AlertDialog.Builder builder;
    Spinner difficultyLevelSpinner;
    private Calendar selectedDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hike);

        hikeNameInput = findViewById(R.id.editTextHikeName);
        locationInput = findViewById(R.id.editTextLocation);
        textViewSelectDate = findViewById(R.id.textViewSelectDate);
        radioGroup = findViewById(R.id.radio_group_parking);
        descriptionInput = findViewById(R.id.editTextDescription);
        updateButton = findViewById(R.id.buttonSaveChanges);
        textViewSelectDate.setOnClickListener(view -> showDatePicker());
        builder = new AlertDialog.Builder(this);
        getAndSetIntentData();

        // Khai báo và kết nối Spinner với tài nguyên string-array difficulty_levels
        difficultyLevelSpinner = findViewById(R.id.spinnerDifficulty);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.difficulty_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultyLevelSpinner.setAdapter(adapter);

        updateButton.setOnClickListener(view -> {
            MyDatabaseHelper db = new MyDatabaseHelper(UpdateActivity.this);
            hikeName = hikeNameInput.getText().toString().trim();
            location = locationInput.getText().toString().trim();
            date = selectDate.getText().toString().trim();
            int radioButtonId = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(radioButtonId);
            parkingAvailable = radioButton.getText().toString().trim();
            hikeDescription = descriptionInput.getText().toString().trim();

            // Lấy giá trị từ Spinner
            String selectedDifficultyLevel = difficultyLevelSpinner.getSelectedItem().toString();

            db.updateHike(hikeId, hikeName, location, date, parkingAvailable, hikeDescription, selectedDifficultyLevel);
        });

//        seeObservationsButton.setOnClickListener(view -> {
//           Intent intent = new Intent(UpdateActivity.this, ObservationsActivity.class);
//            intent.putExtra("hike_id", String.valueOf(hikeId));
//            startActivity(intent);
//        });
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

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new UpdateDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "date1");
    }

    public void editDate(LocalDate date) {
        TextView dateText = findViewById(R.id.textViewSelectDate);
        dateText.setText(date.toString());
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("hike_id")
                && getIntent().hasExtra("hike_name")
                && getIntent().hasExtra("location")
                && getIntent().hasExtra("date")
                && getIntent().hasExtra("parking_available")
                && getIntent().hasExtra("desc")
                && getIntent().hasExtra("difficulty_level")) {

            // ...

            String difficultyLevel = getIntent().getStringExtra("difficulty_level");
            setDifficultyLevelSpinner(difficultyLevel);

            // ...
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to set the selected item in the Spinner
    private void setDifficultyLevelSpinner(String difficultyLevel) {
        ArrayAdapter adapter = (ArrayAdapter) difficultyLevelSpinner.getAdapter();
        int position = adapter.getPosition(difficultyLevel);
        difficultyLevelSpinner.setSelection(position);
    }

    void deleteHike() {
        builder.setTitle("Delete " + hikeName + "?");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            MyDatabaseHelper databaseHelper = new MyDatabaseHelper(UpdateActivity.this);
            databaseHelper.deleteOneRow(hikeId);
            finish();
        });

        builder.setNegativeButton("No", (dialogInterface, i) -> {
        });

        builder.create().show();
    }
    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, month);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(selectedDate.getTime());
            textViewSelectDate.setText(formattedDate);
        };

        new DatePickerDialog(this, dateSetListener, selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH)).show();
    }
}






