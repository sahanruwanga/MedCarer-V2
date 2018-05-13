package com.sahanruwanga.medcarer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.MedicationSchedule;
import com.sahanruwanga.medcarer.helper.DateTimeFormatting;

public class ViewMedicationScheduleActivity extends AppCompatActivity {
    private TextView medicine;
    private TextView quantity;
    private TextView startedAt;
    private TextView period;
    private TextView nextToTake;
    private TextView notifyBefore;

    private MedicationSchedule medicationSchedule;
    private DateTimeFormatting dateTimeFormatting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medication_schedule);

        // Get Medication Schedule object from Intent
        this.medicationSchedule = getIntent().getParcelableExtra(MedicationSchedule.MEDICATION_SCHEDULE);

        // Create DateTimeFormatting object in null
        if(getDateTimeFormatting() == null)
            this.dateTimeFormatting = new DateTimeFormatting();

        // Initializing all widgets
        this.medicine = findViewById(R.id.medicineViewMedicationSchedule);
        this.quantity = findViewById(R.id.quantityViewMedicationSchedule);
        this.startedAt = findViewById(R.id.startedTimeViewMedicationSchedule);
        this.period = findViewById(R.id.periodViewMedicationSchedule);
        this.nextToTake = findViewById(R.id.nextTakingTimeViewMedicationSchedule);
        this.notifyBefore = findViewById(R.id.notifyTimeViewMedicationSchedule);

        // Fill data
        fillData();
    }

    // Fill data into Text Views
    private void fillData(){
        if(getDateTimeFormatting() == null)
            this.dateTimeFormatting = new DateTimeFormatting();

        getMedicine().setText(getMedicationSchedule().getMedicine());
        getQuantity().setText(getMedicationSchedule().getQuantity());
        getStartedAt().setText(getDateTimeFormatting().getDateTimeToShowInUI(getMedicationSchedule().getStartTime()));
        getPeriod().setText(getDateTimeFormatting().getPeriodFormatFromDB(getMedicationSchedule().getPeriod()));

        getNextToTake().setText(getDateTimeFormatting().getNextTimeToTakeMedicine(
                getMedicationSchedule().getNextNotifyTime(), getMedicationSchedule().getNotifyTime()));
        getNotifyBefore().setText(getDateTimeFormatting().getNotifyTimeInSHowinFormat(getMedicationSchedule().getNotifyTime()));
    }

    // Fill data into Text Views
    public void fillDataAfterUpdating(String medicine, String quantity, String startedAt,
                                       String period, String nextToTake, String notifyBefore){
        getMedicine().setText(medicine);
        getQuantity().setText(quantity);
        getStartedAt().setText(startedAt);
        getPeriod().setText(getDateTimeFormatting().getPeriodFormatFromUI(period));
        getNextToTake().setText(nextToTake);
        getNotifyBefore().setText(getDateTimeFormatting().getNotifyTimeInSHowinFormat(notifyBefore));
    }

    // Back Icon click on toolbar
    public void backIconClickViewMedicationSchedule(View view) {
        onBackPressed();
    }

    // Edit Icon click on toolbar
    public void editIconClickViewMedicationSchedule(View view) {
        Intent intent = new Intent(this, UpdateMedicationScheduleActivity.class);
        intent.putExtra(MedicationSchedule.MEDICATION_SCHEDULE, getMedicationSchedule());
        startActivity(intent);
    }

    //region Getters and Setters
    public TextView getMedicine() {
        return medicine;
    }

    public void setMedicine(TextView medicine) {
        this.medicine = medicine;
    }

    public TextView getQuantity() {
        return quantity;
    }

    public void setQuantity(TextView quantity) {
        this.quantity = quantity;
    }

    public TextView getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(TextView startedAt) {
        this.startedAt = startedAt;
    }

    public TextView getPeriod() {
        return period;
    }

    public void setPeriod(TextView period) {
        this.period = period;
    }

    public TextView getNextToTake() {
        return nextToTake;
    }

    public void setNextToTake(TextView nextToTake) {
        this.nextToTake = nextToTake;
    }

    public TextView getNotifyBefore() {
        return notifyBefore;
    }

    public void setNotifyBefore(TextView notifyBefore) {
        this.notifyBefore = notifyBefore;
    }

    public MedicationSchedule getMedicationSchedule() {
        return medicationSchedule;
    }

    public void setMedicationSchedule(MedicationSchedule medicationSchedule) {
        this.medicationSchedule = medicationSchedule;
    }

    public DateTimeFormatting getDateTimeFormatting() {
        return dateTimeFormatting;
    }

    public void setDateTimeFormatting(DateTimeFormatting dateTimeFormatting) {
        this.dateTimeFormatting = dateTimeFormatting;
    }
    //endregion
}
