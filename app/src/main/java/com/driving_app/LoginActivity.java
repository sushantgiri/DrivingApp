package com.driving_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText userNameText;
    private TextInputEditText passwordText;
    private AppCompatButton driverButton, instructorButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        driverButton = findViewById(R.id.driverButton);
        instructorButton = findViewById(R.id.instructorButton);
        driverButton.setOnClickListener(this);
        instructorButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        startActivity(view == driverButton ? 0 : 1);
    }

    private void validateFields(){
        String username = userNameText.getText().toString();
        String password = passwordText.getText().toString();

    }

    private void startActivity(int flag){
        Intent decidingIntent;

        if(flag == 0){
            decidingIntent = new Intent(this, DrivingActivity.class);
        }else {
            decidingIntent = new Intent(this, InstructorActivity.class);
        }
       startActivity(decidingIntent);
    }
}
