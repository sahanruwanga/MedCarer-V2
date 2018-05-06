package com.sahanruwanga.medcarer.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sahan Ruwanga on 5/6/2018.
 */

public class DateTimeFormatting {

    // Get date format for reviewing (AM/PM)
    public String getDateInShowingFormat(String dateTime){
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
