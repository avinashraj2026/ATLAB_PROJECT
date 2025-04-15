package com.example.atlab_project;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class BookAppointmentActivity extends AppCompatActivity {

    private Spinner spinnerDoctors;
    private Button btnPickDate, btnBook;
    private TextView textSelectedDate;

    private DatabaseHelper db;
    private ArrayList<String> doctorNames;
    private HashMap<String, Integer> doctorMap;

    private int selectedYear, selectedMonth, selectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        db = new DatabaseHelper(this);
        doctorMap = new HashMap<>();

        spinnerDoctors = findViewById(R.id.spinner_doctors);
        btnPickDate = findViewById(R.id.btn_pick_date);
        btnBook = findViewById(R.id.btn_book);
        textSelectedDate = findViewById(R.id.text_selected_date);

        loadDoctors();

        btnPickDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(
                    BookAppointmentActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        selectedYear = year;
                        selectedMonth = month + 1;
                        selectedDay = dayOfMonth;
                        String dateStr = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                        textSelectedDate.setText(dateStr);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });

        btnBook.setOnClickListener(v -> {
            String selectedDoctor = spinnerDoctors.getSelectedItem().toString();
            String selectedDate = textSelectedDate.getText().toString();

            if (selectedDoctor.isEmpty() || selectedDate.equals("No date selected")) {
                Toast.makeText(this, "Please select both doctor and date", Toast.LENGTH_SHORT).show();
                return;
            }

            int doctorId = doctorMap.get(selectedDoctor);

            // Fetch userId from SharedPreferences (you should save it during login)
            SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
            int userId = prefs.getInt("userId", -1);

            if (userId == -1) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = db.insertAppointment(userId, doctorId, selectedDate);

            if (success) {
                Toast.makeText(this, "Appointment booked!", Toast.LENGTH_SHORT).show();
                // Optional: clear date selection or finish()
            } else {
                Toast.makeText(this, "Booking failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDoctors() {
        doctorNames = new ArrayList<>();
        Cursor cursor = db.getAllDoctors();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String specialization = cursor.getString(cursor.getColumnIndexOrThrow("specialization"));
            String displayName = name + " - " + specialization;

            doctorNames.add(displayName);
            doctorMap.put(displayName, id);
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, doctorNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDoctors.setAdapter(adapter);
    }
}
