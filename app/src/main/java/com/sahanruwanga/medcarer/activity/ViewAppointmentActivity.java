package com.sahanruwanga.medcarer.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.Appointment;

public class ViewAppointmentActivity extends AppCompatActivity {
    private Appointment appointment;
    private Toolbar toolbar;
    private TextView venue;
    private TextView contact;
    private TextView date;
    private TextView time;
    private TextView reason;
    private TextView doctor;
    private TextView notifyTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);

        // Get appointment object from intent
        appointment = getIntent().getParcelableExtra("Appointment");

        // Initialize toolbar
        this.toolbar = findViewById(R.id.toolbarVieAppointment);
        setSupportActionBar(toolbar);

        // Initialize text views
        this.venue = findViewById(R.id.venueAppointmentDetail);
        this.contact = findViewById(R.id.contactAppointmentDetail);
        this.date = findViewById(R.id.dateAppointmentDetail);
        this.time = findViewById(R.id.timeAppointmentDetail);
        this.reason = findViewById(R.id.reasonAppointmentDetail);
        this.doctor = findViewById(R.id.doctorAppointmentDetail);
        this.notifyTime = findViewById(R.id.notifyTimeAppointmentDetail);

        // Set text in text views
        getVenue().setText(getAppointment().getVenue());
        getContact().setText(getAppointment().getClinicContact());
        getDate().setText(getAppointment().getDate());
        getTime().setText(getAppointment().getTime());
        getReason().setText(getAppointment().getReason());
        getDoctor().setText(getAppointment().getDoctor());
        getNotifyTime().setText(getAppointment().getNotifyTime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.editIcon){
            Intent intent = new Intent(this, UpdateAppointmentActivity.class);
            intent.putExtra("Appointment", getAppointment());
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // Clinic contact number opens in dial pad
    public void openInDialPad(View view) {
        Uri number = Uri.parse("tel:" + getContact().getText().toString().trim());
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    // Back icon click in toolbar
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

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public TextView getVenue() {
        return venue;
    }

    public void setVenue(TextView venue) {
        this.venue = venue;
    }

    public TextView getContact() {
        return contact;
    }

    public void setContact(TextView contact) {
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

    public TextView getReason() {
        return reason;
    }

    public void setReason(TextView reason) {
        this.reason = reason;
    }

    public TextView getDoctor() {
        return doctor;
    }

    public void setDoctor(TextView doctor) {
        this.doctor = doctor;
    }

    public TextView getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(TextView notifyTime) {
        this.notifyTime = notifyTime;
    }
    //endregion
}
