package com.example.ridingcompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Riding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding);
    }

    public void moreInfo(View view){
        Intent intent = new Intent(this, RidingInformation.class);
        startActivity(intent);
    }

    public void stop(View view){
        Intent intent = new Intent(this, Result.class);
        startActivity(intent);
    }
}