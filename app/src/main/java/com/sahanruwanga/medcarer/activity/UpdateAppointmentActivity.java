package com.sahanruwanga.medcarer.activity;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.Appointment;
import com.sahanruwanga.medcarer.app.DatePickerFragment;
import com.sahanruwanga.medcarer.app.TimePickerFragment;

public class UpdateAppointmentActivity extends AppCompatActivity {
    private Appointment appointment;
    private Toolbar toolbar;
    private EditText venue;
    private EditText contact;
    private EditText date;
    private EditText time;
    private EditText reason;
    private EditText doctor;
    private EditText notifyTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_appointment);

        // Get appointment object from intent
        this.appointment = getIntent().getParcelableExtra("Appointment");

        // Initializing toolbar
        this.toolbar = findViewById(R.id.toolbarUpdateAppointment);
        setSupportActionBar(toolbar);

        // Initializing edit texts
        this.venue = findViewById(R.id.venueUpdateAppointment);
        this.contact = findViewById(R.id.clinicContactUpdateAppointment);
        this.date = findViewById(R.id.dateUpdateAppointment);
        this.time = findViewById(R.id.timeUpdateAppointment);
        this.reason = findViewById(R.id.reasonUpdateAppointment);
        this.doctor = findViewById(R.id.doctorUpdateAppointment);
        this.notifyTime = findViewById(R.id.notifyTimeUpdateAppointment);

        // Fill data in edit text boxes
        fillData();

        // Event listeners for opening Calender and Clock
        getDate().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    setDate(getDate().getId());
            }
        });

        getTime().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    setTime(getTime().getId());
            }
        });

        getNotifyTime().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    setTime(getNotifyTime().getId());
            }
        });


    }

    // For fill data in edit text boxes
    public void fillData(){
        getVenue().setText(getAppointment().getVenue());
        getContact().setText(getAppointment().getClinicContact());
        getDate().setText(getAppointment().getDate());
        getTime().setText(getAppointment().getTime());
        getReason().setText(getAppointment().getReason());
        getDoctor().setText(getAppointment().getDoctor());
        getNotifyTime().setText(getAppointment().getNotifyTime());
    }

    // Set Date from calender
    private void setDate(int dateId){
        Bundle bundle = new Bundle();
        bundle.putInt("dateId", dateId);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Set Time from clock
    private void setTime(int timeId){
        Bundle bundle = new Bundle();
        bundle.putInt("timeId", timeId);
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    // Cancel icon function on toolbar
    public void cancelUpdating(View view) {
        onBackPressed();
    }

    // Save (Update) function on toolbar
    public void updateAppointment(View view) {
        Toast.makeText(this, "Should Update database", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AppointmentActivity.class);
        startActivity(intent);
        finish();
    }

    //region Getters and Setters
    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public EditText getVenue() {
        return venue;
    }

    public void setVenue(EditText venue) {
        this.venue = venue;
    }

    public EditText getContact() {
        return contact;
    }

    public void setContact(EditText contact) {
        this.contact = contact;
    }

    public EditText getDate() {
        return date;
    }

    public void setDate(EditText date) {
        this.date = date;
    }

    public EditText getTime() {
        return time;
    }

    public void setTime(EditText time) {
        this.time = time;
    }

    public EditText getReason() {
        return reason;
    }

    public void setReason(EditText reason) {
        this.reason = reason;
    }

    public EditText getDoctor() {
        return doctor;
    }

    public void setDoctor(EditText doctor) {
        this.doctor = doctor;
    }

    public EditText getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(EditText notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }
    //endregion
}
