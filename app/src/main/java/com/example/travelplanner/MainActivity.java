package com.example.travelplanner;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    // Declare the views from your XML
    DatabaseHelper dbHelper;
    TextInputEditText etUser, etPass;
    Button btnAction;
    TextView tvToggle;
    TabLayout authTabs;
    boolean isLoginMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Fix the window insets (This matches your android:id="@+id/main")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        dbHelper = new DatabaseHelper(this);
        etUser = findViewById(R.id.etUsername);
        etPass = findViewById(R.id.etPassword);
        btnAction = findViewById(R.id.btnAction);
        tvToggle = findViewById(R.id.tvToggle);
        authTabs = findViewById(R.id.authTabs);

        // Logic for Switching Tabs (Login vs Sign Up)
        authTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) { // Login Tab
                    setMode(true);
                } else { // Sign Up Tab
                    setMode(false);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Toggle text also switches the mode
        tvToggle.setOnClickListener(v -> {
            isLoginMode = !isLoginMode;
            authTabs.selectTab(authTabs.getTabAt(isLoginMode ? 0 : 1));
            setMode(isLoginMode);
        });

        btnAction.setOnClickListener(v -> handleAuth());
    }

    private void setMode(boolean login) {
        isLoginMode = login;
        btnAction.setText(login ? "Login" : "Create Account");
        tvToggle.setText(login ? "New user? Register here" : "Have an account? Login");
    }

    private void handleAuth() {
        String user = etUser.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        if (isLoginMode) {
            // Check Database for User
            Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[]{user, pass});
            if (cursor.getCount() > 0) {
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } else {
            // Sign Up Logic
            ContentValues cv = new ContentValues();
            cv.put("username", user);
            cv.put("password", pass);
            long result = dbHelper.getWritableDatabase().insert("users", null, cv);
            if (result != -1) {
                Toast.makeText(this, "Account Created! Please Login", Toast.LENGTH_SHORT).show();
                authTabs.selectTab(authTabs.getTabAt(0)); // Move to Login tab
            }
        }
    }
}