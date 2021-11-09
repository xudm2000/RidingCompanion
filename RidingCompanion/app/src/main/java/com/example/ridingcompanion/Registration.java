package com.example.ridingcompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Registration extends AppCompatActivity {

    private EditText username;
    private EditText phone;
    private EditText email;
    private EditText birthday;
    private EditText password;
    private EditText confirm_password;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        username = (EditText) findViewById(R.id.reg_username);
        phone = (EditText) findViewById(R.id.reg_phone);
        email = (EditText) findViewById(R.id.reg_email);
        birthday = (EditText) findViewById(R.id.reg_birthday);
        password = (EditText) findViewById(R.id.reg_password);
        confirm_password = (EditText) findViewById(R.id.reg_confirm);
        textView = (TextView) findViewById(R.id.textView2);
    }

    public void register(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.ridingcompanion", Context.MODE_PRIVATE);
        String um = username.getText().toString();
        String pw = password.getText().toString();
        String con_pw = confirm_password.getText().toString();
        if(sharedPreferences.getString(um, "").equals("")){
            if(pw.equals(con_pw)){
                sharedPreferences.edit().putString("current", um).apply();
                sharedPreferences.edit().putString(um, pw).apply();
                gotoMainPage(um);
            }else{
                textView.setText("passwords are not same!");
            }
        }else{
            textView.setText("Username is not available!");
        }
    }

    public void gotoMainPage(String s){
        Intent intent =new Intent(this, MainPage.class);
        intent.putExtra("username", s);
        startActivity(intent);
    }
}