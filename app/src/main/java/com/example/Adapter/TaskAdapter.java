package com.example.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.Class.Task;
import com.example.ajenda_app.R;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    Context context; // המסך שבו נמצא ה ListView
    List<Task> tasks; // רשימה של הלקוחות שבהמשך נוסיף ל ListView
//    private int count = 0;

    TextView titleText, tvDetails, tv_dateTime, tv_startTime, tv_endTime,
            tv_category, tvPriority,tv_location ,number1;
    ImageView flagIM;

    public TaskAdapter(@NonNull Context context, List<Task> tasks) {
        super(context, 0, tasks);
        this.context = context;
        this.tasks = tasks;
    }

    // int position: מיקום התא שאנחנו רצים עליו(בכל פעם שנקרא לפונקציה הפוזישין יעלה)
    // View convertView: התא עצמו
    // ViewGroup parent: ListView ה

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // נתחבר ל inflater של האקטיביטי
        // LayoutInflater: view ולהפוך אותו לכלאס מסוג XML תפקיד שלן הוא לקחת
        @SuppressLint("ViewHolder")
        View view = LayoutInflater.from(context).inflate(R.layout.task_cell, parent, false);

        titleText = (TextView) view.findViewById(R.id.titleText);
        tvDetails = (TextView) view.findViewById(R.id.tvDetails);
        tv_dateTime = (TextView) view.findViewById(R.id.tv_dateTime);
        tv_startTime = (TextView) view.findViewById(R.id.tv_startTime);
        tv_endTime = (TextView) view.findViewById(R.id.tv_endTime);
        tv_category = (TextView) view.findViewById(R.id.tv_category);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        tvPriority = (TextView) view.findViewById(R.id.tvPriority);
        flagIM = (ImageView) view.findViewById(R.id.flagIM);

        // נקבל את הלקוח שבמקום ה position ברשימה
        Task task = tasks.get(position);

        // נשנה את הנתוהים הרשומים באובייקטים שבתא להיות הנתונים של הלקוח שקיבלנו
        titleText.setText(task.get_Title());
        tvDetails.setText(task.get_Details());
        tv_dateTime.setText(task.get_S_Date());
        tv_startTime.setText(task.get_Starts());
        tv_endTime.setText(task.get_Ends());
        tv_category.setText(task.get_Category());
        tv_location.setText(task.get_Location());
        tvPriority.setText(String.valueOf(task.get_Priority()));

//        TextView number = view.findViewById(R.id.number1);
//        number.setText(position + 1 + ".");

        // במידה ועדיפות חשובה של הלקוח 1 - התמונה שלו תהיה
        if (task.get_PriorityNum() == 1) {
            flagIM.setImageResource(R.drawable.low_flag);
            tvPriority.setTextColor(Color.parseColor("#666767"));
            tvPriority.setText(task.get_Priority());
            tvPriority.setText(R.string.task_priority_low);
        }
        // במידה ועדיפות חשובה של הלקוח 2 - התמונה שלו תהיה
        else if (task.get_PriorityNum() == 2) {
            flagIM.setImageResource(R.drawable.normal_flag);
            tvPriority.setTextColor(Color.parseColor("#F44336"));
            tvPriority.setText(task.get_Priority());
            tvPriority.setText(R.string.task_priority_middle);
        }
        // במידה ועדיפות חשובה של הלקוח 3 - התמונה שלו תהיה
        else if (task.get_PriorityNum() == 3) {
            flagIM.setImageResource(R.drawable.high_flag);
            tvPriority.setTextColor(Color.parseColor("#FF9800"));
            tvPriority.setText(task.get_Priority());
            tvPriority.setText(R.string.task_priority_high);
        }
        return view;
    }
}
