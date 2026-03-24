package com.example.travelplanner;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ExpenseActivity extends AppCompatActivity {
    DatabaseHelper db;
    int tripId;
    EditText etAmount, etCategory;
    TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        db = new DatabaseHelper(this);
        tripId = getIntent().getIntExtra("TRIP_ID", -1);

        etAmount = findViewById(R.id.etExpenseAmount);
        etCategory = findViewById(R.id.etExpenseCategory);
        tvTotal = findViewById(R.id.tvTotalExpense);
        Button btnAdd = findViewById(R.id.btnAddExpense);

        updateTotal();

        btnAdd.setOnClickListener(v -> {
            String amt = etAmount.getText().toString();
            String cat = etCategory.getText().toString();
            if(!amt.isEmpty()) {
                db.getWritableDatabase().execSQL(
                        "INSERT INTO expenses (trip_id, amount, category) VALUES (?, ?, ?)",
                        new Object[]{tripId, Double.parseDouble(amt), cat}
                );
                updateTotal();
                etAmount.setText("");
                etCategory.setText("");
                Toast.makeText(this, "Expense Added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotal() {
        Cursor cursor = db.getReadableDatabase().rawQuery(
                "SELECT SUM(amount) FROM expenses WHERE trip_id = ?",
                new String[]{String.valueOf(tripId)});
        if (cursor.moveToFirst()) {
            tvTotal.setText("Trip Total: ₹" + cursor.getDouble(0));
        }
        cursor.close();
    }
}