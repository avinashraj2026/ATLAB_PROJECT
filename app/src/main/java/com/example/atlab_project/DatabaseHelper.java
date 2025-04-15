package com.example.atlab_project;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    public static final String DATABASE_NAME = "Healthcare.db";
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when DB is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");
        db.execSQL("CREATE TABLE doctors (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, specialization TEXT)");
        db.execSQL("CREATE TABLE appointments (id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, doctorId INTEGER, date TEXT)");

        db.execSQL("INSERT INTO doctors (name, specialization) VALUES ('Dr. Asha Mehta', 'Cardiologist')");
        db.execSQL("INSERT INTO doctors (name, specialization) VALUES ('Dr. Rahul Verma', 'Dermatologist')");
        db.execSQL("INSERT INTO doctors (name, specialization) VALUES ('Dr. Neha Sharma', 'Pediatrician')");
        db.execSQL("INSERT INTO doctors (name, specialization) VALUES ('Dr. Arjun Rao', 'Neurologist')");
        db.execSQL("INSERT INTO doctors (name, specialization) VALUES ('Ravi Kumar', 'Cardiologist')");
        db.execSQL("INSERT INTO doctors (name, specialization) VALUES ('Sneha Sharma', 'Dermatologist')");
    }

    // Called when DB is upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS doctors");
        db.execSQL("DROP TABLE IF EXISTS appointments");
        onCreate(db);
    }

    // Example Method: Insert User
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);
        long result = db.insert("users", null, cv);
        return result != -1;
    }

    // Example Method: Check if user exists
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Cursor getAllDoctors() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM doctors", null);
    }

    public boolean insertAppointment(int userId, int doctorId, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("doctorId", doctorId);
        values.put("date", date);
        long result = db.insert("appointments", null, values);
        return result != -1;
    }

    // New method to get user ID
    public int getUserId(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username=? AND password=?", new String[]{username, password});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        return userId;
    }


    public Cursor getAppointmentsForUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT a.date, d.name, d.specialization " +
                        "FROM appointments a " +
                        "JOIN doctors d ON a.doctorId = d.id " +
                        "WHERE a.userId = ?", new String[]{String.valueOf(userId)}
        );
    }
}


