package com.example.ajenda_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Adapter.TaskAdapter;
import com.example.Class.Task;
import com.example.DatabaseHelper.DataTaskHelper;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton img_btn_back;
    private CardView card_meeting, card_work, card_shopping,card_medicine,
            card_watchList, card_important, card_homeWork, card_personal,
            card_fitness, card_todoList, card_music, card_school;
    // **************************************
    private TextView tvItem_meet, tvItem_works, tvItem_shop, tvItem_medical,
            tvItem_watchList, tvItem_important,tvItem_homeWork,tvItem_personal,
            tvItem_fitness, tvItem_todoList, tvItem_music,tvItem_school,

            tv_tag_meeting, tv_tag_works, tv_tag_shopping,tv_tag_medicine,
            tv_tag_watchList, tv_tag_important, tv_tag_homeWork, tv_tag_personal,
            tv_tag_fitness, tv_tag_school, tv_tag_todoList,tv_tag_music;
    // **************************************
    private ArrayList<Task> listOfTask;
    private DataTaskHelper taskHelper;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;
    private ListView list;
    // **************************************
    private String name_tag;
    private SharedPreferences sharedPreferences;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initViews();

        taskHelper.open();
//        listOfTask = taskHelper.getAllTasks();
//        taskAdapter = new TaskAdapter(this, listOfTask);
        // *****************************************
        img_btn_back = (ImageButton) findViewById(R.id.backB);
        img_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, HomePage.class);
                startActivity(intent);
            }
        });
        // *****************************************
        taskHelper.open();
        // ********** Meeting **********
        String txt_meeting = tv_tag_meeting.getText().toString();
        taskList = taskHelper.searchByCategory(txt_meeting);
        tvItem_meet.setText("" + taskList.size());

        // ********** Shopping **********
        String txt_shopping = tv_tag_shopping.getText().toString();
        taskList = taskHelper.searchByCategory(txt_shopping);
        tvItem_shop.setText("" + taskList.size());

        // ********** Works **********
        String txt_works = tv_tag_works.getText().toString();
        taskList = taskHelper.searchByCategory(txt_works);
        tvItem_works.setText("" + taskList.size());

        // ********** Medical **********
        String txt_medical = tv_tag_medicine.getText().toString();
        taskList = taskHelper.searchByCategory(txt_medical);
        tvItem_medical.setText("" + taskList.size());

        // ********** Watch List **********
        String txt_watch_list = tv_tag_watchList.getText().toString();
        taskList = taskHelper.searchByCategory(txt_watch_list);
        tvItem_watchList.setText("" + taskList.size());

        // ********** Important **********
        String txt_important = tv_tag_important.getText().toString();
        taskList = taskHelper.searchByCategory(txt_important);
        tvItem_important.setText("" + taskList.size());

        // ********** Home Work **********
        String txt_home_work = tv_tag_homeWork.getText().toString();
        taskList = taskHelper.searchByCategory(txt_home_work);
        tvItem_homeWork.setText("" + taskList.size());

        // ********** Personal **********
        String txt_personal = tv_tag_personal.getText().toString();
        taskList = taskHelper.searchByCategory(txt_personal);
        tvItem_personal.setText("" + taskList.size());

        // ********** Fitness **********
        String txt_fitness = tv_tag_fitness.getText().toString();
        taskList = taskHelper.searchByCategory(txt_fitness);
        tvItem_fitness.setText("" + taskList.size());

        // ********** Todo List **********
        String txt_todo_list = tv_tag_todoList.getText().toString();
        taskList = taskHelper.searchByCategory(txt_todo_list);
        tvItem_todoList.setText("" + taskList.size());

        // ********** Music **********
        String txt_music = tv_tag_music.getText().toString();
        taskList = taskHelper.searchByCategory(txt_music);
        tvItem_music.setText("" + taskList.size());

        // ********** School **********
        String txt_school = tv_tag_school.getText().toString();
        taskList = taskHelper.searchByCategory(txt_school);
        tvItem_school.setText("" + taskList.size());
        taskHelper.close();
    }

    public void initViews() {
        card_meeting = (CardView) findViewById(R.id.card_meeting);
        card_work = (CardView) findViewById(R.id.card_work);
        card_shopping = (CardView) findViewById(R.id.card_shopping);
        card_medicine = (CardView) findViewById(R.id.card_medicine);
        card_watchList = (CardView) findViewById(R.id.card_watchList);
        card_important = (CardView) findViewById(R.id.card_important);
        card_homeWork = (CardView) findViewById(R.id.card_homeWork);
        card_personal = (CardView) findViewById(R.id.card_personal);
        card_fitness = (CardView) findViewById(R.id.card_fitness);
        card_todoList = (CardView) findViewById(R.id.card_todoList);
        card_music = (CardView) findViewById(R.id.card_music);
        card_school = (CardView) findViewById(R.id.card_school);
        // **************************************
        tvItem_meet = (TextView) findViewById(R.id.tvItem_meet);
        tvItem_works = (TextView) findViewById(R.id.tvItem_works);
        tvItem_shop = (TextView) findViewById(R.id.tvItem_shop);
        tvItem_medical = (TextView) findViewById(R.id.tvItem_medicine);
        tvItem_watchList = (TextView) findViewById(R.id.tvItem_watchList);
        tvItem_important = (TextView) findViewById(R.id.tvItem_important);
        tvItem_homeWork = (TextView) findViewById(R.id.tvItem_homeWork);
        tvItem_personal = (TextView) findViewById(R.id.tvItem_personal);
        tvItem_fitness = (TextView) findViewById(R.id.tvItem_fitness);
        tvItem_todoList = (TextView) findViewById(R.id.tvItem_todoList);
        tvItem_music = (TextView) findViewById(R.id.tvItem_music);
        tvItem_school = (TextView) findViewById(R.id.tvItem_school);
        // **************************************
        tv_tag_meeting = (TextView) findViewById(R.id.tv_tag_meeting);
        tv_tag_works = (TextView) findViewById(R.id.tv_tag_works);
        tv_tag_shopping = (TextView) findViewById(R.id.tv_tag_shopping);
        tv_tag_medicine = (TextView) findViewById(R.id.tv_tag_medicine);
        tv_tag_watchList = (TextView) findViewById(R.id.tv_tag_watchList);
        tv_tag_important = (TextView) findViewById(R.id.tv_tag_important);
        tv_tag_homeWork = (TextView) findViewById(R.id.tv_tag_homeWork);
        tv_tag_personal = (TextView) findViewById(R.id.tv_tag_personal);
        tv_tag_fitness = (TextView) findViewById(R.id.tv_tag_fitness);
        tv_tag_todoList = (TextView) findViewById(R.id.tv_tag_todoList);
        tv_tag_music = (TextView) findViewById(R.id.tv_tag_music);
        tv_tag_school = (TextView) findViewById(R.id.tv_tag_school);
        // **************************************
        card_meeting.setOnClickListener(this);
        card_work.setOnClickListener(this);
        card_shopping.setOnClickListener(this);
        card_medicine.setOnClickListener(this);
        card_watchList.setOnClickListener(this);
        card_important.setOnClickListener(this);
        card_homeWork.setOnClickListener(this);
        card_personal.setOnClickListener(this);
        card_fitness.setOnClickListener(this);
        card_todoList.setOnClickListener(this);
        card_music.setOnClickListener(this);
        card_school.setOnClickListener(this);
        // **************************************
        listOfTask = new ArrayList<Task>();
        taskHelper = new DataTaskHelper(this);
        taskAdapter = new TaskAdapter(this, listOfTask);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        sharedPreferences = getSharedPreferences("NameTag", Context.MODE_PRIVATE);
        if (card_meeting == v) {
            taskHelper.open();
            name_tag = tv_tag_meeting.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name:", name_tag);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), ItemCategoryActivity.class);
            intent.putExtra("rowName", name_tag);
            startActivityForResult(intent,0);
            taskHelper.close();
        }
        if (card_work == v) {
            taskHelper.open();
            name_tag = tv_tag_works.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name:", name_tag);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), ItemCategoryActivity.class);
            intent.putExtra("rowName", name_tag);
            startActivityForResult(intent,0);
            taskHelper.close();
        }
        if (card_shopping == v) {
            taskHelper.open();
            name_tag = tv_tag_shopping.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name:", name_tag);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), ItemCategoryActivity.class);
            intent.putExtra("rowName", name_tag);
            startActivityForResult(intent,0);
            taskHelper.close();
        }
        if (card_medicine == v) {
            taskHelper.open();
            name_tag = tv_tag_medicine.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name:", name_tag);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), ItemCategoryActivity.class);
            intent.putExtra("rowName", name_tag);
            startActivityForResult(intent,0);
            taskHelper.close();
        }
        if (card_watchList == v) {
            taskHelper.open();
            name_tag = tv_tag_watchList.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name:", name_tag);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), ItemCategoryActivity.class);
            intent.putExtra("rowName", name_tag);
            startActivityForResult(intent,0);
            taskHelper.close();
        }
        if (card_important == v) {
            taskHelper.open();
            name_tag = tv_tag_important.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name:", name_tag);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), ItemCategoryActivity.class);
            intent.putExtra("rowName", name_tag);
            startActivityForResult(intent,0);
            taskHelper.close();
        }
        if (card_homeWork == v) {
            taskHelper.open();
            name_tag = tv_tag_homeWork.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name:", name_tag);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), ItemCategoryActivity.class);
            intent.putExtra("rowName", name_tag);
            startActivityForResult(intent,0);
            taskHelper.close();
        }
        if (card_personal == v) {
            taskHelper.open();
            name_tag = tv_tag_personal.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name:", name_tag);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), ItemCategoryActivity.class);
            intent.putExtra("rowName", name_tag);
            startActivityForResult(intent,0);
            taskHelper.close();
        }
        if (card_fitness == v) {
            taskHelper.open();
            name_tag = tv_tag_fitness.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name:", name_tag);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), ItemCategoryActivity.class);
            intent.putExtra("rowName", name_tag);
            startActivityForResult(intent,0);
            taskHelper.close();
        }
        if (card_todoList == v) {
            taskHelper.open();
            name_tag = tv_tag_todoList.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name:", name_tag);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), ItemCategoryActivity.class);
            intent.putExtra("rowName", name_tag);
            startActivityForResult(intent,0);
            taskHelper.close();
        }
        if (card_music == v) {
            taskHelper.open();
            name_tag = tv_tag_music.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name:", name_tag);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), ItemCategoryActivity.class);
            intent.putExtra("rowName", name_tag);
            startActivityForResult(intent,0);
            taskHelper.close();
        }
        if (card_school == v) {
            taskHelper.open();
            name_tag = tv_tag_school.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name:", name_tag);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), ItemCategoryActivity.class);
            intent.putExtra("rowName", name_tag);
            startActivityForResult(intent,0);
            taskHelper.close();
        }
    }
}