package com.sahanruwanga.medcarer.activity;


import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.DatePickerFragment;
import com.sahanruwanga.medcarer.app.TimePickerFragment;
import com.sahanruwanga.medcarer.app.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddAppointmentActivity extends AppCompatActivity {
    private EditText reasonText;
    private EditText venueText;
    private EditText doctorText;
    private EditText clinicContactText;
    private EditText dateText;
    private EditText timeText;
    private EditText notifyTimeText;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        // Create User Object
        if(getUser() == null)
            this.user = new User(this);

        // Define all Edit Texts and the switch
        this.reasonText = findViewById(R.id.reasonAppointment);
        this.venueText  = findViewById(R.id.venueAppointment);
        this.doctorText = findViewById(R.id.doctorAppointment);
        this.clinicContactText = findViewById(R.id.clinicContactAppointment);
        this.dateText = findViewById(R.id.dateAppointment);
        this.timeText = findViewById(R.id.timeAppointment);
        this.notifyTimeText = findViewById(R.id.notifyTimeAppointment);

        // Set onFocusListener to open up calender in date text
        getDateText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    setDate(getDateText().getId());
            }
        });

        // Set onFocusListener to open up timer in time & notify time texts
        getTimeText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    setTime(getTimeText().getId());
            }
        });

        getNotifyTimeText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if(hasfocus)
                    setTime(getNotifyTimeText().getId());
            }
        });
    }

    private void setDate(int dateId){
        Bundle bundle = new Bundle();
        bundle.putInt("dateId", dateId);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void setTime(int timeId){
        Bundle bundle = new Bundle();
        bundle.putInt("timeId", timeId);
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    // Cancel function in toolbar
    public void cancelSaving(View view) {
        onBackPressed();
    }

    // Save function on toolbar
    public void saveAppointment(View view) {
        // Get daata from edit text boxes
        String reason = getReasonText().getText().toString().trim();
        String venue = getVenueText().getText().toString().trim();
        String doctor = getDoctorText().getText().toString().trim();
        String clinicNo = getClinicContactText().getText().toString().trim();
        String date = getDateText().getText().toString().trim();
        String time = getTimeFormat(getTimeText().getText().toString().trim());
        String notifyTime = getTimeFormat(getNotifyTimeText().getText().toString().trim());

        // Get current date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdAt = dateFormat.format(new Date());

        // Check for essential data
        if(!reason.isEmpty() && !venue.isEmpty() && !doctor.isEmpty() &&
                !date.isEmpty() && !time.isEmpty() && !notifyTime.isEmpty()){
            getUser().saveNewAppointment(reason, date, time, venue, doctor, clinicNo, notifyTime,
                    createdAt);
        }else{
            Toast.makeText(this, "Please enter required data", Toast.LENGTH_LONG).show();
        }

    }

    // Set time format to save in DB
    private String getTimeFormat(String time){
        if(time.substring(6).equals("AM")) {
            if (time.substring(0, 2).equals("12"))
                time = "00" + time.substring(2, 5) + ":00";
            else
                time = time.substring(0, 5) + ":00";
        }else{
            if(time.substring(0, 2).equals("12"))
                time = time.substring(0, 5) + ":00";
            else
                time = String.valueOf(Integer.parseInt(time.substring(0,2)) + 12) + time.substring(2, 5) + ":00";
        }
        return time;
    }

    // onClick for Calender icon
    public void openCalender(View view) {
        setDate(getDateText().getId());
    }

    // onClick for clock for appointment time
    public void openClockForAppointmentTime(View view) {
        setTime(getTimeText().getId());
    }

    // onClick for clock for notify time
    public void openClockForNotifyTime(View view) {
        setTime(getNotifyTimeText().getId());
    }

    private void clearAll(){
        getReasonText().setText("");
        getVenueText().setText("");
        getDoctorText().setText("");
        getClinicContactText().setText("");
        getDateText().setText("");
        getTimeText().setText("");
        getNotifyTimeText().setText("");
        getReasonText().requestFocus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AllergicMedicineActivity.class);
        startActivity(intent);
        finish();
    }

    //region Getters and Setters
    public EditText getReasonText() {
        return reasonText;
    }

    public void setReasonText(EditText reasonText) {
        this.reasonText = reasonText;
    }

    public EditText getVenueText() {
        return venueText;
    }

    public void setVenueText(EditText venueText) {
        this.venueText = venueText;
    }

    public EditText getDoctorText() {
        return doctorText;
    }

    public void setDoctorText(EditText doctorText) {
        this.doctorText = doctorText;
    }

    public EditText getClinicContactText() {
        return clinicContactText;
    }

    public void setClinicContactText(EditText clinicContactText) {
        this.clinicContactText = clinicContactText;
    }

    public EditText getDateText() {
        return dateText;
    }

    public void setDateText(EditText dateText) {
        this.dateText = dateText;
    }

    public EditText getTimeText() {
        return timeText;
    }

    public void setTimeText(EditText timeText) {
        this.timeText = timeText;
    }

    public EditText getNotifyTimeText() {
        return notifyTimeText;
    }

    public void setNotifyTimeText(EditText notifyTimeText) {
        this.notifyTimeText = notifyTimeText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //endregion
}
