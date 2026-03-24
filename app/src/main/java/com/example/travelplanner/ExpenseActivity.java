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
    EditText etAmount, etCategory;
    TextView tvTotalExpense;
    Button btnAddExpense;
    int tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        db = new DatabaseHelper(this);
        tripId = getIntent().getIntExtra("TRIP_ID", -1);

        etAmount = findViewById(R.id.etExpenseAmount);
        etCategory = findViewById(R.id.etExpenseCategory);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        btnAddExpense = findViewById(R.id.btnAddExpense);

        updateTotal();

        btnAddExpense.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString();
            String category = etCategory.getText().toString();

            if (!amountStr.isEmpty() && !category.isEmpty()) {
                double amount = Double.parseDouble(amountStr);
                // MODIFICATION: Insert with actual tripId
                db.getWritableDatabase().execSQL(
                        "INSERT INTO expenses (trip_id, amount, category) VALUES (?, ?, ?)",
                        new Object[]{tripId, amount, category}
                );

                etAmount.setText("");
                etCategory.setText("");
                updateTotal();
                Toast.makeText(this, "Expense Saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotal() {
        // MODIFICATION: Filter SUM by trip_id
        Cursor cursor = db.getReadableDatabase().rawQuery(
                "SELECT SUM(amount) FROM expenses WHERE trip_id = ?",
                new String[]{String.valueOf(tripId)});

        if (cursor.moveToFirst()) {
            double total = cursor.getDouble(0);
            tvTotalExpense.setText("Trip Total: ₹" + total);
        }
        cursor.close();
    }
}