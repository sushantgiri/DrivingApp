package com.driving_app.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.driving_app.R;
import com.driving_app.fragments.AppointmentsFragment;
import com.driving_app.fragments.HomeFragment;
import com.driving_app.fragments.NearbyFragment;
import com.driving_app.fragments.SettingsFragment;
import com.driving_app.model.Instructor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity  {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private static final int DRIVER_DETAIL_REQUEST_CODE = 9001;
    private Instructor selectedInstructor = null;

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        Fragment selectedFragment = null;
        int id = item.getItemId();
        if(id == R.id.homeMenu){
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setHomeListener(new HomeFragment.HomeListener() {
                @Override
                public void onLogoutClicked() {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }

                @Override
                public void onItemClicked(int position, Instructor instructor) {
                    startDriverDetailActivity(position, instructor);
                }

                @Override
                public void onBookingAppointmentClicked(int position, Instructor instructor) {
                    startNewAppointment(instructor);
                }
            });
            selectedFragment= homeFragment;

        }

        else if(id == R.id.calendar){
            selectedFragment = new AppointmentsFragment();

        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, selectedFragment).commit();
        }

        return false;
    };

    private void startDriverDetailActivity(int position, Instructor model){
        selectedInstructor = model;
        Intent driverDetailIntent = new Intent(this, DriverDetailActivity.class);
        driverDetailIntent.putExtra(DriverDetailActivity.DRIVER_DETAIL_KEY, model);
        startActivityForResult(driverDetailIntent, DRIVER_DETAIL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == DRIVER_DETAIL_REQUEST_CODE){
                startNewAppointment(selectedInstructor);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startNewAppointment(Instructor instructor){
        Intent appointmentIntent = new Intent(this, NewAppointmentActivity.class);
        appointmentIntent.putExtra(NewAppointmentActivity.NEW_APPOINTMENT_KEY, instructor);
        startActivity(appointmentIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.frameContainer);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        bottomNavigationView.setSelectedItemId(R.id.homeMenu);

    }

}
