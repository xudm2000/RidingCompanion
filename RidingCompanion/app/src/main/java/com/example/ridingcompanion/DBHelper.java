package com.example.ridingcompanion;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBHelper {
    SQLiteDatabase sqLiteDatabase;

    public DBHelper(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public void createTables(){
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS users " +
                "(username TEXT PRIMARY KEY, email TEXT, phone TEXT, dob TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS rankusers " +
                "(username TEXT PRIMARY KEY, numOfDay INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS histories " +
                "(historyID TEXT PRIMARY KEY, username TEXT, distance TEXT, time TEXT, avg_speed TEXT, calories TEXT)");
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

    public void addRankUser(String username, int numOfDay){
        createTables();
        sqLiteDatabase.execSQL(String.format("INSERT INTO rankusers (username, numOfDay) VALUES ('%s', '%s')", username, String.valueOf(numOfDay)));
    }

    public void updateRankUser(String username, String email, String phone, String dob) {
        createTables();
        RankUser user = getRankUser(username);
        sqLiteDatabase.execSQL(String.format("UPDATE rankusers set numOfDay = '%s' where username = '%s'", String.valueOf(user.numOfDay+1), username));
    }

    public RankUser getRankUser(String username){
        createTables();
        Cursor c = sqLiteDatabase.rawQuery(String.format("Select * from rankusers where username like '%s'", username), null);

        int numIndex = c.getColumnIndex("numOfDay");

        c.moveToFirst();

        if(!c.isAfterLast()){
            int numOfDay = c.getInt(numIndex);
            return new RankUser(username, numOfDay);
        }else{
            return null;
        }
    }

    public ArrayList<RankUser> top5Users(){
        createTables();
        Cursor c = sqLiteDatabase.rawQuery("Select * from rankusers order by numOfDay DESC", null);
        int usernameIndex = c.getColumnIndex("username");
        int numIndex = c.getColumnIndex("numOfDay");

        ArrayList<RankUser> users = new ArrayList<>();
        c.moveToFirst();

        while(!c.isAfterLast()){
            String username = c.getString(usernameIndex);
            int numOfDay = c.getInt(numIndex);
            users.add(new RankUser(username, numOfDay));
            if(users.size() >= 5){
                break;
            }
            c.moveToNext();
        }

        return users;
    }
}
