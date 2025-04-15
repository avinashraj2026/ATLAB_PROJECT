package com.example.atlab_project;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.database.Cursor;

public class ViewDoctorsActivity extends AppCompatActivity {

    ListView listDoctors;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctors);

        listDoctors = findViewById(R.id.list_doctors);
        db = new DatabaseHelper(this);

        ArrayList<String> doctorList = new ArrayList<>();
        Cursor cursor = db.getAllDoctors(); // We'll add this method next

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String specialization = cursor.getString(cursor.getColumnIndexOrThrow("specialization"));
            doctorList.add("Dr. " + name + " - " + specialization);
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, doctorList);
        listDoctors.setAdapter(adapter);
    }
}
