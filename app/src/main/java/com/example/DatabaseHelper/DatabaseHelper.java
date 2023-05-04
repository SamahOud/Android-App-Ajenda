package com.example.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ajenda_app.MainActivity;

public class DatabaseHelper extends SQLiteOpenHelper {

    // נעביר בבנאי את ה קונטקצת, שם הדאתא וגרסת הדאתא לבנאי של המחלקה שממנה אנחנו יורשים
    public DatabaseHelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    // פונקציות סטטיות שחובה לממש כחלק מהמחלקה שאנחנו יורשים
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // ************************* Insert Name and Password Data *************************
    public Boolean insertData(String username, String password) {

        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        // בעזרת ContentValues נוכל להכניס נתונים לטבלה בשיטה של key , value
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);

        long result = MyDatabase.insert("users", null, contentValues);
        return result != -1;
    }

    // ************************* Update Password *************************
    public Boolean updatePassword(String username, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("password", password);

        long result = MyDatabase.update("users", contentValues, "username=?",
                new String[] {username});
        return result != -1;
    }

    // ************************* Check User Name *************************
    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE username = ?",
                new String[] {username});
        return cursor.getCount() > 0;
    }

    // ************************* Check User Name Password *************************
    public Boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?",
                new String[] {username, password});
        return cursor.getCount() > 0;
    }
}
