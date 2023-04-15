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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.driving_app.R;
import com.driving_app.model.Appointments;
import com.driving_app.model.Instructor;
import com.driving_app.utils.BookingUtils;
import com.driving_app.utils.DateUtils;
import com.driving_app.utils.MessageUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Calendar;

public class NewAppointmentActivity extends AppCompatActivity implements View.OnClickListener, CalendarView.OnDateChangeListener {

    private ImageView backIcon;
    private CalendarView calendarView;
    private TextView amText, pmText;
    private Button timeButton,addAppointmentButton;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public static final String NEW_APPOINTMENT_KEY = "NEW_APPOINTMENT_KEY";

    private ProgressDialog progressDialog;
    private Instructor instructor;
    private Calendar selectedCalendarDay = null;
    private Calendar selectedCalendarTime = null;
    private ArrayList<Appointments> appointmentsArrayList;
//    private int selectedHourOfDay, selectedMinute = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        instructor = getIntent().getParcelableExtra(NEW_APPOINTMENT_KEY);
        if(instructor == null){
            finish();
            return;
        }
        appointmentsArrayList = instructor.getAppointmentList();
        selectedCalendarDay = Calendar.getInstance();

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

        calendarView.setDate(DateUtils.getCurrentDate());
        calendarView.setOnDateChangeListener(this);
        Calendar now = Calendar.getInstance();
        timeButton.setText(DateUtils.getCurrentTime());

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
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        addAppointment(task.getResult());

                    });

        }else if(view == amText){
            setAM();
        }else if(view == pmText){
            setPM();
        }
    }

    private void addAppointment(String deviceToken){
        progressDialog.show();
        String selectedDate= DateUtils.getDate(selectedCalendarDay, selectedCalendarTime);
        Appointments appointments = BookingUtils.createAppointments(selectedDate,deviceToken);
        if(appointmentsArrayList != null){
            Appointments availableAppointments = BookingUtils.isBookingAvailable(appointmentsArrayList);
            appointmentsArrayList.remove(availableAppointments);
        }else{
            appointmentsArrayList = new ArrayList<>();
        }

        if(BookingUtils.containsDate(selectedDate, appointmentsArrayList)){
            MessageUtils.showMessage(this, "Please select different time. Instructor seems busy at this moment.");
        }else{
            appointmentsArrayList.add(appointments);
            instructor.setAppointmentList(appointmentsArrayList);
            BookingUtils.bookUser(instructor, task -> {
                progressDialog.cancel();
                if(task.isSuccessful()){
                    MessageUtils.showMessage(this, "Appointment has been added");
                    BookingUtils.initiateMessaging(instructor.getName(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            deviceToken,selectedDate, task1 -> {
                          if(!task1.isSuccessful()){
                              MessageUtils.showMessage(NewAppointmentActivity.this, task1.getException().getMessage());
                          }else{
                              setResult(RESULT_OK);
                              finish();
                          }
                    });

                }else{
                    MessageUtils.showMessage(NewAppointmentActivity.this, task.getException().getMessage());

                }
            });
        }




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
//                            selectedTime = DateUtils.getCalendarDate(hourOfDay,minute);
                                    selectedCalendarTime=DateUtils.getCalendar(hourOfDay,minute);
                                    if (selectedCalendarTime.get(Calendar.AM_PM) == Calendar.AM)
                                        setAM();
                                    else if (selectedCalendarTime.get(Calendar.AM_PM) == Calendar.PM)
                                       setPM();


                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        selectedCalendarDay = DateUtils.getCalendarFromSelectedDay(year, month, dayOfMonth);


//        selectedDate = DateUtils.getCalendarFromSelectedDay(year, month)
    }
}
