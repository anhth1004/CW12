package com.example.cw12.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.cw12.R;
import com.example.cw12.database.MyDatabaseHelper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {
    EditText hikeNameInput, locationInput, hikeLengthInput, descriptionInput;
    TextView dateOfHikeInput;
    Button addHikeButton;
    RadioGroup parkingRadioGroup;
    RadioButton parkingRadioButton;
    TextView textViewSelectDate;
    Spinner difficultyLevelSpinner;
    private Calendar selectedDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);

        hikeNameInput = findViewById(R.id.editTextHikeName);
        locationInput = findViewById(R.id.editTextLocation);
        hikeLengthInput = findViewById(R.id.editTextHikeLength);
        dateOfHikeInput = findViewById(R.id.textViewSelectDate);
        descriptionInput = findViewById(R.id.editTextDescription);
        addHikeButton = findViewById(R.id.buttonAddHike);
        parkingRadioGroup = findViewById(R.id.radio_group_parking);
        textViewSelectDate = findViewById(R.id.textViewSelectDate);

        // Khai báo và kết nối Spinner với tài nguyên string-array difficulty_levels
        difficultyLevelSpinner = findViewById(R.id.spinnerDifficulty);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.difficulty_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultyLevelSpinner.setAdapter(adapter);

        textViewSelectDate.setOnClickListener(view -> showDatePicker());

        addHikeButton.setOnClickListener(view -> {
            int radioId = parkingRadioGroup.getCheckedRadioButtonId();
            parkingRadioButton = findViewById(radioId);

            if (hikeNameInput.getText().toString().trim().isEmpty() ||
                    locationInput.getText().toString().trim().isEmpty() ||
                    hikeLengthInput.getText().toString().trim().isEmpty() ||
                    dateOfHikeInput.getText().toString().trim().isEmpty() ||
                    parkingRadioButton.getText().toString().trim().isEmpty() ||
                    descriptionInput.getText().toString().trim().isEmpty()) {
                Toast.makeText(AddActivity.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            } else {
                MyDatabaseHelper DB = new MyDatabaseHelper(AddActivity.this);

                // Lấy giá trị từ Spinner
                String selectedDifficultyLevel = difficultyLevelSpinner.getSelectedItem().toString();

                DB.addHike(
                        hikeNameInput.getText().toString().trim(),
                        locationInput.getText().toString().trim(),
                        dateOfHikeInput.getText().toString().trim(),
                        hikeLengthInput.getText().toString().trim(),
                        parkingRadioButton.getText().toString().trim(),
                        selectedDifficultyLevel, // Truyền giá trị difficultyLevel vào phương thức addHike
                        descriptionInput.getText().toString().trim()

                );
                Toast.makeText(AddActivity.this, "Hike added successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, month);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(selectedDate.getTime());
            dateOfHikeInput.setText(formattedDate);
        };

        new DatePickerDialog(this, dateSetListener, selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void updateDate(LocalDate date){
        TextView dateText = (TextView) findViewById(R.id.textViewSelectDate);
        dateText.setText(date.toString());
    }
}