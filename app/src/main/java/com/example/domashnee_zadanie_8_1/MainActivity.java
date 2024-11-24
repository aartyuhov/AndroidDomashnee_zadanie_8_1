package com.example.domashnee_zadanie_8_1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.domashnee_zadanie_8_1.Models.Note;
import com.example.domashnee_zadanie_8_1.Services.NoteAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final ArrayList<Note> notes = new ArrayList<>();
    private NoteAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notes.add(new Note("First Note", "This is the first note"));
        notes.add(new Note("Second Note", "This is the second note"));


        adapter = new NoteAdapter(notes);

        recyclerView.setAdapter(adapter);
        registerForContextMenu(recyclerView);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addNote) {
            showAddNoteDialog();
            Toast.makeText(this,"Note is added",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.settings) {
            Toast.makeText(this,"You have clicked on settings",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getSelectedPosition();
        switch (Objects.requireNonNull(item.getTitle()).toString()) {
            case "Edit Note":
                showEditNoteDialog(position);
                Toast.makeText(this, "Note " + position + " is updated", Toast.LENGTH_SHORT).show();
                return true;
            case "Delete Note":
                notes.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(this, "Note " + position + " is deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showEditNoteDialog(int position) {
        // Inflate a custom layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.edit_item_note, null);

        EditText titleInput = dialogView.findViewById(R.id.editTitle);
        EditText descriptionInput = dialogView.findViewById(R.id.editDescription);

        // Pre-fill fields with the existing note's data
        Note currentNote = notes.get(position);
        titleInput.setText(currentNote.getTitle());
        descriptionInput.setText(currentNote.getDescription());

        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Note");
        builder.setView(dialogView); // Set the custom layout

        // Handle Save button
        builder.setPositiveButton("Save", (dialog, which) -> {
            String newTitle = titleInput.getText().toString().trim();
            String newDescription = descriptionInput.getText().toString().trim();

            if (!newTitle.isEmpty() && !newDescription.isEmpty()) {
                // Update the note and notify the adapter
                currentNote.setTitle(newTitle);
                currentNote.setDescription(newDescription);
                adapter.notifyItemChanged(position);
                Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.show();
    }

    private void showAddNoteDialog() {
        // Inflate the custom layout for the Add Note dialog
        View dialogView = getLayoutInflater().inflate(R.layout.add_item_note, null);

        EditText titleInput = dialogView.findViewById(R.id.addNoteTitle);
        EditText descriptionInput = dialogView.findViewById(R.id.addNoteDescription);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Note");
        builder.setView(dialogView);

        // Handle Save button
        builder.setPositiveButton("Save", (dialog, which) -> {
            String newTitle = titleInput.getText().toString().trim();
            String newDescription = descriptionInput.getText().toString().trim();

            if (!newTitle.isEmpty() && !newDescription.isEmpty()) {
                // Create new note and add to the list
                Note newNote = new Note(newTitle, newDescription);
                notes.add(newNote);

                // Notify the adapter to update the RecyclerView
                adapter.notifyItemInserted(notes.size() - 1);
                Toast.makeText(this, "Note Added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.show();
    }


}