package com.example.ajenda_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.Class.AlarmReceiver;
import com.example.Class.IBaseGpsListener;
import com.example.Class.Task;
import com.example.DatabaseHelper.DataTaskHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UpdateTask extends AppCompatActivity implements View.OnClickListener, IBaseGpsListener {
    private EditText et_up_title, et_up_date, et_up_startTime, et_up_endTime,
            et_up_priority, et_up_notification, et_up_desc;
    private ImageButton img_btn_back, img_btn_update;
    // *****************************************
    private DataTaskHelper taskHelper;
    // *****************************************
    private AlarmReceiver alarmReceiver;
    private int mYear, mMonth, mDay, mHourS, mMinuteS;
    // *****************************************
    private TextView tv_up_get_location, tv_up_str_txt;
    private Button btn_up_get_location;
    private static final int PERMISSION_LOCATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        initView();
        alarmReceiver = new AlarmReceiver();
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHourS = calendar.get(Calendar.HOUR_OF_DAY);
        mMinuteS = calendar.get(Calendar.MINUTE);
        // *****************************************
        img_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateTask.this, ListViewTasks.class);
                startActivity(intent);
            }
        });
        // *****************************************
        et_up_date.setInputType(InputType.TYPE_NULL);
        et_up_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(et_up_date);
            }
        });
        et_up_startTime.setInputType(InputType.TYPE_NULL);
        et_up_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(et_up_startTime);
            }
        });
        et_up_endTime.setInputType(InputType.TYPE_NULL);
        et_up_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(et_up_endTime);
            }
        });
        // *****************************************
        et_up_notification.setInputType(InputType.TYPE_NULL);
        et_up_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] items = {"Add", "Undo"};
                // Create a dialog to choose an action
                AlertDialog.Builder dialog = new AlertDialog.Builder(UpdateTask.this);
                dialog.setTitle(R.string.choose_an_action);
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) { // Add
                            if (et_up_date.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(UpdateTask.this, R.string.date_is_empty, Toast.LENGTH_SHORT).show();
                            }
                            else if (et_up_startTime.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(UpdateTask.this, R.string.time_is_empty, Toast.LENGTH_SHORT).show();
                            }
                            else if (TextUtils.isEmpty(et_up_title.getText().toString())) {
                                et_up_title.setError("Message can't be empty!");
                            }
                            else {
                                alarmReceiver.setOneTimeAlarm(UpdateTask.this, AlarmReceiver.TYPE_ONE_TIME,
                                        et_up_date.getText().toString(), et_up_startTime.getText().toString(),
                                        et_up_title.getText().toString());
                                et_up_notification.setText(R.string.add);
                            }
                        }
                        else { // Undo
                            et_up_notification.setText(R.string.undo);
                        }
                    }
                });
                dialog.show();
            }
        });
        btn_up_get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for location permission
                if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M &&
                        checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_LOCATION);
                }
                else {
                    showLocation();
                }
            }
        });
    }
    // =================================================
    private void showDateTimeDialog(EditText date_time_in) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
                date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new DatePickerDialog(UpdateTask.this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    // =================================================
    private void showTimeDialog(final EditText time_in) {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                time_in.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new TimePickerDialog(UpdateTask.this, timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),false).show();
    }
    // =================================================
    @SuppressLint("MissingPermission")
    private void showLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Check if GPS enabled
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Start location
            tv_up_get_location.setText("Loading Location...");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0,0,this);
        }
        else {
            // Enable GPS
            Toast.makeText(this, "Enable GPS!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }
    // =================================================
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        showLocation();
    }
    // =================================================
    public void initView() {
        et_up_title = (EditText) findViewById(R.id.et_up_title);
        et_up_date = (EditText) findViewById(R.id.et_up_date);
        et_up_startTime = (EditText) findViewById(R.id.et_up_startTime);
        et_up_endTime = (EditText) findViewById(R.id.et_up_endTime);
        et_up_priority = (EditText) findViewById(R.id.et_up_priority);
        et_up_notification = (EditText) findViewById(R.id.et_up_notification);
        et_up_desc = (EditText) findViewById(R.id.et_up_desc);
        // **************************************
        img_btn_back = (ImageButton) findViewById(R.id.backB);
        img_btn_update = (ImageButton) findViewById(R.id.updateB);
        // **************************************
        tv_up_get_location = (TextView) findViewById(R.id.tv_up_get_location);
        tv_up_str_txt = (TextView) findViewById(R.id.tv_up_str_txt);
        btn_up_get_location = (Button) findViewById(R.id.btn_up_get_location);
        // **************************************
        taskHelper = new DataTaskHelper(this);
        int id = getIntent().getIntExtra("rowId", 0);
        if (id != 0) {
            taskHelper.open();

            Task task = taskHelper.getTaskById(id);
            et_up_title.setText(task.get_Title());
            et_up_date.setText(task.get_S_Date());
            et_up_startTime.setText(task.get_Starts());
            et_up_endTime.setText(task.get_Ends());
            tv_up_str_txt.setText(task.get_Category());
            et_up_priority.setText(task.get_Priority());
            tv_up_get_location.setText(task.get_Location());
            et_up_desc.setText(task.get_Details());

            taskHelper.close();
        }
        // ***************** For Category *****************
        TextView up_Meeting = (TextView) findViewById(R.id.up_Meeting);
        TextView up_Shopping = (TextView) findViewById(R.id.up_Shopping);
        TextView up_Works = (TextView) findViewById(R.id.up_Works);
        TextView up_Medical = (TextView) findViewById(R.id.up_Medical);
        TextView up_WatchList = (TextView) findViewById(R.id.up_WatchList);
        TextView up_Important = (TextView) findViewById(R.id.up_Important);
        TextView up_HomeWork = (TextView) findViewById(R.id.up_HomeWork);
        TextView up_Personal = (TextView) findViewById(R.id.up_Personal);
        TextView up_Fitness = (TextView) findViewById(R.id.up_Fitness);
        TextView up_TodoList = (TextView) findViewById(R.id.up_TodoList);
        TextView up_Music = (TextView) findViewById(R.id.up_Music);
        TextView up_School = (TextView) findViewById(R.id.up_School);
        // **************************************
        img_btn_update.setOnClickListener(this);
        up_Meeting.setOnClickListener(this);
        up_Shopping.setOnClickListener(this);
        up_Works.setOnClickListener(this);
        up_Medical.setOnClickListener(this);
        up_WatchList.setOnClickListener(this);
        up_Important.setOnClickListener(this);
        up_HomeWork.setOnClickListener(this);
        up_Personal.setOnClickListener(this);
        up_Fitness.setOnClickListener(this);
        up_TodoList.setOnClickListener(this);
        up_Music.setOnClickListener(this);
        up_School.setOnClickListener(this);

    }

    public void savingDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.saving_your_details);
        alertDialog.setMessage(R.string.are_you_sure_to_save_all_your_information);
        alertDialog.setIcon(getDrawable(R.drawable.save_img));
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String title = et_up_title.getText().toString();
                String detail = et_up_desc.getText().toString();
                String date = et_up_date.getText().toString();
                String endTime = et_up_endTime.getText().toString();
                String startTime = et_up_startTime.getText().toString();
                String category = tv_up_str_txt.getText().toString();
                String priority = et_up_priority.getText().toString();
                int priorityNum = Integer.parseInt(et_up_priority.getText().toString());
                String location = tv_up_get_location.getText().toString();

                int id = getIntent().getIntExtra("rowId", 0);
                Task task = new Task();
                task.set_TaskId(id);
                task.set_Title(title);
                task.set_Details(detail);
                task.set_S_Date(date);
                task.set_Starts(startTime);
                task.set_Ends(endTime);
                task.set_Category(category);
                task.set_Priority(priority);
                task.set_PriorityNum(priorityNum);
                task.set_Location(location);

                taskHelper.open();
                taskHelper.updateByRow(task);
                taskHelper.close();

                final View layout = getLayoutInflater().inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_layout_id));
                CardView lyt_card = (CardView) layout.findViewById(R.id.lyt_card);
                TextView text = (TextView) layout.findViewById(R.id.text);
                final Toast toast = new Toast(getApplicationContext());

                text.setText(R.string.your_details_are_update_and_saved);
                lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_teal_500));
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

                Intent intent = new Intent(UpdateTask.this, ListViewTasks.class);
                startActivityForResult(intent,0);
                finish();
            }
        });
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final View layout = getLayoutInflater().inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_layout_id));
                CardView lyt_card = (CardView) layout.findViewById(R.id.lyt_card);
                TextView text = (TextView) layout.findViewById(R.id.text);
                final Toast toast = new Toast(getApplicationContext());

                text.setText(R.string.your_details_are_not_saved);
                lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_blue_grey_500));
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (img_btn_update == v) {
            savingDialog();
        }

        switch (v.getId()) {
            case R.id.up_Meeting:
                tv_up_str_txt.setText(R.string.tag_meeting);
                break;

            case R.id.up_Shopping:
                tv_up_str_txt.setText(R.string.tag_shopping);
                break;

            case R.id.up_Works:
                tv_up_str_txt.setText(R.string.tag_works);
                break;

            case R.id.up_Medical:
                tv_up_str_txt.setText(R.string.tag_medical);
                break;

            case R.id.up_WatchList:
                tv_up_str_txt.setText(R.string.tag_watch_list);
                break;

            case R.id.up_Important:
                tv_up_str_txt.setText(R.string.tag_important);
                break;

            case R.id.up_HomeWork:
                tv_up_str_txt.setText(R.string.tag_home_work);
                break;

            case R.id.up_Personal:
                tv_up_str_txt.setText(R.string.tag_personal);
                break;

            case R.id.up_Fitness:
                tv_up_str_txt.setText(R.string.tag_fitness);
                break;

            case R.id.up_TodoList:
                tv_up_str_txt.setText(R.string.tag_todo_list);
                break;

            case R.id.up_Music:
                tv_up_str_txt.setText(R.string.tag_music);
                break;

            case R.id.up_School:
                tv_up_str_txt.setText(R.string.tag_school);
                break;
            default:
                break;
        }
    }

    // SHow location as string
    private String hereLocation(Location location) {
        Geocoder geocoder = new Geocoder(UpdateTask.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            return " " +  addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Lat: " + location.getLatitude() + "\nLon: " + location.getLongitude();
    }

    @Override
    public void onLocationChanged(@NonNull Location locations) {
        // Update location
        tv_up_get_location.setText(hereLocation(locations));
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }
}