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
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Trip trip = new Trip();
                trip.id = cursor.getInt(0);
                trip.destination = cursor.getString(1);
                trip.startDate = cursor.getString(2);
                trip.endDate = cursor.getString(3);
                tripList.add(trip);
            } while (cursor.moveToNext());
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
                    String dest = etDest.getText().toString();
                    String start = etStart.getText().toString();
                    String end = etEnd.getText().toString();

                    if (!dest.isEmpty()) {
                        db.addTrip(dest, start, end);
                        refreshTripList();
                        Toast.makeText(this, "Trip to " + dest + " saved!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}