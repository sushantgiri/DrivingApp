package com.driving_app.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

public class ValidatorUtils {

    public static boolean isValidEmail(CharSequence target) {
        return (Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean validateSignUpFields(Context context, String username, String password){

        if(TextUtils.isEmpty(username)){
            MessageUtils.showMessage(context, "Email cannot be left empty.");
            return false;
        }
        if(TextUtils.isEmpty(password)){
            MessageUtils.showMessage(context, "Password cannot be left empty.");
            return false;
        }

        if(!ValidatorUtils.isValidEmail(username)){
            MessageUtils.showMessage(context, "Please enter valid email");
            return false;
        }
        return true;

    }


}
