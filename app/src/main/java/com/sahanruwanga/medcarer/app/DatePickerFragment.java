package com.sahanruwanga.medcarer.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.AddMedicalRecordActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String date = dateFormat.format(calendar.getTime());

        EditText dateText = getActivity().findViewById(dateId);
        dateText.setText(date);


    }

}
