package com.example.ridingcompanion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

public class InfoFragment extends Fragment {

    private TextView username;
    private EditText password;
    private EditText confirm_password;
    private EditText email;
    private EditText phone;
    private EditText weight;
    private EditText height;
    private TextView birthday;
    private TextView error_msg;
    private RadioButton male;
    private RadioButton female;
    private Button logout;
    private Button saveButton;
    private Switch loginSwitch;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_info, container, false);
        logout = (Button) fragmentView.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        saveButton = (Button) fragmentView.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.example.ridingcompanion", Context.MODE_PRIVATE);
        username = (TextView) fragmentView.findViewById(R.id.username_show);
        password = (EditText) fragmentView.findViewById(R.id.editTextPassword);
        confirm_password = (EditText) fragmentView.findViewById(R.id.editTextConfirmPassword);
        email = (EditText) fragmentView.findViewById(R.id.editTextEmail);
        phone = (EditText) fragmentView.findViewById(R.id.editTextPhone);
        weight = (EditText) fragmentView.findViewById(R.id.editWeight);
        height = (EditText) fragmentView.findViewById(R.id.editHeight);
        birthday = (TextView) fragmentView.findViewById(R.id.birthday_show);
        male = (RadioButton) fragmentView.findViewById(R.id.radioButton);
        female = (RadioButton) fragmentView.findViewById(R.id.radioButton2);
        error_msg = (TextView) fragmentView.findViewById(R.id.error_msg);
        loginSwitch = (Switch) fragmentView.findViewById(R.id.auto_login);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                female.setChecked(false);
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setChecked(false);
            }
        });

        String current_username = sharedPreferences.getString("current", "");
        String login_username = sharedPreferences.getString("login", "");
        if(!login_username.equals("__no user__") && !login_username.equals("")){
            loginSwitch.setChecked(true);
        }else{
            loginSwitch.setChecked(false);
        }

        loginSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(loginSwitch.isChecked()){
                    sharedPreferences.edit().putString("login", current_username).apply();
                }else{
                    sharedPreferences.edit().putString("login", "__no user__").apply();
                }
            }
        });

        username.setText(current_username);
        Context context = getActivity().getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("users", Context.MODE_PRIVATE, null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        User user = dbHelper.getUser(current_username);
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        birthday.setText(user.getDob());
        weight.setText(user.getWeight());
        height.setText(user.getHeight());
        male.setChecked(false);
        female.setChecked(false);
        if (user.getGender().equals("male")) {
            male.setChecked(true);
        } else {
            female.setChecked(true);
        }

        return fragmentView;
    }

    public void logout() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("com.example.ridingcompanion", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("current", "__no user__").apply();
        sharedPreferences.edit().putString("login", "__no user__").apply();
        gotoLoginPage();
    }

    public void gotoLoginPage() {
        Intent intent = new Intent(this.getActivity(), MainActivity.class);
        startActivity(intent);
    }

    public void save() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("com.example.ridingcompanion", Context.MODE_PRIVATE);
        String pw = password.getText().toString();
        String con_pw = confirm_password.getText().toString();
        String um = sharedPreferences.getString("current", "");

        if(!pw.equals("")){
            if (pw.equals(con_pw)) {
                sharedPreferences.edit().putString(um, pw).apply();
            } else {
                error_msg.setText("passwords are not same!");
            }
        }else{
            error_msg.setText("passwords cannot be empty!");
        }

        Context context = getActivity().getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("users", Context.MODE_PRIVATE, null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        String gender = "";
        if (male.isChecked()) {
            gender = "male";
        } else {
            gender = "female";
        }
        if (email.getText().toString().equals("") || phone.getText().toString().equals("") || birthday.getText().toString().equals("") || weight.getText().toString().equals("") || weight.getText().toString().equals("")) {
            error_msg.setText("Field(s) cannot be empty!");
            return;
        }
        dbHelper.updateUser(um, email.getText().toString(), phone.getText().toString(), birthday.getText().toString(), weight.getText().toString(), height.getText().toString(), gender);
        saveButton.setText("Saved");
        saveButton.setEnabled(false);
    }
}