package com.example.ridingcompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class Registration extends AppCompatActivity {

    private EditText username;
    private EditText phone;
    private EditText email;
    private TextView birthday;
    private Button dobButton;
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
        birthday = (TextView) findViewById(R.id.reg_birthday);
        dobButton = findViewById(R.id.button);
        dobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dateDialog= new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateChoosed = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                        birthday.setText(dateChoosed);
                    }
                }, mYear, mMonth, mDay);
                dateDialog.show();
            }
        });

        password = (EditText) findViewById(R.id.reg_password);
        confirm_password = (EditText) findViewById(R.id.reg_confirm);
        textView = (TextView) findViewById(R.id.error_message);
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

                Context context = getApplicationContext();
                SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("users", Context.MODE_PRIVATE,null);
                DBHelper dbHelper = new DBHelper(sqLiteDatabase);
                dbHelper.addUser(um, email.getText().toString(), phone.getText().toString(), birthday.getText().toString());
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