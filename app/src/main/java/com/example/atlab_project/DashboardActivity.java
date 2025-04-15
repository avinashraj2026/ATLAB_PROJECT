package com.example.atlab_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.URL;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    Button btnBook, btnDoctors, btnMyAppointments, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnBook = findViewById(R.id.btn_book_appointment);
        btnDoctors = findViewById(R.id.btn_view_doctors);
        btnMyAppointments = findViewById(R.id.btn_my_appointments);
        btnLogout = findViewById(R.id.btn_logout);

        btnBook.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, BookAppointmentActivity.class)));

        btnDoctors.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, ViewDoctorsActivity.class)));


        btnMyAppointments.setOnClickListener(v -> {
                    Intent intent = new Intent(DashboardActivity.this, MyAppointmentsActivity.class);
                    startActivity(intent);
                });


            btnLogout.setOnClickListener(v -> {
            // Optional: Clear SharedPreferences if using user sessions
            // Redirect to login
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        Button videoConsultBtn = findViewById(R.id.btn_video_consult);
        videoConsultBtn.setOnClickListener(v -> {
            try {
                JitsiMeetConferenceOptions options
                        = new JitsiMeetConferenceOptions.Builder()
                        .setServerURL(new URL("https://meet.jit.si"))
                        .setRoom("DoctorConsultRoom123") // You can generate a unique room if needed
                        .setAudioMuted(false)
                        .build();
                JitsiMeetActivity.launch(this, options);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error starting video call", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
