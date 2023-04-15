package com.driving_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Appointments implements Parcelable {

    private String userId;
    private String userName;
    private String userEmail;
    private String timeStamp;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Appointments createFromParcel(Parcel in) {
            return new Appointments(in);
        }

        public Appointments[] newArray(int size) {
            return new Appointments[size];
        }
    };

    public Appointments(){

    }

    public Appointments(Parcel in){
        this.userId = in.readString();
        this.userName = in.readString();
        this.userEmail = in.readString();
        this.timeStamp = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.userEmail);
        dest.writeString(this.timeStamp);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}