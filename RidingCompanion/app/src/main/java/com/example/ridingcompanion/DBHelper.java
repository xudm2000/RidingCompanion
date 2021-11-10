package com.example.ridingcompanion;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {
    SQLiteDatabase sqLiteDatabase;

    public DBHelper(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public void createTables(){
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS users " +
                "(id INTEGER PRIMARY KEY, username TEXT PRIMARY KEY, email TEXT, phone TEXT, dob TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS histories " +
                "(id INTEGER PRIMARY KEY, username TEXT, historyID TEXT PRIMARY KEY, distance TEXT, time TEXT, avg_speed TEXT, calories TEXT)");
    }

    public void addUser(String username, String email, String phone, String dob){
        createTables();
        sqLiteDatabase.execSQL(String.format("INSERT INTO users (username, email, phone, dob) VALUES ('%s', '%s','%s','%s')", username, email, phone, dob));
    }

    public void updateUser(String username, String email, String phone, String dob) {
        createTables();
        sqLiteDatabase.execSQL(String.format("UPDATE users set phone = '%s', email = '%s', dob = '%s' where username = '%s'", phone, email, dob, username));
    }

    public User getUser(String username){
        createTables();
        Cursor c = sqLiteDatabase.rawQuery(String.format("Select * from users where username like '%s'", username), null);

        int phoneIndex = c.getColumnIndex("phone");
        int emailIndex = c.getColumnIndex("email");
        int dobIndex = c.getColumnIndex("dob");

        c.moveToFirst();

        if(!c.isAfterLast()){
            String phone = c.getString(phoneIndex);
            String email = c.getString(emailIndex);
            String dob = c.getString(dobIndex);
            return new User(username, email, phone, dob);
        }else{
            return null;
        }
    }
}
