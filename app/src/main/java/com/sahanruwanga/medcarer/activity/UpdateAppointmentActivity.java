package com.sahanruwanga.medcarer.activity;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private TextView date;
    private TextView time;
    private EditText reason;
    private EditText doctor;
    private TextView notifyTime;
    private Button updateBtn;
    private Button cancelBtn;

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
        this.updateBtn = findViewById(R.id.updateAppointment);
        this.cancelBtn = findViewById(R.id.cancelAppointmentUpdate);

        // Fill data in edit text boxes
        fillData();

        // OnClick events for buttons
        getUpdateBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAppointment();
            }
        });

        getCancelBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void openCalender(View view) {
        setDate(getDate().getId());
    }
    public void openClockForNotifyTime(View view) {
        setTime(getNotifyTime().getId());
    }

    public void openClockForAppointmentTime(View view) {
        setTime(getTime().getId());
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

    // Save (Update) function on toolbar
    public void updateAppointment() {
        Toast.makeText(this, "Should Update database", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AppointmentActivity.class);
        startActivity(intent);
        finish();
    }

    // Back Icon click on toolbar
    public void backIconClick(View view) {
        onBackPressed();
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

    public TextView getDate() {
        return date;
    }

    public void setDate(TextView date) {
        this.date = date;
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
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

    public TextView getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(TextView notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public Button getUpdateBtn() {
        return updateBtn;
    }

    public void setUpdateBtn(Button updateBtn) {
        this.updateBtn = updateBtn;
    }

    public Button getCancelBtn() {
        return cancelBtn;
    }

    public void setCancelBtn(Button cancelBtn) {
        this.cancelBtn = cancelBtn;
    }
    //endregion
}
