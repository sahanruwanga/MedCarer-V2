package com.sahanruwanga.medcarer.activity;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.DatePickerFragment;
import com.sahanruwanga.medcarer.app.MedicationSchedule;
import com.sahanruwanga.medcarer.app.TimePickerFragment;
import com.sahanruwanga.medcarer.app.User;
import com.sahanruwanga.medcarer.helper.DateTimeFormatting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medication_schedule);

        // Create User object
        this.user = new User(this);

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


        String dateString = getDateTimeFormatting().getDateTimeToShowInUI(getMedicationSchedule().getStartTime());
        getStartDate().setText(dateString.substring(0, 12));
        getStartTime().setText(dateString.substring(13));

        getPeriodDay().setText(getMedicationSchedule().getPeriod().substring(0, 2));
        getPeriodHour().setText(getMedicationSchedule().getPeriod().substring(3, 5));
        getPeriodMin().setText(getMedicationSchedule().getPeriod().substring(6));

        getNotifyMin().setText(getMedicationSchedule().getNotifyTime());
    }

    // Update icon function in toolbar
    public void updateSchedule() {
        String medicine = getMedicine().getText().toString().trim();
        String quantity = getQuantity().getText().toString().trim();
        String completeStartTime = getDateTimeFormatting().getDateTimeToSaveInDB(
                getStartDate().getText().toString().trim() + " " +
                        getStartTime().getText().toString().trim());
        String period = getPeriodDay().getText().toString().trim() + ":"
                + getPeriodHour().getText().toString().trim() + ":"
                + getPeriodMin().getText().toString().trim();
        String notifyTime = getNotifyMin().getText().toString().trim();
        String nextNotifyTime = calculateNextNotifyTime(completeStartTime);
        int syncStatus = getMedicationSchedule().getSyncStatus();
        int statusType = getMedicationSchedule().getStatusType();

        if(!medicine.equals(getMedicationSchedule().getMedicine()) || !quantity.equals(getMedicationSchedule().getQuantity()) ||
                !completeStartTime.equals(getMedicationSchedule().getStartTime()) || !period.equals(getMedicationSchedule().getPeriod()) ||
                !notifyTime.equals(getMedicationSchedule().getNotifyTime())){
            if(medicine.isEmpty() && quantity.isEmpty() && period.equals("00:00:00") && notifyTime.equals(00))
                Toast.makeText(this, "Enter required details!", Toast.LENGTH_SHORT).show();
            else
                getUser().updateMedicationSchedule(getMedicationSchedule().getScheduleId(), medicine,
                        quantity, completeStartTime, period, notifyTime, nextNotifyTime, syncStatus, statusType);
        }


        onBackPressed();

    }

    // Open clock to set time
    public void openClock(View view) {
        setTime(getStartTime().getId());
    }

    // Open calender to set date
    public void openCalender(View view) {
        setDate(getStartDate().getId());
    }

    // Set time using clock
    private void setTime(int timeId){
        Bundle bundle = new Bundle();
        bundle.putInt(TimePickerFragment.TIME_ID, timeId);
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    // Set date using calender
    private void setDate(int dateId){
        Bundle bundle = new Bundle();
        bundle.putInt(DatePickerFragment.DATE_ID, dateId);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Back Icon click on toolbar
    public void backIconClickUpdateMedicationSchedule(View view) {
        onBackPressed();
    }

    // Calculate next notify time
    private String calculateNextNotifyTime(String dateString){
        int periodDay = 0, periodHour = 0, periodMin = 0, notifyMin = 0;
        try {
            periodDay = Integer.parseInt(getPeriodDay().getText().toString().trim());
            periodHour = Integer.parseInt(getPeriodHour().getText().toString().trim());
            periodMin = Integer.parseInt(getPeriodMin().getText().toString().trim());

            notifyMin = Integer.parseInt(getNotifyMin().getText().toString().trim());
        }catch (NumberFormatException e){
            Toast.makeText(this, "Only numbers are required for period and notify time",
                    Toast.LENGTH_SHORT).show();
        }

        if(periodMin >= notifyMin){
            periodMin -= notifyMin;
        }else{
            periodHour -= 1;
            periodMin += 60 - notifyMin;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, periodDay);
        calendar.add(Calendar.HOUR, periodHour);
        calendar.add(Calendar.MINUTE, periodMin);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(calendar.getTime());
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //endregion

}
