package com.example.ridingcompanion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class InfoFragment extends Fragment {

    private Button button;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_info, container, false);
        button = (Button) fragmentView.findViewById(R.id.logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
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
}