package com.driving_app.screens;

import android.app.ProgressDialog;
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
import com.driving_app.utils.DateUtils;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class NewAppointmentActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backIcon;
    private CalendarView calendarView;
    private TextView amText, pmText;
    private Button timeButton,addAppointmentButton;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public static final String NEW_APPOINTMENT_KEY = "NEW_APPOINTMENT_KEY";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fixing New Appointment");

        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(this);


        timeButton = findViewById(R.id.timeButton);
        addAppointmentButton = findViewById(R.id.addAppointment);
        addAppointmentButton.setOnClickListener(this);
        amText = findViewById(R.id.amText);
        amText.setOnClickListener(this);
        pmText = findViewById(R.id.pmText);
        pmText.setOnClickListener(this);
        timeButton.setOnClickListener(this);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setMinDate(DateUtils.getCurrentDate());
        calendarView.setDate(new Date().getTime());
        Calendar now = Calendar.getInstance();
        timeButton.setText(""+now.get(Calendar.HOUR)+":"+now.get(Calendar.MINUTE));

        if(now.get(Calendar.AM_PM) == Calendar.AM){
            setAM();
        }else{
            setPM();
        }
    }

    private void setAM(){
        amText.setTextColor(Color.BLACK);
        pmText.setTextColor(Color.GRAY);
    }
    private void setPM(){
        amText.setTextColor(Color.GRAY);
        pmText.setTextColor(Color.BLACK);
    }



    @Override
    public void onClick(View view) {
        if(view == backIcon){
            finish();
        }else if(view == timeButton){
            openTimePicker();
        }else if(view == addAppointmentButton){
            addAppointment();
        }else if(view == amText){
            setAM();
        }else if(view == pmText){
            setPM();
        }
    }

    private void addAppointment(){
        progressDialog.show();


    }

    private void openTimePicker(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                            timeButton.setText(hourOfDay + ":" + minute);
                                    Calendar datetime = Calendar.getInstance();
                                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    datetime.set(Calendar.MINUTE, minute);

                                    if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                                        setAM();
                                    else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                                       setPM();


                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}
