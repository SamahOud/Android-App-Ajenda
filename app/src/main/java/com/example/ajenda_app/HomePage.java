package com.example.ajenda_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
    private TextView YourName;
    private CardView clickTask, clickNote, clickCreateTask,clickCategoryList,
            clickDone, clickHistory;
    // **************************************
    private boolean flag = true;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initViews();

        sharedPreferences = getApplicationContext().
                getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        flag = sharedPreferences.getBoolean("flag:",true);

        if (flag) {
            YourName.setText("");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("flag:",flag);
            String name = sharedPreferences.getString("user:", "");
            YourName.setText(name);
        }
    }

    private void initViews() {
        YourName = (TextView) findViewById(R.id.YourName);
        // **************************************
        clickNote = (CardView) findViewById(R.id.clickNoteList);
        clickTask = (CardView) findViewById(R.id.clickTaskList);
        clickCreateTask = (CardView) findViewById(R.id.clickCreateTask);
        clickDone = (CardView) findViewById(R.id.clickDone);
        clickHistory = (CardView) findViewById(R.id.clickHistory);
        clickCategoryList = (CardView) findViewById(R.id.clickCategoryList);
        // **************************************
        clickNote.setOnClickListener(this);
        clickTask.setOnClickListener(this);
        clickCreateTask.setOnClickListener(this);
        clickDone.setOnClickListener(this);
        clickHistory.setOnClickListener(this);
        clickCategoryList.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clickNoteList:
                startActivity(new Intent(HomePage.this,
                        NoteActivity.class));
                break;

            case R.id.clickTaskList:
                startActivity(new Intent(HomePage.this,
                        ListViewTasks.class));
                break;

            case R.id.clickCreateTask:
                startActivity(new Intent(HomePage.this,
                        CreateTask.class));
                break;

            case R.id.clickDone:
                startActivity(new Intent(HomePage.this,
                        DonePageActivity.class));
                break;

            case R.id.clickHistory:
                startActivity(new Intent(HomePage.this,
                        TrashPageActivity.class));
                break;

            case R.id.clickCategoryList:
                startActivity(new Intent(HomePage.this,
                        CategoryActivity.class));
                break;
        }
    }
}