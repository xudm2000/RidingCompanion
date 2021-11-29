package com.example.ridingcompanion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Base64;

public class HistoryPage extends AppCompatActivity {

    private Button back;
    private TextView dateView;
    private TextView speedView;
    private TextView distanceView;
    private TextView timeView;
    private TextView caloriesView;
    private ImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);

        back = (Button) findViewById(R.id.backButton);
        dateView = (TextView) findViewById(R.id.dateInfo);
        speedView = (TextView) findViewById(R.id.speedInfo);
        timeView = (TextView) findViewById(R.id.timeInfo);
        distanceView = (TextView) findViewById(R.id.distanceInfo);
        caloriesView = (TextView) findViewById(R.id.caleriesInfo);
        imageView = (ImageView) findViewById(R.id.imageView3);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        imageView.setMinimumWidth(width/2);
        imageView.setMinimumHeight(height/3);

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        String speed = intent.getStringExtra("speed");
        String distance = intent.getStringExtra("distance");
        String time = intent.getStringExtra("time");
        String calories = intent.getStringExtra("calories");
        String image = intent.getStringExtra("image");

        dateView.setText("Date: " + date);
        speedView.setText("Speed: " + speed + " m/s");
        distanceView.setText("Distance: " + distance + " km");
        int min = (int) Double.parseDouble(time) / 60;
        int sec = (int) Double.parseDouble(time) - min * 60;
        timeView.setText(String.format("Time: %d min %d sec", min, sec));
        caloriesView.setText("Calories: " + calories + " cal");

        if(!image.equals("")) {
            byte[] bytes_new = Base64.getDecoder().decode(image);
            Bitmap photo = BitmapFactory.decodeByteArray(bytes_new, 0, bytes_new.length);
            imageView.setImageBitmap(photo);
        }
    }

    public void goBack(View view){
        Intent intent = new Intent(this, MainPage.class);
        intent.putExtra("fragment", "checkin");
        startActivity(intent);
    }
}