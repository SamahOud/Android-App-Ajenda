package com.example.ajenda_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Adapter.TaskAdapter;
import com.example.Class.Task;
import com.example.DatabaseHelper.DataTaskHelper;

import java.util.ArrayList;

public class PageDetailsActivity extends AppCompatActivity {
    private ImageButton img_backB, img_editB, img_deleteB;
    private ImageView img_see_flag;
    private TextView tv_see_title, tv_see_date,
            tv_see_startTime,tv_see_endTime, tv_st_txt,
            tv_see_priority, tv_see_location, tv_see_desc;
    // **************************************
    private DataTaskHelper taskHelper;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> listOfTask;
    // **************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_details);

        initViews();

        taskHelper.open();
        listOfTask = taskHelper.getAllTasks();
        taskAdapter = new TaskAdapter(this, listOfTask);

        img_backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageDetailsActivity.this, ListViewTasks.class);
                startActivity(intent);
            }
        });
        img_editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = getIntent().getIntExtra("rowId", 0);
                taskHelper.open();
                Task task = taskHelper.getTaskById(id);
                taskHelper.close();

                Intent intent = new Intent(PageDetailsActivity.this, UpdateTask.class);
                intent.putExtra("rowId", task.get_TaskId());
                startActivityForResult(intent,0);
            }
        });
        img_deleteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogDelete = new AlertDialog.Builder(PageDetailsActivity.this);
                dialogDelete.setTitle(R.string.delete);
                dialogDelete.setIcon(R.drawable.history_img);
                dialogDelete.setMessage(R.string.are_you_sure_you_want_to_delete_it);
                dialogDelete.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int id = getIntent().getIntExtra("rowId", 0);
                        taskHelper.open();
                        taskHelper.deleteTaskByRow(id);
                        taskHelper.close();
                        Intent intent = new Intent(PageDetailsActivity.this, ListViewTasks.class);
                        startActivity(intent);
                    }
                });
                dialogDelete.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogDelete.show();
                Intent intent = new Intent(PageDetailsActivity.this, ListViewTasks.class);
                setResult(RESULT_OK, intent);
            }
        });
    }

    public void initViews() {
        img_backB = (ImageButton) findViewById(R.id.img_backB);
        img_editB = (ImageButton) findViewById(R.id.img_editB);
        img_deleteB = (ImageButton) findViewById(R.id.img_deleteB);
        // **************************************
        img_see_flag = (ImageView) findViewById(R.id.img_see_flag);
        // **************************************
        tv_see_title = (TextView) findViewById(R.id.tv_see_title);
        tv_see_date = (TextView) findViewById(R.id.tv_see_date);
        tv_see_startTime = (TextView) findViewById(R.id.tv_see_startTime);
        tv_see_endTime = (TextView) findViewById(R.id.tv_see_endTime);
        tv_st_txt = (TextView) findViewById(R.id.tv_st_txt);
        tv_see_priority = (TextView) findViewById(R.id.tv_see_priority);
        tv_see_location = (TextView) findViewById(R.id.tv_see_location);
        tv_see_desc = (TextView) findViewById(R.id.tv_see_desc);
        // **************************************
        listOfTask = new ArrayList<Task>();
        taskHelper = new DataTaskHelper(this);
        taskAdapter = new TaskAdapter(this, listOfTask);
        // **************************************
        taskHelper = new DataTaskHelper(this);
        int id = getIntent().getIntExtra("rowId", 0);
        if (id != 0) {
            taskHelper.open();

            Task task = taskHelper.getTaskById(id);
            tv_see_title.setText(task.get_Title());
            tv_see_date.setText(task.get_S_Date());
            tv_see_startTime.setText(task.get_Starts());
            tv_see_endTime.setText(task.get_Ends());
            tv_st_txt.setText(task.get_Category());
            tv_see_location.setText(task.get_Location());
            tv_see_desc.setText(task.get_Details());

            if (task.get_PriorityNum() == 1) {
                img_see_flag.setImageResource(R.drawable.low_flag);
                tv_see_priority.setText(R.string.task_priority_low);
            }
            else {
                if (task.get_PriorityNum() == 2) {
                    img_see_flag.setImageResource(R.drawable.normal_flag);
                    tv_see_priority.setText(R.string.task_priority_middle);
                }
                else if (task.get_PriorityNum() == 3) {
                    img_see_flag.setImageResource(R.drawable.high_flag);
                    tv_see_priority.setText(R.string.task_priority_high);
                }
            }
            taskHelper.close();
        }
    }
}