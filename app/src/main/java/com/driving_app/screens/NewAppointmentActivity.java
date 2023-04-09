package com.driving_app.screens;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.driving_app.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewAppointmentActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backIcon;
    private CalendarView calendarView;
    private TextView amText, pmText;
    private Button timeButton,addAppointmentButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(this);


        timeButton = findViewById(R.id.timeButton);
        addAppointmentButton = findViewById(R.id.addAppointment);
        addAppointmentButton.setOnClickListener(this);
        amText = findViewById(R.id.amText);
        pmText = findViewById(R.id.pmText);
        timeButton.setOnClickListener(this);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setDate(new Date().getTime());
        Calendar now = Calendar.getInstance();
        timeButton.setText(now.get(Calendar.HOUR));

        if(now.get(Calendar.AM_PM) == Calendar.AM){
            amText.setTextColor(Color.BLACK);
            pmText.setTextColor(Color.GRAY);
        }else{
            amText.setTextColor(Color.GRAY);
            pmText.setTextColor(Color.BLACK);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == backIcon){
            finish();
        }else if(view == timeButton){


        }else if(view == addAppointmentButton){

        }
    }

    private void openTimePicker(){

    }
}
