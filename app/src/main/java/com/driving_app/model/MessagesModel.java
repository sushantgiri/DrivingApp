package com.driving_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class MessagesModel implements Parcelable {
    private String title, message;
    private String deviceId;
    private String userId;
    private String timeStamp;

    public MessagesModel(){

    }

    public MessagesModel(Parcel in) {
        title = in.readString();
        message = in.readString();
        deviceId = in.readString();
        userId = in.readString();
        timeStamp = in.readString();
    }

    public static final Creator<MessagesModel> CREATOR = new Creator<MessagesModel>() {
        @Override
        public MessagesModel createFromParcel(Parcel in) {
            return new MessagesModel(in);
        }

        @Override
        public MessagesModel[] newArray(int size) {
            return new MessagesModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(message);
        dest.writeString(deviceId);
        dest.writeString(userId);
        dest.writeString(timeStamp);
    }
}
