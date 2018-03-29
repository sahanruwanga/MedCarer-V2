package com.sahanruwanga.medcarer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.AppConfig;
import com.sahanruwanga.medcarer.app.AppController;
import com.sahanruwanga.medcarer.app.DatePickerFragment;
import com.sahanruwanga.medcarer.app.TimePickerFragment;
import com.sahanruwanga.medcarer.app.User;
import com.sahanruwanga.medcarer.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddAppointmentActivity extends AppCompatActivity {
    private static final String TAG = AppointmentActivity.class.getSimpleName();

    private EditText reasonText;
    private EditText venueText;
    private EditText doctorText;
    private EditText clinicContactText;
    private EditText dateText;
    private EditText timeText;
    private EditText notifyTimeText;

    private ProgressDialog progressDialog;
    private SQLiteHandler sqLiteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        // Initialization of progress dialog and SQLiteHelper
        this.progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        this.sqLiteHandler = new SQLiteHandler(getApplicationContext());

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
        String time = getTimeText().getText().toString().trim();
        String notifyTime = getNotifyTimeText().getText().toString().trim();

        // Check for essential data
        if(!reason.isEmpty() && !venue.isEmpty() && !doctor.isEmpty() &&
                !date.isEmpty() && !time.isEmpty()){
            saveAppointmentInDatabase(reason, venue, doctor, clinicNo, date, time, notifyTime);
        }else{
            Toast.makeText(this, "Please enter required data", Toast.LENGTH_LONG).show();
        }

    }

    private boolean saveAppointmentInDatabase(final String reason, final String venue, final String doctor, final String clinicNo,
                                           final String date, final String time, final String notifyTime){
        final boolean[] isSuccessful = {false};
        // Tag used to cancel the request
        String tag_string_req = "req_insert_appointment";

        progressDialog.setMessage("Saving appointment ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT_APPOINTMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Insert Appointment: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // Store the appointment in sqlite

                        JSONObject medicalRecord = jObj.getJSONObject("appointment");
                        String appointment_id = medicalRecord.getString("appointment_id");
                        String created_at = medicalRecord.getString("created_at");

                        // Inserting row in users table
                        sqLiteHandler.addAppointment(Integer.parseInt(appointment_id), reason, date, time,
                                venue, doctor, clinicNo, notifyTime, created_at);

                        Toast.makeText(getApplicationContext(), "Appointment successfully inserted!", Toast.LENGTH_LONG).show();
                        clearAll();
                        isSuccessful[0] = true;
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        isSuccessful[0] = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Enter correct details again",Toast.LENGTH_LONG).show();
                    isSuccessful[0] = false;
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
                isSuccessful[0] = false;
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", User.getUserId());
                params.put("reason", reason);
                params.put("venue", venue);
                params.put("doctor", doctor);
                params.put("clinic_contact", clinicNo);
                params.put("date", date);
                params.put("time", time);
                params.put("notify_time", notifyTime);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return isSuccessful[0];
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

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
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

    //endregion
}
