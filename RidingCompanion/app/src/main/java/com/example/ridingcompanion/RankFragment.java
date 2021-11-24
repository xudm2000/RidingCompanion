package com.example.ridingcompanion;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

        Context context = getActivity().getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("users", Context.MODE_PRIVATE,null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        ArrayList<String> displayUsers = new ArrayList<>();
        ArrayList<RankUser> rankUsers = dbHelper.top5Users();
        for(RankUser user : rankUsers){
            if(!user.equals("")) {
                displayUsers.add(String.format("Username:   %s\nNumber of Days:   %s", user.username, user.numOfDay));
            }
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, displayUsers);
        ListView listView = (ListView) fragmentView.findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
        return fragmentView;
    }

    public void refresh(){
        db = FirebaseFirestore.getInstance();
//        db.collection("users").document("user").set(new RankUser("xudm2000", 1));

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
//                            Context context = getActivity().getApplicationContext();
                            Context context = getContext();
                            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("users", Context.MODE_PRIVATE,null);
                            DBHelper dbHelper = new DBHelper(sqLiteDatabase);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dbHelper.addRankUser(document.getString("username"), document.getLong("numOfDay").intValue());
                            }
                        }
                    }
                });
        Intent intent = new Intent(getActivity(), MainPage.class);
        intent.putExtra("fragment", "rank");
        startActivity(intent);
    }
}