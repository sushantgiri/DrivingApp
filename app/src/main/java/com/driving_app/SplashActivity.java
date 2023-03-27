package com.driving_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.driving_app.helpers.PreferenceHelper;

public class SplashActivity extends AppCompatActivity {

    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferenceHelper = PreferenceHelper.getInstance(this);


        final Animation animLinear = AnimationUtils.loadAnimation(this, R.anim.slide_animation);
        ImageView logoIcon = findViewById(R.id.drivingIcon);
        logoIcon.startAnimation(animLinear);

        startScreenAnimation();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void startMainActivity(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    private void startNewActivity(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    private void launchActivity(){
        Intent decidingIntent;
        if(preferenceHelper.isLoggedIn()){
            decidingIntent = new Intent(this, MainActivity.class);
        }else{
            decidingIntent = new Intent(this,LoginActivity.class);
        }
       startActivity(decidingIntent);
        finish();
    }

    private void startScreenAnimation() {
        new Handler().postDelayed(this::launchActivity, 2000);
    }


}
