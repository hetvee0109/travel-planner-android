package com.example.travelplanner;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripViewHolder> {
    private List<Trip> tripList;

    public TripsAdapter(List<Trip> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.destination.setText(trip.destination);
        holder.dates.setText(trip.startDate + " - " + trip.endDate);

        // This links the trip to the Dashboard
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DashboardActivity.class);
            intent.putExtra("TRIP_ID", trip.id);
            intent.putExtra("DESTINATION", trip.destination);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return tripList.size(); }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView destination, dates;
        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            destination = itemView.findViewById(R.id.tvTripDestination);
            dates = itemView.findViewById(R.id.tvTripDates);
        }
    }
}