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
                "(username TEXT PRIMARY KEY, email TEXT, phone TEXT, dob TEXT, weight TEXT, height TEXT, gender TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS rankusers " +
                "(username TEXT PRIMARY KEY, numOfDay INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS histories " +
                "(historyID TEXT PRIMARY KEY, username TEXT, distance TEXT, time TEXT, avg_speed TEXT, calories TEXT, date TEXT, image TEXT, route TEXT)");
    }

    public void addUser(String username, String email, String phone, String dob, String weight, String height, String gender){
        createTables();
        sqLiteDatabase.execSQL(String.format("INSERT INTO users (username, email, phone, dob, weight, height, gender) VALUES ('%s', '%s','%s','%s', '%s', '%s', '%s')", username, email, phone, dob, weight, height, gender));
    }

    public void updateUser(String username, String email, String phone, String dob, String weight, String height, String gender) {
        createTables();
        sqLiteDatabase.execSQL(String.format("UPDATE users set phone = '%s', email = '%s', dob = '%s', weight = '%s', height = '%s', gender = '%s' where username = '%s'", phone, email, dob, weight, height, gender, username));
    }

    public User getUser(String username){
        createTables();
        Cursor c = sqLiteDatabase.rawQuery(String.format("Select * from users where username like '%s'", username), null);

        int phoneIndex = c.getColumnIndex("phone");
        int emailIndex = c.getColumnIndex("email");
        int weightIndex = c.getColumnIndex("weight");
        int heightIndex = c.getColumnIndex("height");
        int genderIndex = c.getColumnIndex("gender");
        int dobIndex = c.getColumnIndex("dob");

        c.moveToFirst();

        if(!c.isAfterLast()){
            String phone = c.getString(phoneIndex);
            String email = c.getString(emailIndex);
            String dob = c.getString(dobIndex);
            String weight = c.getString(weightIndex);
            String height = c.getString(heightIndex);
            String gender = c.getString(genderIndex);
            return new User(username, email, phone, dob, weight, height, gender);
        }else{
            return null;
        }
    }

    public void addRankUser(String username, int numOfDay){
        createTables();
        RankUser user = getRankUser(username);
        if(user == null) {
            sqLiteDatabase.execSQL(String.format("INSERT INTO rankusers (username, numOfDay) VALUES ('%s', '%s')", username, String.valueOf(numOfDay)));
        }else if(numOfDay != user.numOfDay){
            updateRankUser(username, numOfDay);
        }
    }

    public void updateRankUser(String username, int numOfDay) {
        createTables();
        sqLiteDatabase.execSQL(String.format("UPDATE rankusers set numOfDay = '%s' where username = '%s'", String.valueOf(numOfDay), username));
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
            c.moveToNext();
        }

        return users;
    }

    public void addHistory(String username, String distance, String time, String avg_speed, String calories, String date, String image, String route){
        createTables();
        sqLiteDatabase.execSQL(String.format("INSERT INTO histories (username, distance, time, avg_speed, calories, date, image, route) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", username, distance, time, avg_speed, calories, date, image, route));
    }

    public ArrayList<History> getUserHistory(String username){
        createTables();
        Cursor c = sqLiteDatabase.rawQuery(String.format("Select * from histories where username like '%s'", username), null);

        int distanceIndex = c.getColumnIndex("distance");
        int timeIndex = c.getColumnIndex("time");
        int speedIndex = c.getColumnIndex("avg_speed");
        int caloriesIndex = c.getColumnIndex("calories");
        int dateIndex = c.getColumnIndex("date");
        int imageIndex = c.getColumnIndex("image");
        int routeIndex = c.getColumnIndex("route");

        c.moveToFirst();
        ArrayList<History> histories = new ArrayList<>();

        while (!c.isAfterLast()){
            String distance = c.getString(distanceIndex);
            String time = c.getString(timeIndex);
            String avg_speed = c.getString(speedIndex);
            String calories = c.getString(caloriesIndex);
            String date = c.getString(dateIndex);
            String image = c.getString(imageIndex);
            String route = c.getString(routeIndex);
            histories.add(new History(username, distance, time, avg_speed, calories, date, image, route));
            c.moveToNext();
        }

        return histories;
    }
}
