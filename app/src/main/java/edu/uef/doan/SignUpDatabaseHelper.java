package edu.uef.doan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SignUpDatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "Signup.db";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    private static final String TABLE_USERS = "users";

    // Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_FULLNAME = "fullname";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";

    public SignUpDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create table
        String CREATE_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_FULLNAME + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PHONE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Them user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PASSWORD, user.getPassword());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // Lay username
    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[] {KEY_ID, KEY_USERNAME, KEY_PASSWORD},
                KEY_USERNAME + "=?",
                new String[] {username},
                null, null, null);
        User user = null; // Khởi tạo user là null

        if (cursor != null && cursor.moveToFirst()) {
            user = new User(
                    cursor.getString(1),
                    cursor.getString(2));
            user.setId(cursor.getInt(0));
        }

        if (cursor != null) {
            cursor.close(); // Đảm bảo đóng Cursor khi bạn đã sử dụng xong
        }

        return user;
    }

    // Lay password
    public User getUserByPassword(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[] {KEY_ID, KEY_USERNAME, KEY_PASSWORD},
                KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?",
                new String[] {username, password},
                null, null, null);
        User user = null; // Khởi tạo user là null

        if (cursor != null && cursor.moveToFirst()) {
            user = new User(username, password);
            user.setId(cursor.getInt(0));
        }

        if (cursor != null) {
            cursor.close(); // Đảm bảo đóng Cursor khi bạn đã sử dụng xong
        }

        return user;
    }


    // Cap nhat user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FULLNAME, user.getFullname());
        values.put(KEY_IMAGE, user.getImage());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PHONE, user.getPhone());
        return db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[] { String.valueOf(user.getId()) });
    }
}
