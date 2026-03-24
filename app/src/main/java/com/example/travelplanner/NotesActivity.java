package com.example.travelplanner;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    int tripId;
    EditText etNote;
    ListView lvNotes;
    ArrayList<String> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        dbHelper = new DatabaseHelper(this);
        tripId = getIntent().getIntExtra("TRIP_ID", -1);

        etNote = findViewById(R.id.etNoteContent);
        lvNotes = findViewById(R.id.lvNotes);
        Button btnSave = findViewById(R.id.btnSaveNote);

        loadNotes();

        btnSave.setOnClickListener(v -> {
            String noteText = etNote.getText().toString().trim();
            if (!noteText.isEmpty()) {
                ContentValues cv = new ContentValues();
                cv.put("trip_id", tripId);
                cv.put("content", noteText);
                dbHelper.getWritableDatabase().insert("notes", null, cv);
                etNote.setText("");
                loadNotes();
            }
        });
    }

    private void loadNotes() {
        notesList = new ArrayList<>();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT content FROM notes WHERE trip_id = ? ORDER BY id DESC",
                new String[]{String.valueOf(tripId)});

        while (cursor.moveToNext()) {
            notesList.add(cursor.getString(0));
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        lvNotes.setAdapter(adapter);
    }
}