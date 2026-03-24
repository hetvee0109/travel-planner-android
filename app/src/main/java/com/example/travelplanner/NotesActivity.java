package com.example.travelplanner;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    EditText etNote;
    Button btnSaveNote;
    ListView lvNotes;
    ArrayList<String> notesList;
    ArrayAdapter<String> adapter;
    int tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        dbHelper = new DatabaseHelper(this);
        tripId = getIntent().getIntExtra("TRIP_ID", -1);

        etNote = findViewById(R.id.etNoteContent);
        btnSaveNote = findViewById(R.id.btnSaveNote);
        lvNotes = findViewById(R.id.lvNotes);

        loadNotes();

        btnSaveNote.setOnClickListener(v -> {
            String noteText = etNote.getText().toString().trim();
            if (!noteText.isEmpty()) {
                saveNoteToDb(noteText);
                etNote.setText("");
                loadNotes();
            }
        });
    }

    private void saveNoteToDb(String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("trip_id", tripId); // Link to trip
        cv.put("content", content);
        db.insert("notes", null, cv);
    }

    private void loadNotes() {
        notesList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // MODIFICATION: Filter by trip_id
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE trip_id = ? ORDER BY id DESC",
                new String[]{String.valueOf(tripId)});

        if (cursor.moveToFirst()) {
            do {
                notesList.add(cursor.getString(cursor.getColumnIndexOrThrow("content")));
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        lvNotes.setAdapter(adapter);
    }
}