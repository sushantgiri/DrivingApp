package com.driving_app.screens;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.driving_app.R;

import java.util.Calendar;
import java.util.Date;

public class NewAppointmentActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backIcon;
    private CalendarView calendarView;
    private TextView amText, pmText;
    private Button timeButton,addAppointmentButton;
    private int mYear, mMonth, mDay, mHour, mMinute;
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
        timeButton.setText(""+now.get(Calendar.HOUR));

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
            openTimePicker();
        }else if(view == addAppointmentButton){

        }
    }

    private void openTimePicker(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> timeButton.setText(hourOfDay + ":" + minute), mHour, mMinute, false);
        timePickerDialog.show();
    }
}
