package com.example.DatabaseHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.Class.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DataTaskHelper extends SQLiteOpenHelper {
    // נעביר בבנאי את ה context, שם ה database וגרסת ה database לבנאי של המחלקה ממנה אנחנו יורשים
    public DataTaskHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // מפעלת פעם אחת ביצירת מסד הנתונים והטבלאות
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASK);
        db.execSQL(CREATE_TABLE_DONE);
        db.execSQL(CREATE_TABLE_TRASH);
    }

    // בפונקציה onUpgrade נריץ את השאילתא DROP TABLE IF EXISTS שמוחקת את הטבלה שלנו, ונעביר לה את ה TABLE_TASKS
    // העדכני כדי שתיצור אותו מחדש עם הנתונים העדכניים
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DONE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRASH);
        onCreate(db);
    }

    // מקנה גישה למסד הניתונים
    // נצהיר על SQLiteDatabase
    SQLiteDatabase database;

    // לטובת הנדסת תכנה נכונה ומניעת באגים צפויים...
    // הגדרנו את הקבועים כשסטטיים כדי שנוכל לגשת אליהם מכל class
    // נצהיר בקבועים סטטיים על שם ה database , שם הטבלה שבה יופיעו המוצרים וגרסת ה database
    public static final String DATABASE_NAME = "task.db";
    public static final String TABLE_TASKS = "tableTasks";
    public static final String TABLE_DONE = "tableDone";
    public static final String TABLE_TRASH = "tableTrash";
    public static final int DATABASE_VERSION = 1;

    public static final String COLUMN_ID_DONE = "idDone";
    public static final String COLUMN_ID_TRASH = "idTrash";

    // נצהיר על קבועים סטטיים של שמות העמודים שיהיו בטבלה
    public static final String COLUMN_ID = "taskId";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DETAILS = "details";
    public static final String COLUMN_START_DATE = "startDate";
    public static final String COLUMN_START_TIME = "startTime";
    public static final String COLUMN_END_TIME = "entTime";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_PRIORITY_NUM = "priorityNum";
    public static final String COLUMN_LOCATION = "location";
    public static final String KEY_CREATED_AT = "created_at";
    // ******************************************************

    // CREATE TABLES :
    // PRIMARY KEY AUTOINCREMENT means >>
    // הגדרנו את עמודת ה taskId לעלות ב1 אוטומטית בכל פעם שנכניס מוצר חדש לטבלה

    // ************************* CREATE TABLE TASK *************************
    private static final String CREATE_TABLE_TASK = "CREATE TABLE IF NOT EXISTS " +
            TABLE_TASKS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " VARCHAR, " + COLUMN_DETAILS + " VARCHAR, " +
            COLUMN_START_DATE + " VARCHAR, " +
            COLUMN_START_TIME + " VARCHAR, " + COLUMN_END_TIME + " VARCHAR, " +
            COLUMN_CATEGORY + " VARCHAR, " +
            COLUMN_PRIORITY + " VARCHAR, " + COLUMN_LOCATION + " VARCHAR, " +
            COLUMN_PRIORITY_NUM + " INTEGER " + ");";

    // ************************* CREATE TABLE DONE *************************
    private static final String CREATE_TABLE_DONE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_DONE + "(" + COLUMN_ID_DONE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " VARCHAR, " + COLUMN_DETAILS + " VARCHAR, " +
            COLUMN_START_DATE + " VARCHAR, " +
            COLUMN_START_TIME + " VARCHAR, " + COLUMN_END_TIME + " VARCHAR, " +
            COLUMN_CATEGORY + " VARCHAR, " +
            COLUMN_PRIORITY + " VARCHAR, " + COLUMN_LOCATION + " VARCHAR, " +
            COLUMN_PRIORITY_NUM + " INTEGER " + ");";

    // ************************* CREATE TABLE TRASH *************************
    private static final String CREATE_TABLE_TRASH = "CREATE TABLE IF NOT EXISTS " +
            TABLE_TRASH + "(" + COLUMN_ID_TRASH + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " VARCHAR, " + COLUMN_DETAILS + " VARCHAR, " +
            COLUMN_START_DATE + " VARCHAR, " +
            COLUMN_START_TIME + " VARCHAR, " + COLUMN_END_TIME + " VARCHAR, " +
            COLUMN_CATEGORY + " VARCHAR, " +
            COLUMN_PRIORITY + " VARCHAR, " + COLUMN_LOCATION + " VARCHAR, " +
            COLUMN_PRIORITY_NUM + " INTEGER " + ");";



    // נצהיר על מערך String שבו יהיו שמות כל העמודות שבטבלה
    private final String[] allColumnTask = { COLUMN_ID, COLUMN_TITLE, COLUMN_DETAILS,
            COLUMN_START_DATE, COLUMN_START_TIME,COLUMN_END_TIME,
            COLUMN_CATEGORY, COLUMN_PRIORITY,
            COLUMN_LOCATION, COLUMN_PRIORITY_NUM };


    private final String[] allTrashColumn = { COLUMN_ID_TRASH, COLUMN_TITLE, COLUMN_DETAILS,
            COLUMN_START_DATE, COLUMN_START_TIME,COLUMN_END_TIME,
            COLUMN_CATEGORY, COLUMN_PRIORITY,
            COLUMN_LOCATION, COLUMN_PRIORITY_NUM };


    private final String[] allDoneColumn = { COLUMN_ID_DONE, COLUMN_TITLE, COLUMN_DETAILS,
            COLUMN_START_DATE, COLUMN_START_TIME,COLUMN_END_TIME,
            COLUMN_CATEGORY, COLUMN_PRIORITY,
            COLUMN_LOCATION, COLUMN_PRIORITY_NUM };



    // ************************* Open *************************
    // בפונקציה open נהפוך את ה database לאפשרי לעריכה
    public void open() {
        database = this.getWritableDatabase();
        Log.i("Data:-", "Database connection Open");
    }

    // ************************* Insert Task *************************
    // להכניס מוצר חדש לטבלה
    public Task insertTask(Task task) {
        // בעזרת ContentValues נוכל להכניס נתונים לטבלה בשיטה של key , value
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(COLUMN_TITLE, task.get_Title());
        values.put(COLUMN_DETAILS, task.get_Details());
        values.put(COLUMN_START_DATE, task.get_S_Date());
        values.put(COLUMN_START_TIME, task.get_Starts());
        values.put(COLUMN_END_TIME, task.get_Ends());
        values.put(COLUMN_CATEGORY, task.get_Category());
        values.put(COLUMN_PRIORITY, task.get_Priority());
        values.put(COLUMN_PRIORITY_NUM, task.get_PriorityNum());
        values.put(COLUMN_LOCATION, task.get_Location());

        // נכניס את המוצר החדש לטבלה עם ה ContentValues ויוחזר לנו ID
        int insertId = (int) database.insert(TABLE_TASKS, null, values);

        Log.d("Data", "Task: " + insertId + " insert to database");
        // נגדיר את ה ID שחזר אלינו למוצר שהכנסנו לטבלה
        task.set_TaskId(insertId);

        return task;
    }
    // ************************* Insert Trash *************************
    // להכניס מוצר חדש לטבלה
    public Task insertTrash(Task task) {
        // בעזרת ContentValues נוכל להכניס נתונים לטבלה בשיטה של ket , value
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(COLUMN_TITLE, task.get_Title());
        values.put(COLUMN_DETAILS, task.get_Details());
        values.put(COLUMN_START_DATE, task.get_S_Date());
        values.put(COLUMN_START_TIME, task.get_Starts());
        values.put(COLUMN_END_TIME, task.get_Ends());
        values.put(COLUMN_CATEGORY, task.get_Category());
        values.put(COLUMN_PRIORITY, task.get_Priority());
        values.put(COLUMN_PRIORITY_NUM, task.get_PriorityNum());
        values.put(COLUMN_LOCATION, task.get_Location());

        // נכניס את המוצר החדש לטבלה עם ה ContentValues ויוחזר לנו ID
        int insertId = (int) database.insert(TABLE_TRASH, null, values);

        Log.d("Data", "Trash: " + insertId + " insert to database");
        // נגדיר את ה ID שחזר אלינו למוצר שהכנסנו לטבלה
        task.set_TaskId(insertId);

        return task;
    }
    // ************************* Insert Done *************************
    // להכניס מוצר חדש לטבלה
    public Task insertDone(Task task) {
        // בעזרת ContentValues נוכל להכניס נתונים לטבלה בשיטה של ket , value
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(COLUMN_TITLE, task.get_Title());
        values.put(COLUMN_DETAILS, task.get_Details());
        values.put(COLUMN_START_DATE, task.get_S_Date());
        values.put(COLUMN_START_TIME, task.get_Starts());
        values.put(COLUMN_END_TIME, task.get_Ends());
        values.put(COLUMN_CATEGORY, task.get_Category());
        values.put(COLUMN_PRIORITY, task.get_Priority());
        values.put(COLUMN_PRIORITY_NUM, task.get_PriorityNum());
        values.put(COLUMN_LOCATION, task.get_Location());

        // נכניס את המוצר החדש לטבלה עם ה ContentValues ויוחזר לנו ID
        int insertId = (int) database.insert(TABLE_DONE, null, values);

        Log.d("Data", "Done: " + insertId + " insert to database");
        // נגדיר את ה ID שחזר אלינו למוצר שהכנסנו לטבלה
        task.set_TaskId(insertId);

        return task;
    }

    // ************************* UPDATE *************************
    // נממש את הפונקציה שמעדכנת את הנתונים של המוצר בטבלה:
    public long updateByRow(Task task) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, task.get_Title());
        values.put(COLUMN_DETAILS, task.get_Details());
        values.put(COLUMN_START_DATE, task.get_S_Date());
        values.put(COLUMN_START_TIME, task.get_Starts());
        values.put(COLUMN_END_TIME, task.get_Ends());
        values.put(COLUMN_CATEGORY, task.get_Category());
        values.put(COLUMN_PRIORITY, task.get_Priority());
        values.put(COLUMN_PRIORITY_NUM, task.get_PriorityNum());
        values.put(COLUMN_LOCATION, task.get_Location());

        // נעדכן את המוצר בטבלה ונחזיר את ה ID שלו
        return database.update(TABLE_TASKS, values, COLUMN_ID + "=" + task.get_TaskId(), null);
    }

    @SuppressLint("Range")
    // ************************* Get Task By Id *************************
    public Task getTaskById(int rowId) {
        // נצביע לטבלה, ונגדיר למצביע שלנו לעבור על כל העמודות של המוצר שה id שלו שווה ל id שהעברנו כפרמטר של הפונקציה
        Cursor cursor = database.query(TABLE_TASKS, allColumnTask, COLUMN_ID + " = " + rowId,
                null, null, null, null);
        // נצביע למוצר שעונה על קריטריון ה id שלנו
        cursor.moveToFirst();
        // במידה ובאמת קיים מוצר שעונה על הקריטריון שלנו והמצביע מצביע אליו
        if (cursor.getCount() > 0) {
            // נשמור את הנתונים של המוצר

            Task task = new Task();
            task.set_TaskId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            task.set_Title((cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))));
            task.set_Details((cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS))));
            task.set_S_Date((cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE))));
            task.set_Starts((cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME))));
            task.set_Ends((cursor.getString(cursor.getColumnIndex(COLUMN_END_TIME))));
            task.set_Category((cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY))));
            task.set_Priority((cursor.getString(cursor.getColumnIndex(COLUMN_PRIORITY))));
            task.set_PriorityNum((cursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY_NUM))));
            task.set_Location((cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION))));

            return task;
        }
        return null;
    }

    @SuppressLint("Range")
    // ************************* Get All Tasks *************************
    // מימוש הפונקציה getAllTasks שתציג למשתמש את כל המוצרים שבטבלה
    public ArrayList<Task> getAllTasks() {
        // נצהיר על ArrayList שבהמשך נוסיף עליו את כל המוצרים שבטבלה
        ArrayList<Task> taskArrayList = new ArrayList<>();
        String sortDate = COLUMN_START_DATE + " ASC"; // // sorting by date (DESC)

        // נגדיר מצביע שיצביע לטבלה ויעבור על הנתונים של כל העמודות
        Cursor cursor = database.query(TABLE_TASKS, allColumnTask,
                null, null, null, null, sortDate);
        if (cursor.getCount() > 0) {
            // נעבור עם לולאה על כל המוצרים שבטבלה עד שלא יישארו עוד כאלו
            while (cursor.moveToNext()) {

                Task task = new Task();
                task.set_TaskId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                task.set_Title((cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))));
                task.set_Details((cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS))));
                task.set_S_Date((cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE))));
                task.set_Starts(cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME)));
                task.set_Ends((cursor.getString(cursor.getColumnIndex(COLUMN_END_TIME))));
                task.set_Category((cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY))));
                task.set_Priority((cursor.getString(cursor.getColumnIndex(COLUMN_PRIORITY))));
                task.set_PriorityNum((cursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY_NUM))));
                task.set_Location((cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION))));

                taskArrayList.add(task); // נוסיף את המוצר ל ArrayList שהגדרנו
            }
        }
        return taskArrayList; // נחזיר את ה ArrayList
    }

    @SuppressLint("Range")
    // ************************* Search For This Date *************************
    public ArrayList<Task> searchForThisDate() {
        ArrayList<Task> taskArrayList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        String strDate = sdf.format(new Date());

        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_START_DATE +
                " LIKE '" + strDate + "%'";

        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            // נעבור עם לולאה על כל המוצרים שבטבלה עד שלא יישארו עוד כאלו
            while (cursor.moveToNext()) {
                Task task = new Task();
                task.set_TaskId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                task.set_Title((cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))));
                task.set_Details((cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS))));
                task.set_S_Date((cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE))));
                task.set_Starts((cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME))));
                task.set_Ends((cursor.getString(cursor.getColumnIndex(COLUMN_END_TIME))));
                task.set_Category((cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY))));
                task.set_Priority((cursor.getString(cursor.getColumnIndex(COLUMN_PRIORITY))));
                task.set_PriorityNum((cursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY_NUM))));
                task.set_Location((cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION))));

                taskArrayList.add(task); // נוסיף את המוצר ל ArrayList שהגדרנו
            }
        }
        return taskArrayList;
    }

    @SuppressLint("Range")
    // ************************* Get All Trash Tasks *************************
    // מימוש הפונקציה getAllTasks שתציג למשתמש את כל המוצרים שבטבלה
    public ArrayList<Task> getAllTrashTasks() {
        // נצהיר על ArrayList שבהמשך נוסיף עליו את כל המוצרים שבטבלה
        ArrayList<Task> taskArrayList = new ArrayList<>();
        String sortDate = COLUMN_START_DATE + " ASC"; // // sorting by date (DESC)
        // נגדיר מצביע שיצביע לטבלה ויעבור על הנתונים של כל העמודות
        Cursor cursor = database.query(TABLE_TRASH, allTrashColumn,
                null, null, null, null, sortDate);

        if (cursor.getCount() > 0) {
            // נעבור עם לולאה על כל המוצרים שבטבלה עד שלא יישארו עוד כאלו
            while (cursor.moveToNext()) {

                Task task = new Task();
                task.set_TaskId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_TRASH)));
                task.set_Title((cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))));
                task.set_Details((cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS))));
                task.set_S_Date((cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE))));
                task.set_Ends((cursor.getString(cursor.getColumnIndex(COLUMN_END_TIME))));
                task.set_Starts((cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME))));
                task.set_Category((cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY))));
                task.set_Priority((cursor.getString(cursor.getColumnIndex(COLUMN_PRIORITY))));
                task.set_PriorityNum((cursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY_NUM))));
                task.set_Location((cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION))));

                taskArrayList.add(task); // נוסיף את המוצר ל ArrayList שהגדרנו
            }
        }
        return taskArrayList; // נחזיר את ה ArrayList
    }
    @SuppressLint("Range")
    // ************************* Get All Done Tasks *************************
    // מימוש הפונקציה getAllTasks שתציג למשתמש את כל המוצרים שבטבלה
    public ArrayList<Task> getAllDoneTasks() {
        // נצהיר על ArrayList שבהמשך נוסיף עליו את כל המוצרים שבטבלה
        String sortDate = COLUMN_START_DATE + " ASC"; // // sorting by date (DESC)
        ArrayList<Task> taskArrayList = new ArrayList<>();
        // נגדיר מצביע שיצביע לטבלה ויעבור על הנתונים של כל העמודות
        Cursor cursor = database.query(TABLE_DONE, allDoneColumn,
                null, null, null, null, sortDate);

        if (cursor.getCount() > 0) {
            // נעבור עם לולאה על כל המוצרים שבטבלה עד שלא יישארו עוד כאלו
            while (cursor.moveToNext()) {

                Task task = new Task();
                task.set_TaskId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_DONE)));
                task.set_Title((cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))));
                task.set_Details((cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS))));
                task.set_S_Date((cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE))));
                task.set_Ends((cursor.getString(cursor.getColumnIndex(COLUMN_END_TIME))));
                task.set_Starts((cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME))));
                task.set_Category((cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY))));
                task.set_Priority((cursor.getString(cursor.getColumnIndex(COLUMN_PRIORITY))));
                task.set_PriorityNum((cursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY_NUM))));
                task.set_Location((cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION))));

                taskArrayList.add(task); // נוסיף את המוצר ל ArrayList שהגדרנו
            }
        }
        return taskArrayList; // נחזיר את ה ArrayList
    }


    // ************************* Get All Tasks By Filter *************************
    public ArrayList<Task> getAllTasksByFilter(String selection, String OrderBy) {
        Cursor cursor = database.query(TABLE_TASKS, allColumnTask, selection,
                null, null, null, OrderBy);
        ArrayList<Task> task = convertCursorToList(cursor);
        return task;
    }

    @SuppressLint("Range")
    public ArrayList<Task> convertCursorToList(Cursor cursor) {
        ArrayList<Task> taskArrayList = new ArrayList<Task>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Task task = new Task();
                task.set_TaskId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                task.set_Title((cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))));
                task.set_Details((cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS))));
                task.set_S_Date((cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE))));
                task.set_Starts((cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME))));
                task.set_Ends((cursor.getString(cursor.getColumnIndex(COLUMN_END_TIME))));
                task.set_Category((cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY))));
                task.set_Priority((cursor.getString(cursor.getColumnIndex(COLUMN_PRIORITY))));
                task.set_PriorityNum((cursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY_NUM))));
                task.set_Location((cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION))));

                taskArrayList.add(task);
            }
        }
        return taskArrayList;
    }

    @SuppressLint("Range")
    // ************************* Search By Category *************************
    public ArrayList<Task> searchByCategory(String category) {
        ArrayList<Task> taskArrayList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_CATEGORY +
                " LIKE '" + category + "%'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            // נעבור עם לולאה על כל המוצרים שבטבלה עד שלא יישארו עוד כאלו
            while (cursor.moveToNext()) {
                Task task = new Task();
                task.set_TaskId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                task.set_Title((cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))));
                task.set_Details((cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS))));
                task.set_S_Date((cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE))));
                task.set_Starts((cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME))));
                task.set_Ends((cursor.getString(cursor.getColumnIndex(COLUMN_END_TIME))));
                task.set_Category((cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY))));
                task.set_Priority((cursor.getString(cursor.getColumnIndex(COLUMN_PRIORITY))));
                task.set_PriorityNum((cursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY_NUM))));
                task.set_Location((cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION))));

                taskArrayList.add(task); // נוסיף את המוצר ל ArrayList שהגדרנו
            }
        }
        return taskArrayList;
    }

    // ************************* Delete Done By Row *************************
    public void deleteDoneByRow(int rowId) {
        database.delete(TABLE_DONE, COLUMN_ID_DONE + "=" + rowId, null);
    }

    // ************************* Delete Trash By Row *************************
    public void deleteTrashByRow(int rowId) {
        database.delete(TABLE_TRASH, COLUMN_ID_TRASH + "=" + rowId, null);
    }

    // ************************* Delete Task By Row *************************
    public void deleteTaskByRow(int rowId) {
        database.delete(TABLE_TASKS, COLUMN_ID + "=" + rowId, null);
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
