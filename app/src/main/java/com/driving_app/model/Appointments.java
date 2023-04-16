package com.driving_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Appointments implements Parcelable {

    @SerializedName("userId")
    private String userId;
    @SerializedName("userName")
    private String userName;
    @SerializedName("userEmail")
    private String userEmail;
    @SerializedName("timeStamp")
    private String timeStamp;
    @SerializedName("deviceId")
    private String deviceId;
    @SerializedName("isBookingAccepted")
    private String isBookingAccepted = "false";

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
        this.isBookingAccepted = in.readString();

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
        dest.writeString(this.isBookingAccepted);
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

    public String isBookingAccepted() {
        return isBookingAccepted;
    }

    public void setBookingAccepted(String bookingAccepted) {
        isBookingAccepted = bookingAccepted;
    }
}
