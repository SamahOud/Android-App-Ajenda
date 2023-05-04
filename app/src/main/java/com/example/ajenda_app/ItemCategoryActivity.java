package com.example.ajenda_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Adapter.TaskAdapter;
import com.example.Class.Task;
import com.example.DatabaseHelper.DataTaskHelper;

import java.util.ArrayList;

public class ItemCategoryActivity extends AppCompatActivity {
    private boolean flag = true;
    private SharedPreferences sharedPreferences;
    // **************************************
    private ImageButton img_btn_back;
    private TextView tv_name_tag;
    private ListView listView;
    // **************************************
    private ArrayList<Task> listOfTask;
    private TaskAdapter taskAdapter;
    private DataTaskHelper taskHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_category);

        tv_name_tag = (TextView) findViewById(R.id.tv_name_tag);
        // **************************************
        sharedPreferences = getApplicationContext().
                getSharedPreferences("NameTag", Context.MODE_PRIVATE);
        flag = sharedPreferences.getBoolean("flag:",true);
        if (flag) {
            tv_name_tag.setText("");
            // בשביל להזין נתונים ל  SharedPreferences צריך ליצור אידיתור ולערוך בעזרתו
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("flag:",flag);
            String name_tag = sharedPreferences.getString("name:", "");
            tv_name_tag.setText(name_tag);
        }
        // **************************************
        img_btn_back = (ImageButton) findViewById(R.id.back);
        img_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemCategoryActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });
        // **************************************
        String name_tag = getIntent().getStringExtra("rowName");

        listOfTask = new ArrayList<Task>();
        taskHelper = new DataTaskHelper(this);

        listView = (ListView) findViewById(R.id.listView);
        taskHelper.open();

        listOfTask = taskHelper.searchByCategory(name_tag);
        taskAdapter = new TaskAdapter(this, listOfTask);
        listView.setAdapter(taskAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = taskAdapter.getItem(position);
                Intent intent = new Intent(ItemCategoryActivity.this,
                        PageDetailsActivity.class);
                intent.putExtra("rowId", task.get_TaskId());
                startActivityForResult(intent,0);
            }
        });
        taskHelper.close();
    }
}