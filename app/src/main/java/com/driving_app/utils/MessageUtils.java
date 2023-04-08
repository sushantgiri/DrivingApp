package com.driving_app.utils;

import android.content.Context;
import android.widget.Toast;

public class MessageUtils {

    public static void showMessage(Context context, String message){
        Toast.makeText(context, message,Toast.LENGTH_SHORT).show();
    }
}
