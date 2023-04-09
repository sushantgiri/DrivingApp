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
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity  {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        Fragment selectedFragment = null;
        int id = item.getItemId();
        if(id == R.id.homeMenu){
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setHomeListener(() -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
            selectedFragment= homeFragment;

        }
//        else if(id == R.id.nearBy){
//            selectedFragment = new NearbyFragment();
//        }

        else if(id == R.id.calendar){
            selectedFragment = new AppointmentsFragment();

        }

//        else if(id == R.id.settingsIcon){
//            selectedFragment = new SettingsFragment();
//        }
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, selectedFragment).commit();
        }

        return false;
    };

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
