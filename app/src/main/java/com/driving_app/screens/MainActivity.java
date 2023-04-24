package com.driving_app.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.driving_app.R;
import com.driving_app.fragments.AppointmentsFragment;
import com.driving_app.fragments.HomeFragment;
import com.driving_app.fragments.NearbyFragment;
import com.driving_app.fragments.SettingsFragment;
import com.driving_app.helpers.PreferenceHelper;
import com.driving_app.model.Instructor;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private static final int DRIVER_DETAIL_REQUEST_CODE = 9001;
    private static final int APPOINTMENT_ADDED_REQUEST_CODE = 9002;
    private Instructor selectedInstructor = null;
    Fragment selectedFragment = null;
    private AdView mAdView;
    private ImageView hamMenu;
    private DrawerLayout drawerLayout;
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        int id = item.getItemId();
        if(id == R.id.homeIcon){
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setHomeListener(new HomeFragment.HomeListener() {
                @Override
                public void onLogoutClicked() {
                    PreferenceHelper.getInstance(MainActivity.this).setIsLoggedIn(false);
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
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
            selectedFragment = homeFragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, selectedFragment).commitAllowingStateLoss();

        }

        else if(id == R.id.messageIcon){
            selectedFragment = new AppointmentsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, selectedFragment).commitAllowingStateLoss();

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
            }else if(requestCode == APPOINTMENT_ADDED_REQUEST_CODE){
                if(bottomNavigationView != null){
                    bottomNavigationView.setSelectedItemId(R.id.upcomingAppointments);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    private void startNewAppointment(Instructor instructor){
        Intent appointmentIntent = new Intent(this, NewAppointmentActivity.class);
        appointmentIntent.putExtra(NewAppointmentActivity.NEW_APPOINTMENT_KEY, instructor);
        startActivityForResult(appointmentIntent, APPOINTMENT_ADDED_REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        hamMenu = findViewById(R.id.hamMenu);
        hamMenu.setOnClickListener(this);
        MobileAds.initialize(this, initializationStatus -> {

        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        PreferenceHelper.getInstance(this).setIsLoggedIn(true);

        frameLayout = findViewById(R.id.frameContainer);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        bottomNavigationView.setSelectedItemId(R.id.homeIcon);

    }

    @Override
    public void onClick(View view) {
        if(view == hamMenu){

        }
    }
}
