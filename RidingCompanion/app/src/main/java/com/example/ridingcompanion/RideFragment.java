package com.example.ridingcompanion;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RideFragment extends Fragment {

    private Button button;

    public RideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_ride, container, false);

        button = (Button) fragmentView.findViewById(R.id.startRiding);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRiding();
            }
        });
        return fragmentView;
    }

    public void startRiding(){
        Intent intent = new Intent(this.getActivity(), Riding.class);
        startActivity(intent);
    }
}