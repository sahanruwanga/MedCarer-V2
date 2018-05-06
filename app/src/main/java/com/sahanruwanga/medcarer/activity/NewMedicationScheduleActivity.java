package com.sahanruwanga.medcarer.activity;

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
import com.sahanruwanga.medcarer.app.DatePickerFragment;
import com.sahanruwanga.medcarer.app.TimePickerFragment;
import com.sahanruwanga.medcarer.app.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

public class NewMedicationScheduleActivity extends AppCompatActivity {
    private static final String TAG = MedicationScheduleActivity.class.getSimpleName();
    // EditTexts
    private EditText medicine;
    private EditText quantity;
    private TextView startDate;
    private TextView startTime;
    private EditText periodDay;
    private EditText periodHour;
    private EditText periodMin;
    private EditText notifyMin;
    private Button saveBtn;
    private Button cancelBtn;

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

        // Define all widgets
        this.medicine = findViewById(R.id.medicineNewMedicationSchedule);
        this.quantity = findViewById(R.id.quantityNewMedicationSchedule);
        this.startDate = findViewById(R.id.startDateNewMedicationSchedule);
        this.startTime = findViewById(R.id.startTimeNewMedicationSchedule);
        this.periodDay = findViewById(R.id.periodDayNewMedicationSchedule);
        this.periodHour = findViewById(R.id.periodHourNewMedicationSchedule);
        this.periodMin = findViewById(R.id.periodMinNewMedicationSchedule);
        this.notifyMin = findViewById(R.id.notifyMinNewMedicationSchedule);
        this.saveBtn = findViewById(R.id.saveNewMedicationSchedule);
        this.cancelBtn = findViewById(R.id.cancelNewMedicationSchedule);

        // Define Toolbar
        this.toolbar = findViewById(R.id.toolbarNewMedicationSchedule);

        // OnClick listeners for buttons
        getSaveBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSchedule();
            }
        });

        getCancelBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Clock icon function in Start Time
    public void openClock(View view) {
        setDate(getStartDate().getId());
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

    // Set date using caender
    private void setDate(int dateId){
        Bundle bundle = new Bundle();
        bundle.putInt("dateId", dateId);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    // Save function
    public void saveSchedule() {
        String medicine = getMedicine().getText().toString().trim();
        String quantity = getQuantity().getText().toString().trim();
        String startDate = getStartDate().getText().toString().trim();
        String startTime = getStartTime().getText().toString().trim();
        String period = getPeriod();
        String notifyTime = getNotifyTime();
        // Get current date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdAt = dateFormat.format(new Date());

        String fullStartedTime = getFullStartedTime(startDate+ " " + startTime);

        String nextNotifyTime = calculateNextNotifyTime(fullStartedTime);

        if(!medicine.isEmpty() && !quantity.isEmpty() && !startTime.equals("00:00:00") &&
                !period.equals("00:00:00") && !notifyTime.equals("00:00:00")){
            clearAll();
            getUser().saveNewMedicationSchedule(medicine, quantity, fullStartedTime,
                    period, notifyTime, nextNotifyTime, createdAt);

        }else{
            Toast.makeText(this, "Please enter Required Details!", Toast.LENGTH_LONG).show();
        }
    }


    private void clearAll(){
        getMedicine().setText("");
        getQuantity().setText("");
        getStartTime().setText("00:00 AM");
        getPeriodDay().setText("00");
        getPeriodHour().setText("00");
        getPeriodMin().setText("00");
        getNotifyMin().setText("00");
    }

    // Back Icon click on toolbar
    public void backIconClickNewMedicationSchedule(View view) {
        onBackPressed();
    }

    //region Calculate Period and Notify Time
    // Return complete value for Period
    private String getFullStartedTime(String dateString){
        SimpleDateFormat fmt = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmtOut.format(date);
    }

    private String getPeriod(){
        if(getPeriodHour().getText().toString().length() == 0)
            getPeriodHour().setText("00");
        else if(getPeriodHour().getText().toString().length() == 1)
            getPeriodHour().setText("0" + getPeriodHour().getText().toString().trim());

        if(getPeriodMin().getText().toString().length() == 0)
            getPeriodMin().setText("00");
        else if(getPeriodMin().getText().toString().length() == 1)
            getPeriodMin().setText("0" + getPeriodMin().getText().toString().trim());

        if(getPeriodDay().getText().toString().length() == 0)
            getPeriodDay().setText("00");
        else if(getPeriodDay().getText().toString().length() == 1)
            getPeriodDay().setText("0" + getPeriodDay().getText().toString().trim());

        return getPeriodDay().getText().toString().trim() + ":" +
                getPeriodHour().getText().toString().trim() + ":" +
                getPeriodMin().getText().toString().trim();
    }

    // Return complete value for "Notify Before" time
    private String getNotifyTime(){

        if(getNotifyMin().getText().toString().length() == 0)
            getNotifyMin().setText("00");
        else if(getNotifyMin().getText().toString().length() == 1)
            getNotifyMin().setText("0" + getNotifyMin().getText().toString().trim());


        return getNotifyMin().getText().toString().trim();
    }

    // Calculate next notify time
    private String calculateNextNotifyTime(String dateString){
        int periodDay = Integer.parseInt(getPeriodDay().getText().toString().trim());
        int periodHour = Integer.parseInt(getPeriodHour().getText().toString().trim());
        int periodMin = Integer.parseInt(getPeriodMin().getText().toString().trim());

        int notifyMin = Integer.parseInt(getNotifyMin().getText().toString().trim());

        if(periodMin > notifyMin){
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

    public EditText getPeriodDay() {
        return periodDay;
    }

    public void setPeriodDay(EditText periodDay) {
        this.periodDay = periodDay;
    }

    public Button getCancelBtn() {
        return cancelBtn;
    }

    public void setCancelBtn(Button cancelBtn) {
        this.cancelBtn = cancelBtn;
    }

    public Button getSaveBtn() {
        return saveBtn;
    }

    public void setSaveBtn(Button saveBtn) {
        this.saveBtn = saveBtn;
    }

    public TextView getStartDate() {
        return startDate;
    }

    public void setStartDate(TextView startDate) {
        this.startDate = startDate;
    }

    //endregion
}
