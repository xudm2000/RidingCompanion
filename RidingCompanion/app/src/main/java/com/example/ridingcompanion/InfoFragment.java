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
import android.widget.EditText;
import android.widget.TextView;

public class InfoFragment extends Fragment {

    private TextView username;
    private EditText password;
    private EditText confirm_password;
    private EditText email;
    private EditText phone;
    private TextView birthday;
    private Button logout;
    private Button saveButton;

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
        birthday = (TextView) fragmentView.findViewById(R.id.birthday_show);

        String current_username = sharedPreferences.getString("current", "");
        username.setText(current_username);

        Context context = getActivity().getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("users", Context.MODE_PRIVATE,null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        User user = dbHelper.getUser(current_username);
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        birthday.setText(user.getDob());

        return fragmentView;
    }

    public void logout(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("com.example.ridingcompanion", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("current", "__no user__").apply();
        gotoLoginPage();
    }

    public void gotoLoginPage(){
        Intent intent = new Intent(this.getActivity(), MainActivity.class);
        startActivity(intent);
    }

    public void save(){

    }
}