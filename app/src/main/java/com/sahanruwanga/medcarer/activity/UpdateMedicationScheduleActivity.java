package com.sahanruwanga.medcarer.activity;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.MedicationSchedule;
import com.sahanruwanga.medcarer.app.TimePickerFragment;
import com.sahanruwanga.medcarer.helper.DateTimeFormatting;

public class UpdateMedicationScheduleActivity extends AppCompatActivity {
    private EditText medicine;
    private EditText quantity;
    private TextView startDate;
    private TextView startTime;
    private EditText periodDay;
    private EditText periodHour;
    private EditText periodMin;
    private EditText notifyMin;
    private Button updateBtn;
    private Button cancelBtn;

    private MedicationSchedule medicationSchedule;
    private DateTimeFormatting dateTimeFormatting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medication_schedule);

        // Get MedicationSchedule object from Intent
        this.medicationSchedule = getIntent().getParcelableExtra(MedicationSchedule.MEDICATION_SCHEDULE);

        // Create DateTimeFormatting object if null
        if(getDateTimeFormatting() == null)
            this.dateTimeFormatting = new DateTimeFormatting();

        // Initializing all widgets
        this.medicine = findViewById(R.id.medicineUpdateMedicationSchedule);
        this.quantity = findViewById(R.id.quantityUpdateMedicationSchedule);
        this.startDate = findViewById(R.id.startDateUpdateMedicationSchedule);
        this.startTime = findViewById(R.id.startTimeUpdateMedicationSchedule);
        this.periodDay = findViewById(R.id.periodDayUpdateMedicationSchedule);
        this.periodHour = findViewById(R.id.periodHourUpdateMedicationSchedule);
        this.periodMin = findViewById(R.id.periodMinUpdateMedicationSchedule);
        this.notifyMin = findViewById(R.id.notifyMinUpdateMedicationSchedule);
        this.updateBtn = findViewById(R.id.updateUpdateMedicationSchedule);
        this.cancelBtn = findViewById(R.id.cancelUpdateMedicationSchedule);

        // Method call for filling data
        fillData();

        // OnCLick listeners for buttons
        getUpdateBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSchedule();
            }
        });

        getCancelBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Fill data in text boxes
    private void fillData(){
        getMedicine().setText(getMedicationSchedule().getMedicine());
        getQuantity().setText(getMedicationSchedule().getQuantity());


        String dateString = getDateTimeFormatting().getDateInShowingFormat(getMedicationSchedule().getStartTime());
        getStartDate().setText(dateString.substring(0, 12));
        getStartTime().setText(dateString.substring(13));

        getPeriodDay().setText(getMedicationSchedule().getPeriod().substring(0, 2));
        getPeriodHour().setText(getMedicationSchedule().getPeriod().substring(3, 5));
        getPeriodMin().setText(getMedicationSchedule().getPeriod().substring(3, 5));

        getNotifyMin().setText(getMedicationSchedule().getNotifyTime());
    }

    // Cancel icon function in toolbar
    public void cancelUpdating(View view) {
        onBackPressed();
    }

    // Update icon function in toolbar
    public void updateSchedule() {
        Toast.makeText(this, "Should update details", Toast.LENGTH_SHORT).show();
        onBackPressed();

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

    // Back Icon click on toolbar
    public void backIconClickUpdateMedicationSchedule(View view) {
        onBackPressed();
    }

    //region Getters and Setters
    public MedicationSchedule getMedicationSchedule() {
        return medicationSchedule;
    }

    public void setMedicationSchedule(MedicationSchedule medicationSchedule) {
        this.medicationSchedule = medicationSchedule;
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

    public TextView getStartTime() {
        return startTime;
    }

    public void setStartTime(TextView startTime) {
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

    public EditText getNotifyMin() {
        return notifyMin;
    }

    public void setNotifyMin(EditText notifyMin) {
        this.notifyMin = notifyMin;
    }


    public TextView getStartDate() {
        return startDate;
    }

    public void setStartDate(TextView startDate) {
        this.startDate = startDate;
    }

    public EditText getPeriodDay() {
        return periodDay;
    }

    public void setPeriodDay(EditText periodDay) {
        this.periodDay = periodDay;
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

    public DateTimeFormatting getDateTimeFormatting() {
        return dateTimeFormatting;
    }

    public void setDateTimeFormatting(DateTimeFormatting dateTimeFormatting) {
        this.dateTimeFormatting = dateTimeFormatting;
    }
    //endregion

}
