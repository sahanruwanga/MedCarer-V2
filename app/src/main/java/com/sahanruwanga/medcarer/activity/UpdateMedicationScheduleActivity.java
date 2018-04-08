package com.sahanruwanga.medcarer.activity;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.MedicationSchedule;
import com.sahanruwanga.medcarer.app.TimePickerFragment;

public class UpdateMedicationScheduleActivity extends AppCompatActivity {
    private MedicationSchedule medicationSchedule;
    private Toolbar toolbar;
    private EditText medicine;
    private EditText quantity;
    private EditText startTime;
    private EditText periodHour;
    private EditText periodMin;
    private EditText periodSec;
    private EditText notifyHour;
    private EditText notifyMin;
    private EditText notifySec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medication_schedule);

        // Get MedicationSchedule object from Intent
        medicationSchedule = getIntent().getParcelableExtra("MedicationSchedule");

        // Initializing toolbar
        this.toolbar = findViewById(R.id.toolbarUpdateMedicationSchedule);

        // Initializing EditTexts
        this.medicine = findViewById(R.id.medicineUpdateMedicationSchedule);
        this.quantity = findViewById(R.id.quantityUpdateMedicationSchedule);
        this.startTime = findViewById(R.id.startTimeUpdateMedicationSchedule);
        this.periodHour = findViewById(R.id.periodHourUpdateMedicationSchedule);
        this.periodMin = findViewById(R.id.periodMinUpdateMedicationSchedule);
        this.periodSec = findViewById(R.id.periodSecUpdateMedicationSchedule);
        this.notifyHour = findViewById(R.id.notifyHourUpdateMedicationSchedule);
        this.notifyMin = findViewById(R.id.notifyMinUpdateMedicationSchedule);
        this.notifySec = findViewById(R.id.notifySecUpdateMedicationSchedule);

        // Method call for filling data
        fillData();
    }

    // Fill data in text boxes
    private void fillData(){
        getMedicine().setText(getMedicationSchedule().getMedicine());
        getQuantity().setText(getMedicationSchedule().getQuantity());
        getStartTime().setText(getMedicationSchedule().getStartTime());
        getPeriodHour().setText(getMedicationSchedule().getPeriod().substring(0, 2));
        getPeriodMin().setText(getMedicationSchedule().getPeriod().substring(3, 5));
        getPeriodSec().setText(getMedicationSchedule().getPeriod().substring(6));
        getNotifyHour().setText(getMedicationSchedule().getNotifyTime().substring(0, 2));
        getNotifyMin().setText(getMedicationSchedule().getNotifyTime().substring(3, 5));
        getNotifyMin().setText(getMedicationSchedule().getNotifyTime().substring(6));
    }

    // Cancel icon function in toolbar
    public void cancelUpdating(View view) {
        onBackPressed();
    }

    // Update icon function in toolbar
    public void updateSchedule(View view) {
        Toast.makeText(this, "Should update details", Toast.LENGTH_SHORT).show();
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

    //region Getters and Setters
    public MedicationSchedule getMedicationSchedule() {
        return medicationSchedule;
    }

    public void setMedicationSchedule(MedicationSchedule medicationSchedule) {
        this.medicationSchedule = medicationSchedule;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

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
    //endregion

}
