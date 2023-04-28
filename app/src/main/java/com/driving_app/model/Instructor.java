package com.driving_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Instructor implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Instructor createFromParcel(Parcel in) {
            return new Instructor(in);
        }

        public Instructor[] newArray(int size) {
            return new Instructor[size];
        }
    };

    private String name;
    private String rating;
    private String drivingExperienceDetails;
    private String summary;
    private String profileUrl;

    private String instructorAvailability;
    private ArrayList<Appointments> appointmentList;

    public Instructor(){

    }

    public Instructor(Parcel in){
        this.name = in.readString();
        this.rating = in.readString();
        this.drivingExperienceDetails = in.readString();
        this.profileUrl = in.readString();
        this.instructorAvailability = in.readString();
        in.createTypedArrayList(Appointments.CREATOR);

//        this.appointmentList = in.readArrayListx(null);
        this.summary = in.readString();
    }

    public String getInstructorAvailability() {
        return instructorAvailability;
    }

    public void setInstructorAvailability(String instructorAvailability) {
        this.instructorAvailability = instructorAvailability;
    }

    public ArrayList<Appointments> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(ArrayList<Appointments> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDrivingExperienceDetails() {
        return drivingExperienceDetails;
    }

    public void setDrivingExperienceDetails(String drivingExperienceDetails) {
        this.drivingExperienceDetails = drivingExperienceDetails;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeString(this.name);
        dest.writeString(this.rating);
        dest.writeString(this.drivingExperienceDetails);
        dest.writeString(this.profileUrl);
        dest.writeString(this.instructorAvailability);
        dest.writeTypedList(this.appointmentList);
        dest.writeString(this.summary);
    }


}