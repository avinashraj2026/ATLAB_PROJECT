package com.example.atlab_project;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MyAppointmentsActivity extends AppCompatActivity {

    DatabaseHelper db;
    ListView listAppointments;
    ArrayList<String> appointmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointments);

        try {
            listAppointments = findViewById(R.id.list_appointments);
            db = new DatabaseHelper(this);

            int userId = getSharedPreferences("user", MODE_PRIVATE).getInt("userId", -1);

            if (userId == -1) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<String> appointmentList = new ArrayList<>();
            Cursor cursor = db.getAppointmentsForUser(userId);

            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String doctorName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String specialization = cursor.getString(cursor.getColumnIndexOrThrow("specialization"));

                String entry = "Dr. " + doctorName + " (" + specialization + ") - " + date;
                appointmentList.add(entry);
            }

            cursor.close();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, appointmentList);
            listAppointments.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();  // Shows error in Logcat
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
