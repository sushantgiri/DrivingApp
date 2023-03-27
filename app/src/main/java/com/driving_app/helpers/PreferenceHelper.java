package com.driving_app.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    private static final String PREFS_NAME = "DRIVING_APP";

    private static final String IS_LOGGED_IN = "IS_LOGGED_IN";

    private Context context;
    private static PreferenceHelper preferenceHelper;
    private SharedPreferences sharedPreferences;

    private PreferenceHelper(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static final PreferenceHelper getInstance(Context context){
        if(preferenceHelper == null){
            preferenceHelper = new PreferenceHelper(context);
        }
        return preferenceHelper;
    }

    public void setIsLoggedIn(boolean isLoggedIn){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

}
