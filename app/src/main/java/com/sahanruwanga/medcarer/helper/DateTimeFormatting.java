package com.sahanruwanga.medcarer.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sahan Ruwanga on 5/6/2018.
 */

public class DateTimeFormatting {

    // Get datetime format for showing (AM/PM)
    public String getDateTimeToShowInUI(String dateTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
        return fmtOut.format(date);
    }

    // Get datetime format for saving in DB (AM/PM)
    public String getDateTimeToSaveInDB(String dateTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmtOut.format(date);
    }

    // Get date format for saving in DB
    public String getDateToSaveInDB(String dateFormt){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateFormt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("yyyy-MM-dd");
        return fmtOut.format(date);
    }

    // Get time format for saving in DB
    public String getTimeToSaveInDB(String timeFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a");
        Date time = null;
        try {
            time = simpleDateFormat.parse(timeFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm:ss");
        return fmtOut.format(time);
    }

    // Get time format for showing in UI
    public String getTimeToShowInUI(String timeFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        Date time = null;
        try {
            time = simpleDateFormat.parse(timeFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm a");
        return fmtOut.format(time);
    }


    // Get period format for reviewing (with days, hours, and mins tag) from DB
    public String getPeriodFormatFromDB(String period){
        return period.substring(0, 2) + " day "
                + period.substring(3, 5) + " hour "
                + period.substring(6) + " min";
    }

    // Get period format for reviewing (with days, hours, and mins tag) from Edit texts
    public String getPeriodFormatFromUI(String period){
        return period.substring(0, 2) + " day "
                + period.substring(2, 4) + " hour "
                + period.substring(4) + " min";
    }

    // Get notify time with "mins" tag
    public String getNotifyTimeInSHowinFormat(String notifyTime){
        return notifyTime + " mins";
    }
}
