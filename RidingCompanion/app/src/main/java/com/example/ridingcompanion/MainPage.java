package com.example.ridingcompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

public class MainPage extends AppCompatActivity {

    private NavigationBarView buttonNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        buttonNavigationView = findViewById(R.id.bottomnav);
        buttonNavigationView.setOnItemSelectedListener(buttomnavFunction);

        String intentString = getIntent().getStringExtra("fragment");

        if(intentString == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new RideFragment()).commit();
        }else if(intentString.equals("checkin")){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new CheckinFragment()).commit();
            buttonNavigationView.getMenu().getItem(1).setChecked(true);
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new RideFragment()).commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private NavigationBarView.OnItemSelectedListener buttomnavFunction = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Fragment fragment = null;
            switch(item.getItemId()){
                case R.id.ride:
                    fragment = new RideFragment();
                    break;
                case R.id.checkin:
                    fragment = new CheckinFragment();
                    break;
                case R.id.rank:
                    fragment = new RankFragment();
                    break;
                case R.id.info:
                    fragment = new InfoFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            return true;
        }
    };
}