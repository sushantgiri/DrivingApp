package com.driving_app.screens;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.driving_app.R;
import com.driving_app.fragments.driver.DriverAppointmentFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DrivingActivity extends AppCompatActivity  {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    Fragment selectedFragment = null;

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        int id = item.getItemId();
        if(id == R.id.pendingAppointments){
            DriverAppointmentFragment fragment = DriverAppointmentFragment.newInstance(1);
            fragment.setDriverAppointmentListener(() -> {
                bottomNavigationView.setSelectedItemId(R.id.upcomingAppointments);

            });
            selectedFragment = fragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, selectedFragment).commitAllowingStateLoss();

        }

        else if(id == R.id.upcomingAppointments){
            DriverAppointmentFragment fragment = DriverAppointmentFragment.newInstance(2);
            selectedFragment = fragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, selectedFragment).commitAllowingStateLoss();

        }



        return false;
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);

        frameLayout = findViewById(R.id.frameContainer);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.pendingAppointments);


    }



}
