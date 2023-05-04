package com.example.ajenda_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.Adapter.NoteAdapter;
import com.example.DatabaseHelper.DataNoteHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {
    // Initialize variable
    private RecyclerView recyclerView;
    private Button btnAdd;
    // **************************************
    private DataNoteHelper databaseHelper;
    private NoteAdapter adapter;
    // **************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.it_notes);
        // **************************************
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.it_home:
                        startActivity(new Intent(getApplicationContext(), HomePage.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    // **************************************
                    case R.id.it_tasks:
                        startActivity(new Intent(getApplicationContext(), ListViewTasks.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    // **************************************
                    case R.id.it_categories:
                        startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    // **************************************

                }
                return NoteActivity.super.onOptionsItemSelected(item);
            }
        });
        // **************************************
        // Assign variable
        recyclerView = findViewById(R.id.recycle);
        btnAdd = findViewById(R.id.btn_add_note);

        // Initialize database
        databaseHelper = new DataNoteHelper(getApplicationContext());

        // Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Initialize adapter
        adapter = new NoteAdapter(this,databaseHelper.getArrayData());
        // Set adapter
        recyclerView.setAdapter(adapter);

        // Set click listener on button
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize dialog
                Dialog dialog = new Dialog(NoteActivity.this);
                // Set background transparent
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_note); // Set view
                dialog.show(); // Display dialog

                // Initialize and assign variable
                EditText editTitle = dialog.findViewById(R.id.edit_title);
                EditText editText = dialog.findViewById(R.id.edit_detail);
                Button btSubmit = dialog.findViewById(R.id.bt_submit);

                // Set click listener on submit button
                btSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get title and text from editText
                        String sTitle = editTitle.getText().toString().trim();
                        String sText = editText.getText().toString().trim();
                        // Get current date
                        String sDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                                .format(new Date());
                        // Insert into database
                        databaseHelper.insertData(sTitle,sText,sDate);
                        // Refresh array
                        adapter.updateArray(databaseHelper.getArrayData());
                        dialog.dismiss(); // Close dialog
                    }
                });
            }
        });
        // Set long click listener on button
        btnAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                builder.setTitle(R.string.confirm); // Set title
                builder.setMessage(R.string.are_you_sure_to_delete);// Set Message
                builder.setIcon(getDrawable(R.drawable.history_img));
                // Set positive button
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Clear all values
                        databaseHelper.truncateData();
                        // Refresh array
                        adapter.updateArray(databaseHelper.getArrayData());
                        // Set adapter
                        recyclerView.setAdapter(adapter);
                    }
                });
                // Set negative button
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Close dialog
                    }
                });
                builder.show(); // Display dialog
                return true;
            }
        });
    }
}