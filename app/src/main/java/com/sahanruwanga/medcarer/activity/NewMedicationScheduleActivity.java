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

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medication_schedule);

        // Create User object
        if(getUser() == null)
            this.user = new User(this);

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
            clearAll();
            getUser().saveNewMedicationSchedule(medicine, quantity, startTime, period, notifyTime, createdAt);

        }else{
            Toast.makeText(this, "Please enter Required Details!", Toast.LENGTH_LONG).show();
        }
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //endregion
}
