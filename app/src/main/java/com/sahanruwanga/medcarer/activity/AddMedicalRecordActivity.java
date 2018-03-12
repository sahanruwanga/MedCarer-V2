package com.sahanruwanga.medcarer.activity;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.DatePickerFragment;

public class AddMedicalRecordActivity extends AppCompatActivity {
    private EditText disease;
    private EditText medicine;
    private EditText allergic;
    private static EditText date1;
    private static EditText date2;
    private EditText doctorName;
    private EditText contact;
    private EditText description;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_record);

        setToolbar((Toolbar) findViewById(R.id.toolbarAddMedicalRecord));
        setSupportActionBar(getToolbar());

        setDisease((EditText)findViewById(R.id.disease));
        setMedicine((EditText)findViewById(R.id.medicine));
        setAllergic((EditText)findViewById(R.id.allergic));
        setDate1((EditText)findViewById(R.id.date1));
        setDate2((EditText)findViewById(R.id.date2));
        setDoctorName((EditText)findViewById(R.id.doctor));
        setContact((EditText)findViewById(R.id.contact));
        setDescription((EditText)findViewById(R.id.description));
        getDate1().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    setDate("date1");
            }
        });
        getDate2().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    setDate("date2");
            }
        });
    }

    public void saveRecord(View view){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void setDate(String dateNum) {
//        DialogFragment newFragment = new TimePickerFragment();
//        newFragment.show(getSupportFragmentManager(), "timePicker");
        Bundle bundle = new Bundle();
        bundle.putString("Date", dateNum);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //region Getters and Setters
    public static EditText getDate2() {
        return date2;
    }

    public static void setDate2(EditText date2) {
        AddMedicalRecordActivity.date2 = date2;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public EditText getDisease() {
        return disease;
    }

    public void setDisease(EditText disease) {
        this.disease = disease;
    }

    public EditText getMedicine() {
        return medicine;
    }

    public void setMedicine(EditText medicine) {
        this.medicine = medicine;
    }

    public EditText getAllergic() {
        return allergic;
    }

    public void setAllergic(EditText allergic) {
        this.allergic = allergic;
    }

    public static EditText getDate1() {
        return date1;
    }

    public static void setDate1(EditText date1) {
        AddMedicalRecordActivity.date1 = date1;
    }

    public EditText getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(EditText doctorName) {
        this.doctorName = doctorName;
    }

    public EditText getContact() {
        return contact;
    }

    public void setContact(EditText contact) {
        this.contact = contact;
    }

    public EditText getDescription() {
        return description;
    }

    public void setDescription(EditText description) {
        this.description = description;
    }


    //endregion
}
