package com.driving_app.utils;

import java.util.Date;

public final class DateUtils {

    private DateUtils(){

    }

    public static long getCurrentDate(){
        return new Date().getTime();
    }
}
