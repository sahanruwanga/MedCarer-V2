package com.sahanruwanga.medcarer.activity;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.DatePickerFragment;
import com.sahanruwanga.medcarer.app.MedicalRecord;

public class UpdateMedicalRecordActivity extends AppCompatActivity {
    private MedicalRecord medicalRecord;
    private EditText disease;
    private EditText medicine;
    private EditText allergic;
    private static EditText date1;
    private static EditText date2;
    private EditText doctor;
    private EditText contact;
    private EditText description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medical_record);

        medicalRecord = getIntent().getParcelableExtra("medicalRecord");

        setDisease((EditText)findViewById(R.id.diseaseUpdate));
        setMedicine((EditText)findViewById(R.id.medicineUpdate));
        setAllergic((EditText)findViewById(R.id.allergicUpdate));
        setDate1((EditText)findViewById(R.id.date1Update));
        setDate2((EditText)findViewById(R.id.date2Update));
        setDoctor((EditText)findViewById(R.id.doctorUpdate));
        setContact((EditText)findViewById(R.id.contactUpdate));
        setDescription((EditText)findViewById(R.id.descriptionUpdate));

        getDate1().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    setDate(getDate1().getId());
            }
        });
        getDate2().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    setDate(getDate2().getId());
            }
        });

        fillData();
    }

    public void setDate(int dateId) {
        Bundle bundle = new Bundle();
        bundle.putInt("dateId", dateId);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void updateMedicalRecord(View view) {
        Toast.makeText(this, "Update should work", Toast.LENGTH_LONG).show();
        finish();
    }

    private void fillData(){
        getDisease().setText(medicalRecord.getDisease());
        getMedicine().setText(medicalRecord.getMedicine());
        getAllergic().setText(medicalRecord.getAllergic());
        getDate1().setText(medicalRecord.getDuration().substring(0,8));
        getDoctor().setText(medicalRecord.getDoctor());
        getContact().setText(medicalRecord.getContact());
        getDescription().setText(medicalRecord.getDescription());
    }

    //region Getters and Setters
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

    public EditText getDoctor() {
        return doctor;
    }

    public void setDoctor(EditText doctor) {
        this.doctor = doctor;
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

    public static EditText getDate1() {
        return date1;
    }

    public static void setDate1(EditText date1) {
        UpdateMedicalRecordActivity.date1 = date1;
    }

    public static EditText getDate2() {
        return date2;
    }

    public static void setDate2(EditText date2) {
        UpdateMedicalRecordActivity.date2 = date2;
    }
    //endregion
}
