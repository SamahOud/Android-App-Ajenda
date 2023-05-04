package com.example.ajenda_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.Adapter.TaskAdapter;
import com.example.Class.Task;
import com.example.DatabaseHelper.DataTaskHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class DonePageActivity extends AppCompatActivity {
    private ArrayList<Task> listOfTask;
    private ArrayList<Task> arrayList = new ArrayList<>();;
    private TaskAdapter taskAdapter;
    private DataTaskHelper taskHelper;
    private List<Task> taskList;
    // **************************************
    private SwipeMenuListView listViewItem;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_page);

        initViews();
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNav);
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
                    case R.id.it_notes:
                        startActivity(new Intent(getApplicationContext(), NoteActivity.class));
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
                return DonePageActivity.super.onOptionsItemSelected(item);
            }
        });
        // **************************************

        taskHelper.open();
        listOfTask = taskHelper.getAllDoneTasks();
        taskAdapter = new TaskAdapter(this, listOfTask);
        Log.d("Done:", "listOfTask: " + listOfTask.size());
        listViewItem.setAdapter(taskAdapter);

        listViewItem.setChoiceMode(SwipeMenuListView.CHOICE_MODE_MULTIPLE_MODAL);
        listViewItem.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                count = count + 1;
                mode.setTitle(count + " " + R.string.items_selected);
                arrayList.add(listOfTask.get(position));
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.list_menu,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_id:
                        for(Task msg : arrayList) {
                            int id = msg.get_TaskId();
                            taskHelper.open();
                            taskHelper.deleteDoneByRow(id);
                            taskHelper.close();
                            taskAdapter.remove(msg);
                        }
                        Toast.makeText(getApplicationContext(),R.string.deleted,Toast.LENGTH_SHORT).show();
                        count = 0;
                        mode.finish();
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(),R.string.nothing_selected,Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
        listViewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelected(true);
            }
        });
    }

    private void initViews() {
        listViewItem = findViewById(R.id.listViewItem);
        // **************************************
        listOfTask = new ArrayList<Task>();
        taskHelper = new DataTaskHelper(this);
        taskAdapter = new TaskAdapter(this, listOfTask);
        taskList = new ArrayList<>();
    }
}