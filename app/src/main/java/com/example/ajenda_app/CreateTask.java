package com.example.ajenda_app;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
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

public class CreateTask extends AppCompatActivity implements View.OnClickListener, IBaseGpsListener {
    private EditText et_title, et_date, et_startTime, et_endTime,
            et_priority, et_notification, et_desc;
    private ImageButton img_btn_back, img_btn_save;
    // *****************************************
    private DataTaskHelper taskHelper;
    // *****************************************
    private AlarmReceiver alarmReceiver;
    private int mYear, mMonth, mDay, mHourS, mMinuteS;
    // *****************************************
    private TextView tv_get_location,tv_str_txt;
    private Button btn_get_location;
    private static final int PERMISSION_LOCATION = 1000;
    // *****************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

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
                Intent intent = new Intent(CreateTask.this, HomePage.class);
                startActivity(intent);
            }
        });
        // *****************************************
        et_date.setInputType(InputType.TYPE_NULL);
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(et_date);
            }
        });
        et_startTime.setInputType(InputType.TYPE_NULL);
        et_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(et_startTime);
            }
        });
        et_endTime.setInputType(InputType.TYPE_NULL);
        et_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(et_endTime);
            }
        });
        // *****************************************
        et_notification.setInputType(InputType.TYPE_NULL);
        et_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] items = {"Add", "Undo"};
                // Create a dialog to choose an action
                AlertDialog.Builder dialog = new AlertDialog.Builder(CreateTask.this);
                dialog.setTitle(R.string.choose_an_action);
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) { // Add
                            if (et_date.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(CreateTask.this, R.string.date_is_empty, Toast.LENGTH_SHORT).show();
                            } else if (et_startTime.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(CreateTask.this, R.string.time_is_empty, Toast.LENGTH_SHORT).show();
                            } else if (TextUtils.isEmpty(et_title.getText().toString())) {
                                et_title.setError("Message can't be empty!");
                            } else {
                                alarmReceiver.setOneTimeAlarm(CreateTask.this, AlarmReceiver.TYPE_ONE_TIME,
                                        et_date.getText().toString(), et_startTime.getText().toString(),
                                        et_title.getText().toString());
                                et_notification.setText(R.string.add);
                            }
                        } else { // Undo
                            et_notification.setText(R.string.undo);
                        }
                    }
                });
                dialog.show();
            }
        });
        btn_get_location.setOnClickListener(new View.OnClickListener() {
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
        new DatePickerDialog(CreateTask.this, dateSetListener,
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
        new TimePickerDialog(CreateTask.this, timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false).show();
    }
    // =================================================
    @SuppressLint("MissingPermission")
    private void showLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Check if GPS enabled
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Start location
            tv_get_location.setText("Loading Location...");
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

        if (requestCode == PERMISSION_LOCATION) {               // 0
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showLocation();
            }
            else {
                Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    // =================================================
    public void initView() {
        et_title = (EditText) findViewById(R.id.et_title);
        et_date = (EditText) findViewById(R.id.et_date);
        et_startTime = (EditText) findViewById(R.id.et_startTime);
        et_endTime = (EditText) findViewById(R.id.et_endTime);
        et_priority = (EditText) findViewById(R.id.et_priority);
        et_notification = (EditText) findViewById(R.id.et_notification);
        et_desc = (EditText) findViewById(R.id.et_desc);
        // **************************************
        img_btn_back = (ImageButton) findViewById(R.id.backB);
        img_btn_save = (ImageButton) findViewById(R.id.saveB);
        // **************************************
        tv_get_location = (TextView) findViewById(R.id.tv_get_location);
        tv_str_txt = (TextView) findViewById(R.id.tv_str_txt);
        btn_get_location = (Button) findViewById(R.id.btn_get_location);
        // **************************************
        taskHelper = new DataTaskHelper(this);
        // ***************** For Category *****************
        TextView f_Meeting = (TextView) findViewById(R.id.f_Meeting);
        TextView f_Shopping = (TextView) findViewById(R.id.f_Shopping);
        TextView f_Works = (TextView) findViewById(R.id.f_Works);
        TextView f_Medical = (TextView) findViewById(R.id.f_Medical);
        TextView f_WatchList = (TextView) findViewById(R.id.f_WatchList);
        TextView f_Important = (TextView) findViewById(R.id.f_Important);
        TextView f_HomeWork = (TextView) findViewById(R.id.f_HomeWork);
        TextView f_Personal = (TextView) findViewById(R.id.f_Personal);
        TextView f_Fitness = (TextView) findViewById(R.id.f_Fitness);
        TextView f_TodoList = (TextView) findViewById(R.id.f_TodoList);
        TextView f_Music = (TextView) findViewById(R.id.f_Music);
        TextView f_School = (TextView) findViewById(R.id.f_School);
        // **************************************
        img_btn_save.setOnClickListener(this);
        f_Meeting.setOnClickListener(this);
        f_Shopping.setOnClickListener(this);
        f_Works.setOnClickListener(this);
        f_Medical.setOnClickListener(this);
        f_WatchList.setOnClickListener(this);
        f_Important.setOnClickListener(this);
        f_HomeWork.setOnClickListener(this);
        f_Personal.setOnClickListener(this);
        f_Fitness.setOnClickListener(this);
        f_TodoList.setOnClickListener(this);
        f_Music.setOnClickListener(this);
        f_School.setOnClickListener(this);
    }

    public void savingDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.saving_your_details);
        alertDialog.setMessage(R.string.are_you_sure_to_save_all_your_information);
        alertDialog.setIcon(getDrawable(R.drawable.save_img));
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String title = et_title.getText().toString();
                String detail = et_desc.getText().toString();
                String date = et_date.getText().toString();
                String endTime = et_endTime.getText().toString();
                String startTime = et_startTime.getText().toString();
                String category = tv_str_txt.getText().toString();
                String priority = et_priority.getText().toString();
                int priorityNum = Integer.parseInt(et_priority.getText().toString());
                String location = tv_get_location.getText().toString();

                Task task = new Task();
                task.set_TaskId(0);
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
                taskHelper.insertTask(task);
                taskHelper.close();

                Intent intent = new Intent(CreateTask.this, ListViewTasks.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CreateTask.this, R.string.your_details_are_not_saved, Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.create();
        alertDialog.show();
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (img_btn_save == v) {
            savingDialog();
        }

        switch (v.getId()) {
            case R.id.f_Meeting:
                tv_str_txt.setText(R.string.tag_meeting);
                break;

            case R.id.f_Shopping:
                tv_str_txt.setText(R.string.tag_shopping);
                break;

            case R.id.f_Works:
                tv_str_txt.setText(R.string.tag_works);
                break;

            case R.id.f_Medical:
                tv_str_txt.setText(R.string.tag_medical);
                break;

            case R.id.f_WatchList:
                tv_str_txt.setText(R.string.tag_watch_list);
                break;

            case R.id.f_Important:
                tv_str_txt.setText(R.string.tag_important);
                break;

            case R.id.f_HomeWork:
                tv_str_txt.setText(R.string.tag_home_work);
                break;

            case R.id.f_Personal:
                tv_str_txt.setText(R.string.tag_personal);
                break;

            case R.id.f_Fitness:
                tv_str_txt.setText(R.string.tag_fitness);
                break;

            case R.id.f_TodoList:
                tv_str_txt.setText(R.string.tag_todo_list);
                break;

            case R.id.f_Music:
                tv_str_txt.setText(R.string.tag_music);
                break;

            case R.id.f_School:
                tv_str_txt.setText(R.string.tag_school);
                break;
            default:
                break;
        }
    }

    // SHow location as string
    private String hereLocation(Location location) {
        Geocoder geocoder = new Geocoder(CreateTask.this, Locale.getDefault());
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
        tv_get_location.setText(hereLocation(locations));
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