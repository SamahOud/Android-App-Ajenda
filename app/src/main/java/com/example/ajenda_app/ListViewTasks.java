package com.example.ajenda_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.Adapter.TaskAdapter;
import com.example.Class.Task;
import com.example.DatabaseHelper.DataTaskHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ListViewTasks extends AppCompatActivity {
    private SwipeMenuListView listViewItem;
    private ImageView img_add_task,filterAll, searchCategory;
    private TextView filterLow, filterMedium, filterHigh;
    // **************************************
    private ArrayList<Task> listOfTask;
    private TaskAdapter taskAdapter;
    private Task task;
    private DataTaskHelper taskHelper;
    private List<Task> taskList;
    // **************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_tasks);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.it_tasks);
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
                return ListViewTasks.super.onOptionsItemSelected(item);
            }
        });
        // **************************************
        img_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListViewTasks.this, CreateTask.class);
                startActivityForResult(intent, 1);
            }
        });
        filterAll.setBackgroundResource(R.drawable.filter_selected_shape);
        taskHelper.open();
        listOfTask = taskHelper.searchForThisDate();
        Log.i("This Date:-", "List is " + listOfTask.size());
        taskAdapter = new TaskAdapter(this, listOfTask);
        listViewItem.setAdapter(taskAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // ***************** create "done" item *****************
                SwipeMenuItem doneItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                doneItem.setBackground(new ColorDrawable(Color.parseColor("#068B0C")));
                doneItem.setWidth(200); // set item width
                doneItem.setIcon(R.drawable.done); // set an icon
                doneItem.setTitle(R.string.done); // set item title
                doneItem.setTitleSize(18); // set item title font size
                doneItem.setTitleColor(Color.WHITE); // set item title font color
                menu.addMenuItem(doneItem); // add to menu

                // ***************** create "edit" item *****************
                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.parseColor("#03303F")));
                editItem.setWidth(200); // set item width
                editItem.setIcon(R.drawable.edit); // set an icon
                editItem.setTitle(R.string.edit); // set item title
                editItem.setTitleSize(18); // set item title font size
                editItem.setTitleColor(Color.WHITE); // set item title font color
                menu.addMenuItem(editItem); // add to menu

                // ***************** create "delete" item *****************
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#BA3A11")));
                deleteItem.setWidth(200); // set item width
                deleteItem.setIcon(R.drawable.delete);// set an icon
                deleteItem.setTitle(R.string.delete); // set item title
                deleteItem.setTitleSize(18); // set item title font size
                deleteItem.setTitleColor(Color.WHITE); // set item title font color
                menu.addMenuItem(deleteItem); // add to menu
            }
        };
        listViewItem.setMenuCreator(creator);
        listViewItem.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0: // for item "done"
                        Log.d("TAG-0:", "onMenuItemClick: clicked item done : " + index);
                        Task taskDone = taskAdapter.getItem(position);
                        int id_1 = taskDone.get_TaskId();

                        AlertDialog.Builder dialogDone = new AlertDialog.Builder(ListViewTasks.this);
                        dialogDone.setTitle(R.string.completed_the_task);
                        dialogDone.setIcon(R.drawable.completed);
                        dialogDone.setMessage(R.string.are_you_sure_you_want_to_mark_this_task_as_completed);
                        dialogDone.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                taskHelper.open();
                                taskHelper.insertDone(taskDone);
                                taskHelper.deleteTaskByRow(id_1);
                                taskHelper.close();
                                taskAdapter.remove(taskDone);
                            }
                        });
                        dialogDone.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialogDone.show();
                        break;
                    // **************************************
                    case 1: // for item "edit"
                        Log.d("TAG-1:", "onMenuItemClick: clicked item edit : " + index);
                        Task taskE = taskAdapter.getItem(position);
                        refreshMyAdapter();
                        Intent intent = new Intent(ListViewTasks.this, UpdateTask.class);
                        intent.putExtra("rowId", taskE.get_TaskId());
                        startActivityForResult(intent,0);
                        break;
                    // **************************************
                    case 2: // for item "delete"
                        Log.d("TAG-2:", "onMenuItemClick: clicked item delete : " + index);
                        Task taskD = taskAdapter.getItem(position);
                        int id_2 = taskD.get_TaskId();

                        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(ListViewTasks.this);
                        dialogDelete.setTitle(R.string.delete);
                        dialogDelete.setIcon(R.drawable.history_img);
                        dialogDelete.setMessage(R.string.are_you_sure_to_delete);
                        dialogDelete.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                taskHelper.open();
                                taskHelper.insertTrash(taskD);
                                taskHelper.deleteTaskByRow(id_2);
                                taskHelper.close();
                                taskAdapter.remove(taskD);
                            }
                        });
                        dialogDelete.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialogDelete.show();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + index);
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        listViewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = taskAdapter.getItem(position);
                Intent intent = new Intent(ListViewTasks.this, PageDetailsActivity.class);
                intent.putExtra("rowId", task.get_TaskId());
                startActivityForResult(intent,0);
            }
        });
        // **************************************
        filterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskHelper.open();
                filterAll.setBackgroundResource(R.drawable.filter_selected_shape);
                filterLow.setBackgroundResource(R.drawable.filter_shape);
                filterMedium.setBackgroundResource(R.drawable.filter_shape);
                filterHigh.setBackgroundResource(R.drawable.filter_shape);
                listOfTask = taskHelper.getAllTasks();
                Log.i("AllPriority:-", "List count is " + listOfTask.size());
                taskHelper.close();
                refreshMyAdapter();
            }
        });
        filterLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskHelper.open();
                listOfTask = taskHelper.getAllTasksByFilter("priorityNum=1", "priorityNum");
                filterAll.setBackgroundResource(R.drawable.filter_shape);
                filterLow.setBackgroundResource(R.drawable.filter_selected_shape);
                filterMedium.setBackgroundResource(R.drawable.filter_shape);
                filterHigh.setBackgroundResource(R.drawable.filter_shape);
                Log.i("LowPriority:-", "List count is " + listOfTask.size());
                taskHelper.close();
                refreshMyAdapter();
            }
        });
        filterMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskHelper.open();
                listOfTask = taskHelper.getAllTasksByFilter("priorityNum=2", "priorityNum");
                filterAll.setBackgroundResource(R.drawable.filter_shape);
                filterLow.setBackgroundResource(R.drawable.filter_shape);
                filterMedium.setBackgroundResource(R.drawable.filter_selected_shape);
                filterHigh.setBackgroundResource(R.drawable.filter_shape);
                Log.i("MediumPriority:-", "List count is " + listOfTask.size());
                taskHelper.close();
                refreshMyAdapter();
            }
        });
        filterHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskHelper.open();
                listOfTask = taskHelper.getAllTasksByFilter("priorityNum=3", "priorityNum");
                filterAll.setBackgroundResource(R.drawable.filter_shape);
                filterLow.setBackgroundResource(R.drawable.filter_shape);
                filterMedium.setBackgroundResource(R.drawable.filter_shape);
                filterHigh.setBackgroundResource(R.drawable.filter_selected_shape);
                Log.i("HighPriority:-", "List count is " + listOfTask.size());
                taskHelper.close();
                refreshMyAdapter();
            }
        });

        searchCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize dialog
                Dialog dialog = new Dialog(ListViewTasks.this);
                // Set background transparent
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_category); // Set view
                dialog.show(); // Display dialog

                // Initialize and assign variable
                TextView tv_Meeting =  dialog.findViewById(R.id.tv_Meeting);
                TextView tv_Shopping = dialog.findViewById(R.id.tv_Shopping);
                TextView tv_Works = dialog.findViewById(R.id.tv_Works);
                TextView tv_Medical = dialog.findViewById(R.id.tv_Medical);
                TextView tv_WatchList = dialog.findViewById(R.id.tv_WatchList);
                TextView tv_Important = dialog.findViewById(R.id.tv_Important);
                TextView tv_HomeWork = dialog.findViewById(R.id.tv_HomeWork);
                TextView tv_Personal = dialog.findViewById(R.id.tv_Personal);
                TextView tv_Fitness = dialog.findViewById(R.id.tv_Fitness);
                TextView tv_TodoList = dialog.findViewById(R.id.tv_TodoList);
                TextView tv_Music = dialog.findViewById(R.id.tv_Music);
                TextView tv_School = dialog.findViewById(R.id.tv_School);

                // **************************************
                tv_Meeting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskHelper.open();
                        String txt_meeting = tv_Meeting.getText().toString().trim();
                        taskList = taskHelper.searchByCategory(txt_meeting);
                        taskAdapter = new TaskAdapter(ListViewTasks.this, taskList);
                        listViewItem.setAdapter(taskAdapter);
                        taskHelper.close();
                        dialog.dismiss(); // Close dialog
                    }
                });
                tv_Shopping.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskHelper.open();
                        String txt_shopping = tv_Shopping.getText().toString().trim();
                        taskList = taskHelper.searchByCategory(txt_shopping);
                        taskAdapter = new TaskAdapter(ListViewTasks.this, taskList);
                        listViewItem.setAdapter(taskAdapter);
                        taskHelper.close();
                        dialog.dismiss(); // Close dialog
                    }
                });
                tv_Works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskHelper.open();
                        String txt_works = tv_Works.getText().toString().trim();
                        taskList = taskHelper.searchByCategory(txt_works);
                        taskAdapter = new TaskAdapter(ListViewTasks.this, taskList);
                        listViewItem.setAdapter(taskAdapter);
                        taskHelper.close();
                        dialog.dismiss(); // Close dialog
                    }
                });
                tv_Medical.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskHelper.open();
                        String txt_medical = tv_Medical.getText().toString().trim();
                        taskList = taskHelper.searchByCategory(txt_medical);
                        taskAdapter = new TaskAdapter(ListViewTasks.this, taskList);
                        listViewItem.setAdapter(taskAdapter);
                        taskHelper.close();
                        dialog.dismiss(); // Close dialog
                    }
                });
                tv_WatchList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskHelper.open();
                        String txt_watch_list = tv_WatchList.getText().toString().trim();
                        taskList = taskHelper.searchByCategory(txt_watch_list);
                        taskAdapter = new TaskAdapter(ListViewTasks.this, taskList);
                        listViewItem.setAdapter(taskAdapter);
                        taskHelper.close();
                        dialog.dismiss(); // Close dialog
                    }
                });
                tv_Important.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskHelper.open();
                        String txt_important = tv_Important.getText().toString().trim();
                        taskList = taskHelper.searchByCategory(txt_important);
                        taskAdapter = new TaskAdapter(ListViewTasks.this, taskList);
                        listViewItem.setAdapter(taskAdapter);
                        taskHelper.close();
                        dialog.dismiss(); // Close dialog
                    }
                });
                tv_HomeWork.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskHelper.open();
                        String txt_home_work = tv_HomeWork.getText().toString().trim();
                        taskList = taskHelper.searchByCategory(txt_home_work);
                        taskAdapter = new TaskAdapter(ListViewTasks.this, taskList);
                        listViewItem.setAdapter(taskAdapter);
                        taskHelper.close();
                        dialog.dismiss(); // Close dialog
                    }
                });
                tv_Personal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskHelper.open();
                        String txt_personal = tv_Personal.getText().toString().trim();
                        taskList = taskHelper.searchByCategory(txt_personal);
                        taskAdapter = new TaskAdapter(ListViewTasks.this, taskList);
                        listViewItem.setAdapter(taskAdapter);
                        taskHelper.close();
                        dialog.dismiss(); // Close dialog
                    }
                });
                tv_Fitness.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskHelper.open();
                        String txt_fitness = tv_Fitness.getText().toString().trim();
                        taskList = taskHelper.searchByCategory(txt_fitness);
                        taskAdapter = new TaskAdapter(ListViewTasks.this, taskList);
                        listViewItem.setAdapter(taskAdapter);
                        taskHelper.close();
                        dialog.dismiss(); // Close dialog
                    }
                });
                tv_TodoList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskHelper.open();
                        String txt_todo_list = tv_TodoList.getText().toString().trim();
                        taskList = taskHelper.searchByCategory(txt_todo_list);
                        taskAdapter = new TaskAdapter(ListViewTasks.this, taskList);
                        listViewItem.setAdapter(taskAdapter);
                        taskHelper.close();
                        dialog.dismiss(); // Close dialog
                    }
                });
                tv_Music.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskHelper.open();
                        String txt_music = tv_Music.getText().toString().trim();
                        taskList = taskHelper.searchByCategory(txt_music);
                        taskAdapter = new TaskAdapter(ListViewTasks.this, taskList);
                        listViewItem.setAdapter(taskAdapter);
                        taskHelper.close();
                        dialog.dismiss(); // Close dialog
                    }
                });
                tv_School.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskHelper.open();
                        String txt_school = tv_School.getText().toString().trim();
                        taskList = taskHelper.searchByCategory(txt_school);
                        taskAdapter = new TaskAdapter(ListViewTasks.this, taskList);
                        listViewItem.setAdapter(taskAdapter);
                        taskHelper.close();
                        dialog.dismiss(); // Close dialog
                    }
                });
            }
        });
    }

    // *************** Refresh My Adapter ***************
    public void refreshMyAdapter() {
        taskAdapter = new TaskAdapter(this, listOfTask);
        listViewItem.setAdapter(taskAdapter);
    }

    private void initViews() {
        listViewItem = findViewById(R.id.listViewItem);
        // **************************************
        img_add_task = (ImageView) findViewById(R.id.img_add_task);
        filterAll = (ImageView) findViewById(R.id.filterAll);
        searchCategory = (ImageView) findViewById(R.id.searchCategory);
        // **************************************
        filterLow = (TextView) findViewById(R.id.filterLow);
        filterMedium = (TextView) findViewById(R.id.filterMedium);
        filterHigh = (TextView) findViewById(R.id.filterHigh);
        // **************************************
        listOfTask = new ArrayList<Task>();
        taskHelper = new DataTaskHelper(this);
        taskList = new ArrayList<>();
        // **************************************
        refreshMyAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            taskHelper.open();
            listOfTask = taskHelper.getAllTasks();
            refreshMyAdapter();

            final View layout = getLayoutInflater().inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_layout_id));
            CardView lyt_card = (CardView) layout.findViewById(R.id.lyt_card);
            TextView text = (TextView) layout.findViewById(R.id.text);
            final Toast toast = new Toast(getApplicationContext());

            if (requestCode == 0) {
                text.setText(R.string.your_details_are_update_and_saved);
                lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_blue_grey_500));
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }
            else if (requestCode == 1) {
                text.setText(R.string.your_details_are_saved);
                lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_teal_500));
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_date, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.it_this_date) {
            taskHelper.open();
            listOfTask = taskHelper.searchForThisDate();
            taskHelper.close();
            refreshMyAdapter();
            Log.i("This Date:-", "List is " + listOfTask.size());
        }
        return super.onOptionsItemSelected(item);
    }
}