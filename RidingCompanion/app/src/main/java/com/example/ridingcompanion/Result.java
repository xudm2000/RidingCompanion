package com.example.ridingcompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    public void checkin(View view){
        Intent intent = new Intent(this, MainPage.class);
        intent.putExtra("fragment", "checkin");
        startActivity(intent);
    }
}