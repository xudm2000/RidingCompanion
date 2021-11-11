package com.example.ridingcompanion;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RankFragment extends Fragment {

    private FirebaseFirestore db;
    private Button button;

    public RankFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragmentView = inflater.inflate(R.layout.fragment_rank, container, false);

        button = (Button) fragmentView.findViewById(R.id.refresh);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        ArrayList<String> displayUsers = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.example.ridingcompanion", Context.MODE_PRIVATE);
        String temp = sharedPreferences.getString("num", "");
        String[] users = temp.split("\\|");
        int count = 0;
        for(String user : users){
            if(count == 5){
                break;
            }
            if(!user.equals("")) {
                String[] info = user.split(",");
                String um = info[0];
                String nd = info[1];
                displayUsers.add(String.format("Username:%s\nNumber of Days:%s", um, nd));
                count++;
            }
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, displayUsers);
        ListView listView = (ListView) fragmentView.findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
        return fragmentView;
    }

    public void refresh(){
        db = FirebaseFirestore.getInstance();
//        db.collection("users").document("xudm2000").set(new RankUser("xudm2000", 1));

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.example.ridingcompanion", Context.MODE_PRIVATE);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String temp = sharedPreferences.getString("num", "");
                                sharedPreferences.edit().putString("num", temp + "|" + document.getString("username") + "," + document.getLong("numOfDay").toString()).apply();
                            }
                        }else{

                        }
                    }
                });
    }
}