package com.example.travelplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    int tripId;
    String dest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Receive data from Adapter
        tripId = getIntent().getIntExtra("TRIP_ID", -1);
        dest = getIntent().getStringExtra("DESTINATION");


        findViewById(R.id.btnExpense).setOnClickListener(v -> {
            Intent i = new Intent(this, ExpenseActivity.class);
            i.putExtra("TRIP_ID", tripId);
            startActivity(i);
        });

        findViewById(R.id.btnCalendar).setOnClickListener(v -> {
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnMap).setOnClickListener(v -> {
            Intent i = new Intent(this, MapActivity.class);
            i.putExtra("TRIP_ID", tripId);
            i.putExtra("DESTINATION", dest);
            startActivity(i);
        });

        findViewById(R.id.btnNotes).setOnClickListener(v -> {
            Intent i = new Intent(this, NotesActivity.class);
            i.putExtra("TRIP_ID", tripId);
            startActivity(i);
        });

        findViewById(R.id.btnTrips).setOnClickListener(v -> {
            // Using DashboardActivity.this is safer than just 'this'
            Intent intent = new Intent(DashboardActivity.this, TripsActivity.class);
            startActivity(intent);
        });
    }
}