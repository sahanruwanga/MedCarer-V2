package com.sahanruwanga.medcarer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.sahanruwanga.medcarer.R;

public class AddMedicalRecordActivity extends AppCompatActivity {
    private EditText disease;
    private EditText medicine;
    private EditText allergic;
    private EditText date;
    private EditText doctorName;
    private EditText contact;
    private EditText description;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_record);

        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(getToolbar());
        getToolbar().setTitle(R.string.new_record);
        getToolbar().setLogo(R.drawable.ic_create);

        setDisease((EditText)findViewById(R.id.disease));
        setMedicine((EditText)findViewById(R.id.medicine));
        setAllergic((EditText)findViewById(R.id.allergic));
        setDate((EditText)findViewById(R.id.date));
        setDoctorName((EditText)findViewById(R.id.doctor));
        setContact((EditText)findViewById(R.id.contact));
        setDescription((EditText)findViewById(R.id.description));
    }

    public void saveRecord(View view){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //region Getters and Setters
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

    public EditText getDate() {
        return date;
    }

    public void setDate(EditText date) {
        this.date = date;
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
