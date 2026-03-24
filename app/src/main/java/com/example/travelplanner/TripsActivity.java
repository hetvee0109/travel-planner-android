package com.example.travelplanner;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class TripsActivity extends AppCompatActivity {
    DatabaseHelper db;
    RecyclerView recyclerView;
    TripsAdapter adapter;
    List<Trip> tripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.rvTrips);
        tripList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TripsAdapter(tripList);
        recyclerView.setAdapter(adapter);

        refreshTripList();

        findViewById(R.id.btnAddNewTrip).setOnClickListener(v -> showAddTripDialog());
    }

    private void refreshTripList() {
        tripList.clear();
        Cursor cursor = db.getAllTrips();
        if (cursor != null) {
            // Use column names instead of hardcoded numbers (0, 1, 2) to prevent crashes
            int idIndex = cursor.getColumnIndex("id");
            int destIndex = cursor.getColumnIndex("destination");
            int startIndex = cursor.getColumnIndex("start_date");
            int endIndex = cursor.getColumnIndex("end_date");

            while (cursor.moveToNext()) {
                Trip trip = new Trip();
                trip.id = cursor.getInt(idIndex);
                trip.destination = cursor.getString(destIndex);
                trip.startDate = cursor.getString(startIndex);
                trip.endDate = cursor.getString(endIndex);
                tripList.add(trip);
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    private void showAddTripDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_trip, null);
        TextInputEditText etDest = view.findViewById(R.id.etDest);
        TextInputEditText etStart = view.findViewById(R.id.etStart);
        TextInputEditText etEnd = view.findViewById(R.id.etEnd);

        new AlertDialog.Builder(this)
                .setTitle("Plan New Journey")
                .setView(view)
                .setPositiveButton("Add", (dialog, which) -> {
                    String dest = etDest.getText().toString().trim();
                    String start = etStart.getText().toString().trim();
                    String end = etEnd.getText().toString().trim();

                    if (!dest.isEmpty() && !start.isEmpty() && !end.isEmpty()) {
                        db.addTrip(dest, start, end);
                        refreshTripList();
                        Toast.makeText(this, "Trip to " + dest + " saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}