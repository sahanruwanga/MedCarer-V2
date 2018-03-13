package com.sahanruwanga.medcarer.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.AddMedicalRecordActivity;

import java.util.Calendar;

/**
 * Created by Sahan Ruwanga on 3/12/2018.
 */

public class DatePickerFragment extends DialogFragment
                    implements DatePickerDialog.OnDateSetListener{
    int dateId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Bundle bundle = getArguments();
        dateId = bundle.getInt("dateId");
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        String monthNew = String.valueOf(month);
        String dayNew = String.valueOf(day);
        if(month < 10) {
            monthNew = "0"+month;
        }
        if(day < 10){
            dayNew = "0"+day;
        }
        EditText dateText = getActivity().findViewById(dateId);
        dateText.setText(year+"-"+monthNew+"-"+dayNew);
//        if(dateNum.equals("date1"))
//            AddMedicalRecordActivity.getDate1().setText(year+"-"+monthNew+"-"+dayNew);
//        else if(dateNum.equals("date2"))
//            AddMedicalRecordActivity.getDate2().setText(year+"-"+monthNew+"-"+dayNew);
    }
}
