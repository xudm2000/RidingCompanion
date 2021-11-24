package com.example.ridingcompanion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Base64;

public class CheckinFragment extends Fragment {

    private ImageView tempImage;
    public ArrayList<History> histories;

    public CheckinFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkin, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.example.ridingcompanion", Context.MODE_PRIVATE);
        String current_username = sharedPreferences.getString("current", "");

        Context context = getActivity().getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("users", Context.MODE_PRIVATE, null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        histories = dbHelper.getUserHistory(current_username);

        ArrayList<String> displayHistories = new ArrayList<>();

        for(int i=histories.size()-1;i>=0;i--){
            History history = histories.get(i);
            displayHistories.add(String.format("Date:%s   |   Calories:%s", history.getDate(), history.getCalories()));
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, displayHistories);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), HistoryPage.class);
                History target_history = histories.get(histories.size()-position-1);
                intent.putExtra("date", target_history.getDate());
                intent.putExtra("speed", target_history.getAvg_speed());
                intent.putExtra("distance", target_history.getDistance());
                intent.putExtra("time", target_history.getTime());
                intent.putExtra("calories", target_history.getCalories());
                intent.putExtra("image", target_history.getImage());
                startActivity(intent);
            }
        });
        return view;
    }
}