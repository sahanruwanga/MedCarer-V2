package com.sahanruwanga.medcarer.app;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;

/**
 * Created by Sahan Ruwanga on 3/12/2018.
 */

public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener{
    private int timeId;
    public static final String TIME_ID = "timeId";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        Bundle bundle = getArguments();
        this.timeId = bundle.getInt(TIME_ID);
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {



        // Set time in particular time text
        String hourSign = " AM";
        if(hourOfDay > 12 ){
            hourOfDay -= 12;
            hourSign = " PM";
        }else if(hourOfDay == 00){
            hourOfDay = 12;
        }else if(hourOfDay == 12)
            hourSign = " PM";
        String newHour = String.valueOf(hourOfDay);
        if(newHour.length() != 2)
            newHour = "0" + hourOfDay;
        String newMinute = String.valueOf(minute);
        if(newMinute.length() != 2)
            newMinute = "0" + minute;
        TextView timeText = getActivity().findViewById(getTimeId());
        timeText.setText(newHour + ":" + newMinute + hourSign);

    }

    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }
}
