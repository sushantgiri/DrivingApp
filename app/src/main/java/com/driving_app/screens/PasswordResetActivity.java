package com.driving_app.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.driving_app.R;

public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == backIcon){
            finish();
        }
    }
}
