package com.driving_app.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.driving_app.BuildConfig;
import com.driving_app.R;
import com.driving_app.model.Instructor;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DriverDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageURL, backIcon;
    private TextView driverName;
    private TextView driverDetails,driverSummary;
    private ImageView callIcon, shareIcon;
    public static final String DRIVER_DETAIL_KEY = "DRIVER_DETAIL_KEY";
    private Instructor instructor;
    private RatingBar ratingButton;
    private Button bookAppointmentButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail);
        driverSummary = findViewById(R.id.driverSummary);

        ratingButton = findViewById(R.id.ratingBar);
        bookAppointmentButton = findViewById(R.id.bookAppointmentButton);
        bookAppointmentButton.setOnClickListener(this);

        instructor = getIntent().getParcelableExtra(DRIVER_DETAIL_KEY);
        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(this);
        if(instructor == null){
            finish();
            return;
        }

        imageURL = findViewById(R.id.profileURL);
        driverName = findViewById(R.id.driverName);
        driverDetails = findViewById(R.id.driverDetails);
        driverName.setText(instructor.getName());
        driverDetails.setText(instructor.getDrivingExperienceDetails());
        driverSummary.setText(instructor.getSummary());

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(instructor.getProfileUrl());
        Glide.with(this)
                .load(storageReference)
                .centerCrop()
                .into(imageURL);


        callIcon = findViewById(R.id.callIcon);
        shareIcon = findViewById(R.id.shareIcon);
        callIcon.setOnClickListener(this);
        shareIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == callIcon){
            callIntent();
        }else if(view == shareIcon){
            shareIntent();
        }else if(view == backIcon){
            finish();
        }else if(view == bookAppointmentButton){
            setResult(RESULT_OK);
            finish();
        }
    }

    private void shareIntent(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out Driving app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void callIntent() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0987654321"));
        startActivity(intent);
    }
}
