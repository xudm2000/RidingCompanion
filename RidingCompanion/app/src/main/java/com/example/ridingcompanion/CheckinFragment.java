package com.example.ridingcompanion;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;

public class CheckinFragment extends Fragment {

    private ImageView tempImage;
    public ArrayList<History> histories;
    public ArrayList<String> displayHistories;
    public ArrayAdapter arrayAdapter;
    private TextView dateSelect;
    public ListView listView;

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
        listView = (ListView) view.findViewById(R.id.listView);

        refreshList(view, histories);

        dateSelect = (TextView) view.findViewById(R.id.dateSelect);
        dateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dateDialog= new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateChosen = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                        dateSelect.setText(dateChosen);

                        ArrayList<History> temp_histories = new ArrayList<>();
                        for(History history : histories){
                            String date = history.getDate().split(" ")[0];
                            if(date.equals(dateChosen)){
                                temp_histories.add(history);
                            }
                        }
                        refreshList(view, temp_histories);
                    }
                }, mYear, mMonth, mDay);
                dateDialog.show();
            }
        });
        return view;
    }

    public void refreshList(View view, ArrayList<History> temp_histories){
        displayHistories = new ArrayList<>();

        for(int i=temp_histories.size()-1;i>=0;i--){
            History history = temp_histories.get(i);
            displayHistories.add(String.format("\nDate: %s  \n\n@Calories: %.2f cal\n", history.getDate(), Double.parseDouble(history.getCalories())));
        }

        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, displayHistories);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), HistoryPage.class);
                History target_history = temp_histories.get(temp_histories.size()-position-1);
                intent.putExtra("date", target_history.getDate());
                intent.putExtra("speed", target_history.getAvg_speed());
                intent.putExtra("distance", target_history.getDistance());
                intent.putExtra("time", target_history.getTime());
                intent.putExtra("calories", target_history.getCalories());
                intent.putExtra("image", target_history.getImage());
                startActivity(intent);
            }
        });
    }
}