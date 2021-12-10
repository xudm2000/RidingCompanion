package com.example.ridingcompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.ridingcompanion", Context.MODE_PRIVATE);

        if(!sharedPreferences.getString("login", "__no user__").equals("__no user__")){
            Intent intent =new Intent(this, MainPage.class);
            startActivity(intent);
        }else {
            setContentView(R.layout.activity_main);
        }
    }

    public void login(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.ridingcompanion", Context.MODE_PRIVATE);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        String um = username.getText().toString();
        String pw = password.getText().toString();
        if(sharedPreferences.getString(um, "__wrong password").equals(pw)) {
            sharedPreferences.edit().putString("current", um).apply();
            sharedPreferences.edit().putString("login", "__no user__").apply();
            gotoMainPage(um);
        }else{
            textView = (TextView) findViewById(R.id.textView);
            textView.setText("Wrong username or password!");
        }
    }

    public void registration(View view){
        gotoRegistrationPage();
    }

    public void gotoMainPage(String s){
        Intent intent =new Intent(this, MainPage.class);
        intent.putExtra("username", s);
        startActivity(intent);
    }

    public void gotoRegistrationPage(){
        Intent intent =new Intent(this, Registration.class);
        startActivity(intent);
    }
}