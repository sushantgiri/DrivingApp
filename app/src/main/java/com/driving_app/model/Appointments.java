package com.driving_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Appointments implements Parcelable {

    private String userId;
    private String userName;
    private String userEmail;
    private String timeStamp;
    private String deviceId;
    private boolean isBookingAccepted = false;

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
        this.deviceId = in.readString();
        this.isBookingAccepted = in.readInt() == 1;

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
        dest.writeInt(this.isBookingAccepted ? 1 : 0);
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isBookingAccepted() {
        return isBookingAccepted;
    }

    public void setBookingAccepted(boolean bookingAccepted) {
        isBookingAccepted = bookingAccepted;
    }
}
