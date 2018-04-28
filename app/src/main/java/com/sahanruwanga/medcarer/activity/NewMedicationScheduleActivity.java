package com.sahanruwanga.medcarer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.AppConfig;
import com.sahanruwanga.medcarer.app.AppController;
import com.sahanruwanga.medcarer.app.TimePickerFragment;
import com.sahanruwanga.medcarer.app.User;
import com.sahanruwanga.medcarer.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewMedicationScheduleActivity extends AppCompatActivity {
    private static final String TAG = MedicationScheduleActivity.class.getSimpleName();
    // EditTexts
    private EditText medicine;
    private EditText quantity;
    private EditText startTime;
    private EditText periodHour;
    private EditText periodMin;
    private EditText periodSec;
    private EditText notifyHour;
    private EditText notifyMin;
    private EditText notifySec;

    // ToolBar
    private Toolbar toolbar;

    // Progress Dialog and SQLiteHelper class objects
    private ProgressDialog progressDialog;
    private SQLiteHandler sqLiteHandler;

    private static final int NOTIFICATION_STATUS_ON = 1;
    private static final int NOTIFICATION_STATUS_OFF = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medication_schedule);

        // Define EditTexts
        this.medicine = findViewById(R.id.medicineMedicationSchedule);
        this.quantity = findViewById(R.id.quantityMedicationSchedule);
        this.startTime = findViewById(R.id.startTimeMedicationSchedule);
        this.periodHour = findViewById(R.id.periodHourMedicationSchedule);
        this.periodMin = findViewById(R.id.periodMinMedicationSchedule);
        this.periodSec = findViewById(R.id.periodSecMedicationSchedule);
        this.notifyHour = findViewById(R.id.notifyHourMedicationSchedule);
        this.notifyMin = findViewById(R.id.notifyMinMedicationSchedule);
        this.notifySec = findViewById(R.id.notifySecMedicationSchedule);

        // Define Toolbar
        this.toolbar = findViewById(R.id.toolbarNewMedicationSchedule);

        // Initializing progress dialog instant
        this.progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // Initializing SQLiteHelper instant
        this.sqLiteHandler = new SQLiteHandler(getApplicationContext());

    }

    // Clock icon function in Start Time
    public void openClock(View view) {
        setTime(getStartTime().getId());
    }

    // Set time using clock
    private void setTime(int timeId){
        Bundle bundle = new Bundle();
        bundle.putInt("timeId", timeId);
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    // Cancel function in toolbar
    public void cancelSaving(View view) {
        Intent intent = new Intent(this, MedicationScheduleActivity.class);
        startActivity(intent);
        finish();
    }

    // Save function in toolbar
    public void saveSchedule(View view) {
        String medicine = getMedicine().getText().toString().trim();
        String quantity = getQuantity().getText().toString().trim();
        String startTime = getStartTime().getText().toString().trim();
        String period = getPeriod();
        String notifyTime = getNotifyTime();
        // Get current date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdAt = dateFormat.format(new Date());
        if(!medicine.isEmpty() && !quantity.isEmpty() && !startTime.equals("00:00:00") &&
                !period.equals("00:00:00") && !notifyTime.equals("00:00:00")){
            saveScheduleInDatabase(medicine, quantity, startTime, period, notifyTime, createdAt, NOTIFICATION_STATUS_ON);
        }else{
            Toast.makeText(this, "Please enter Required Details!", Toast.LENGTH_LONG).show();
        }
    }

    // Save Schedule details in database
    private void saveScheduleInDatabase(final String medicine, final String quantity, final String startTime,
                                        final String period, final String notifyTime, final String createdAt,
                                        final int notificationStatus){
        // Tag used to cancel the request
        String tag_string_req = "req_insert_medication_schedule";

        getProgressDialog().setMessage("Saving Schedule ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT_MEDICATION_SCHEDULE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Insert Schedule: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // Store the schedule in sqlite

                        JSONObject schedule = jObj.getJSONObject("schedule");
                        String schedule_id = schedule.getString("schedule_id");

                        // Inserting row in users table
                        getSqLiteHandler().addMedicationSchedule(Integer.parseInt(schedule_id), medicine, quantity,
                                startTime, period, notifyTime, createdAt, notificationStatus);

                        Toast.makeText(getApplicationContext(), "New Schedule successfully Created!", Toast.LENGTH_LONG).show();
                        clearAll();
                        Intent intent = new Intent(getApplicationContext(), MedicationScheduleActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Enter correct details again",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", User.getUserId());
                params.put("medicine", medicine);
                params.put("quantity", quantity);
                params.put("start_time", startTime);
                params.put("period", period);
                params.put("notify_time", notifyTime);
                params.put("created_at", createdAt);
                params.put("notification_status", String.valueOf(notificationStatus));

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!getProgressDialog().isShowing())
            getProgressDialog().show();
    }

    private void hideDialog() {
        if (getProgressDialog().isShowing())
            getProgressDialog().dismiss();
    }

    private void clearAll(){
        getMedicine().setText("");
        getQuantity().setText("");
        getStartTime().setText("00:00:00");
        getPeriodHour().setText("00");
        getPeriodMin().setText("00");
        getPeriodSec().setText("00");
        getNotifyHour().setText("00");
        getNotifyMin().setText("00");
        getNotifySec().setText("00");
    }

    //region Calculate Period and Notify Time
    // Return complete value for Period
    private String getPeriod(){
        if(getPeriodHour().getText().toString().length() == 0)
            getPeriodHour().setText("00");
        else if(getPeriodHour().getText().toString().length() == 1)
            getPeriodHour().setText("0" + getPeriodHour().getText().toString().trim());

        if(getPeriodMin().getText().toString().length() == 0)
            getPeriodMin().setText("00");
        else if(getPeriodMin().getText().toString().length() == 1)
            getPeriodMin().setText("0" + getPeriodMin().getText().toString().trim());

        if(getPeriodSec().getText().toString().length() == 0)
            getPeriodSec().setText("00");
        else if(getPeriodSec().getText().toString().length() == 1)
            getPeriodSec().setText("0" + getPeriodSec().getText().toString().trim());

        return getPeriodHour().getText().toString().trim() + ":" +
                getPeriodMin().getText().toString().trim() + ":" +
                getPeriodSec().getText().toString().trim();
    }

    // Return complete value for "Notify Before" time
    private String getNotifyTime(){
        if(getNotifyHour().getText().toString().length() == 0)
            getNotifyHour().setText("00");
        else if(getNotifyHour().getText().toString().length() == 1)
            getNotifyHour().setText("0" + getNotifyHour().getText().toString().trim());

        if(getNotifyMin().getText().toString().length() == 0)
            getNotifyMin().setText("00");
        else if(getNotifyMin().getText().toString().length() == 1)
            getNotifyMin().setText("0" + getNotifyMin().getText().toString().trim());

        if(getNotifySec().getText().toString().length() == 0)
            getNotifySec().setText("00");
        else if(getNotifySec().getText().toString().length() == 1)
            getNotifySec().setText("0" + getNotifySec().getText().toString().trim());

        return getNotifyHour().getText().toString().trim() + ":" +
                getNotifyMin().getText().toString().trim() + ":" +
                getNotifySec().getText().toString().trim();
    }
    //endregion

    //region Getters and Setters
    public EditText getMedicine() {
        return medicine;
    }

    public void setMedicine(EditText medicine) {
        this.medicine = medicine;
    }

    public EditText getQuantity() {
        return quantity;
    }

    public void setQuantity(EditText quantity) {
        this.quantity = quantity;
    }

    public EditText getStartTime() {
        return startTime;
    }

    public void setStartTime(EditText startTime) {
        this.startTime = startTime;
    }

    public EditText getPeriodHour() {
        return periodHour;
    }

    public void setPeriodHour(EditText periodHour) {
        this.periodHour = periodHour;
    }

    public EditText getPeriodMin() {
        return periodMin;
    }

    public void setPeriodMin(EditText periodMin) {
        this.periodMin = periodMin;
    }

    public EditText getPeriodSec() {
        return periodSec;
    }

    public void setPeriodSec(EditText periodSec) {
        this.periodSec = periodSec;
    }

    public EditText getNotifyHour() {
        return notifyHour;
    }

    public void setNotifyHour(EditText notifyHour) {
        this.notifyHour = notifyHour;
    }

    public EditText getNotifyMin() {
        return notifyMin;
    }

    public void setNotifyMin(EditText notifyMin) {
        this.notifyMin = notifyMin;
    }

    public EditText getNotifySec() {
        return notifySec;
    }

    public void setNotifySec(EditText notifySec) {
        this.notifySec = notifySec;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public SQLiteHandler getSqLiteHandler() {
        return sqLiteHandler;
    }

    public void setSqLiteHandler(SQLiteHandler sqLiteHandler) {
        this.sqLiteHandler = sqLiteHandler;
    }
    //endregion
}
