package com.example.DatabaseHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.Class.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DataNoteHelper extends SQLiteOpenHelper {
    // Define table name
    String TableName = "table_name";

    // Create constructor
    public DataNoteHelper(@Nullable Context context) {
        super(context, "database_name", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table query
        String sQuery = "create table " + TableName
                + "(id INTEGER primary key autoincrement, title TEXT, detail TEXT, date TEXT)";
        // Execute query
        db.execSQL(sQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table query
        String sQuery = "drop table if exists " + TableName;
        db.execSQL(sQuery); // Execute query
        onCreate(db);       // Create new table
    }

    public void insertData(String title, String detail, String date) {
        // Initialize database
        SQLiteDatabase database = getWritableDatabase();
        // Initialize content values
        ContentValues values = new ContentValues();
        // Add values
        values.put("title", title);
        values.put("detail", detail);
        values.put("date", date);
        // Insert values in database
        database.insertWithOnConflict(TableName,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        database.close(); // Close database
    }

    public void updateData(String id, String title, String detail, String date) {
        // Initialize database
        SQLiteDatabase database = getWritableDatabase();
        // Update query
        String sQuery = "update " + TableName +
                " set title='" + title + "'" +
                ", detail='" + detail + "'" +
                ", date='" + date + "'" +
                " where id='" + id + "'";

        String sortDate = date + " DESC";

        database.execSQL(sQuery); // Execute query

        database.close();         // Close database
    }

    public void deleteData(String id) {
        // Initialize database
        SQLiteDatabase database = getWritableDatabase();
        // Delete query
        String sQuery = "delete from " + TableName + " where id='" + id + "'";
        database.execSQL(sQuery); // Execute query
        database.close();         // Close database
    }

    public void truncateData() {
        // Initialize database
        SQLiteDatabase database = getWritableDatabase();
        // Truncate query
        String sQuery1 = "delete from " + TableName;
        // Reset sequence
        String sQuery2 = "delete from sqlite_sequence where name='" + TableName + "'";
        // Execute queries
        database.execSQL(sQuery1);
        database.execSQL(sQuery2);
        database.close(); // Close database
    }

    public JSONArray getArrayData() {
        // Initialize database
        SQLiteDatabase database = getReadableDatabase();
        // Initialize json array
        JSONArray jsonArray = new JSONArray();
        // Select query
        String sQuery = "select * from " + TableName;
        // Initialize cursor
        Cursor cursor = database.rawQuery(sQuery,null);
        // Check condition
        if (cursor.moveToFirst()) {
            // When cursor move to first item
            do {
                // Initialize json object
                JSONObject object = new JSONObject();
                try {
                    // Put all values in object
                    object.put("id", cursor.getString(0));
                    object.put("title", cursor.getString(1));
                    object.put("detail", cursor.getString(2));
                    object.put("date", cursor.getString(3));
                    // Add values in json array
                    jsonArray.put(object);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();   // Close cursor
        database.close(); // Close database
        return jsonArray; // Pass json array
    }
}
