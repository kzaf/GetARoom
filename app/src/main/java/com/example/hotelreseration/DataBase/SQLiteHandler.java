package com.example.hotelreseration.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "user_6";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "ida";
    private static final String KEY_UID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TELEPHONE = "telephone";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY,"  + KEY_UID + " TEXT,"
                + KEY_NAME + " TEXT," + KEY_SURNAME + " TEXT," + KEY_COUNTRY + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_PASSWORD + " TEXT,"
                + KEY_TELEPHONE + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    //Delete the DB
//    public void dropDB(SQLiteDatabase db){
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
//    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String id, String name, String surname, String country, String email, String password, String telephone) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UID, id); // Id
        values.put(KEY_NAME, name); // Name
        values.put(KEY_SURNAME, surname); // Surname
        values.put(KEY_COUNTRY, country); // Country
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_PASSWORD, password); // Password
        values.put(KEY_TELEPHONE, telephone); // Telephone

        // Inserting Row
        Long ide = db.insertOrThrow(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + ide);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        //String[] selectedArgs = {"name","surname", "country", "email", "password"};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("id", cursor.getString(1));
            user.put("name", cursor.getString(2));
            user.put("surname", cursor.getString(3));
            user.put("country", cursor.getString(4));
            user.put("email", cursor.getString(5));
            user.put("password", cursor.getString(6));
            user.put("telephone", cursor.getString(7));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }
//
//    public String getData(String a){
//        SQLiteDatabase db = this.getReadableDatabase();
//        String s;
//        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE name = " + KEY_NAME, null);
//        c.moveToFirst();
//        s=c.getString(c.getColumnIndex(a));
//        db.close();
//
//        return s;
//    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}