package com.sahanruwanga.medcarer.activity;


import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.Appointment;
import com.sahanruwanga.medcarer.app.DatePickerFragment;
import com.sahanruwanga.medcarer.app.TimePickerFragment;
import com.sahanruwanga.medcarer.app.User;
import com.sahanruwanga.medcarer.helper.DateTimeFormatting;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddAppointmentActivity extends AppCompatActivity {
    private EditText reasonText;
    private EditText venueText;
    private EditText doctorText;
    private EditText clinicContactText;
    private TextView dateText;
    private TextView timeText;
    private TextView notifyTimeText;
    private Button saveBtn;
    private Button cancelBtn;

    private User user;
    private DateTimeFormatting dateTimeFormatting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        // Create User Object
        if(getUser() == null)
            this.user = new User(this);

        // Create DateTimeFormatting object
        this.dateTimeFormatting = new DateTimeFormatting();

        // Define all widget Edit Texts and the switch
        this.reasonText = findViewById(R.id.reasonAppointment);
        this.venueText  = findViewById(R.id.venueAppointment);
        this.doctorText = findViewById(R.id.doctorAppointment);
        this.clinicContactText = findViewById(R.id.clinicContactAppointment);
        this.dateText = findViewById(R.id.dateAppointment);
        this.timeText = findViewById(R.id.timeAppointment);
        this.notifyTimeText = findViewById(R.id.notifyTimeAppointment);
        this.saveBtn = findViewById(R.id.saveAppointment);
        this.cancelBtn = findViewById(R.id.cancelAppointment);

        getDateText().setText(getCurrentDateTimeUI().substring(0, 12));
        getTimeText().setText(getCurrentDateTimeUI().substring(13));
        getNotifyTimeText().setText(getCurrentDateTimeUI().substring(13));

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

        // OnClick events for save and cancel buttons
        getSaveBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAppointment();
            }
        });

        getCancelBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setDate(int dateId){
        Bundle bundle = new Bundle();
        bundle.putInt(DatePickerFragment.DATE_ID, dateId);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void setTime(int timeId){
        Bundle bundle = new Bundle();
        bundle.putInt(TimePickerFragment.TIME_ID, timeId);
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    // Save function
    public void saveAppointment() {
        // Get data from edit text boxes
        String reason = getReasonText().getText().toString().trim();
        String venue = getVenueText().getText().toString().trim();
        String doctor = getDoctorText().getText().toString().trim();
        String clinicNo = getClinicContactText().getText().toString().trim();
        String date = getDateTimeFormatting().getDateToSaveInDB(getDateText().getText().toString().trim());
        String time = getDateTimeFormatting().getTimeToSaveInDB(getTimeText().getText().toString().trim());
        String notifyTime = getDateTimeFormatting().getTimeToSaveInDB(getNotifyTimeText().getText().toString().trim());

        // Get current date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdAt = dateFormat.format(new Date());

        // Check for essential data
        if(!reason.isEmpty() && !venue.isEmpty() && !doctor.isEmpty() &&
                !date.isEmpty() && !time.isEmpty() && !notifyTime.isEmpty()){
            long id = getUser().saveNewAppointment(reason, date, time, venue, doctor, clinicNo, notifyTime,
                    createdAt);
            Appointment appointment = new Appointment();
            appointment.setAppointmentId((int) id);
            appointment.setReason(reason);
            appointment.setVenue(venue);
            appointment.setDoctor(doctor);
            appointment.setClinicContact(clinicNo);
            appointment.setDate(date);
            appointment.setTime(time);
            appointment.setNotifyTime(notifyTime);
            Intent intent = new Intent();
            intent.putExtra(Appointment.APPOINTMENT, appointment);
            setResult(1, intent);
            finish();
        }else{
            Toast.makeText(this, "Please enter required data", Toast.LENGTH_LONG).show();
        }

    }

    private String getCurrentDateTimeUI(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
        return  dateFormat.format(new Date());
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
    }

    // Back Icon click on toolbar
    public void backIconClick(View view) {
        onBackPressed();
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

    public TextView getDateText() {
        return dateText;
    }

    public void setDateText(TextView dateText) {
        this.dateText = dateText;
    }

    public TextView getTimeText() {
        return timeText;
    }

    public void setTimeText(TextView timeText) {
        this.timeText = timeText;
    }

    public TextView getNotifyTimeText() {
        return notifyTimeText;
    }

    public void setNotifyTimeText(TextView notifyTimeText) {
        this.notifyTimeText = notifyTimeText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Button getSaveBtn() {
        return saveBtn;
    }

    public void setSaveBtn(Button saveBtn) {
        this.saveBtn = saveBtn;
    }

    public Button getCancelBtn() {
        return cancelBtn;
    }

    public void setCancelBtn(Button cancelBtn) {
        this.cancelBtn = cancelBtn;
    }

    public DateTimeFormatting getDateTimeFormatting() {
        return dateTimeFormatting;
    }

    public void setDateTimeFormatting(DateTimeFormatting dateTimeFormatting) {
        this.dateTimeFormatting = dateTimeFormatting;
    }
    //endregion
}
