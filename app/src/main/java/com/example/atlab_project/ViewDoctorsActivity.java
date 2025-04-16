package com.example.atlab_project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewDoctorsActivity extends AppCompatActivity {

    DatabaseHelper db;
    ListView listDoctors;
    EditText searchInput;
    ArrayList<String> doctorList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctors);

        listDoctors = findViewById(R.id.list_doctors);
        searchInput = findViewById(R.id.search_input);
        db = new DatabaseHelper(this);
        doctorList = new ArrayList<>();

        Cursor cursor = db.getAllDoctors();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String specialization = cursor.getString(cursor.getColumnIndexOrThrow("specialization"));
            doctorList.add("Dr. " + name + " - " + specialization);
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, doctorList);
        listDoctors.setAdapter(adapter);
        listDoctors.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDoctor = adapter.getItem(position);
            String doctorName = selectedDoctor.split("Dr. ")[1].split(" - ")[0].trim();  // Extract name

            Intent intent = new Intent(ViewDoctorsActivity.this, BookAppointmentActivity.class);
            intent.putExtra("doctor_name", doctorName);
            startActivity(intent);
        });


        // Add text change listener for searching
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s); // simple filter
            }

            @Override
            public void afterTextChanged(Editable s) { }


        });
    }
}
