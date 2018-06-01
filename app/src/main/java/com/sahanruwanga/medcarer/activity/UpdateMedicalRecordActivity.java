package com.sahanruwanga.medcarer.activity;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.DatePickerFragment;
import com.sahanruwanga.medcarer.app.MedicalRecord;
import com.sahanruwanga.medcarer.app.User;

public class UpdateMedicalRecordActivity extends AppCompatActivity {
    private MedicalRecord medicalRecord;
    private EditText medicine;
    private EditText disease;
    private EditText doctor;
    private EditText contact;
    private EditText note;
    private LinearLayout moreItemLayout;
    private Switch moreItemSwitch;
    private TextView date1;
    private TextView date2;
    private Spinner allergic;
    private Button updateBtn;
    private Button cancelBtn;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medical_record);

        // Create User object
        if(getUser() == null) {
            this.user = new User(this);
        }

        // Initiating widgets
        this.medicine = findViewById(R.id.updateMedicalRecordMedicine);
        this.disease = findViewById(R.id.updateMedicalRecordDisease);
        this.doctor = findViewById(R.id.updateMedicalRecordDoctor);
        this.contact = findViewById(R.id.updateMedicalRecordContact);
        this.note = findViewById(R.id.updateMedicalRecordNote);
        this.moreItemLayout = findViewById(R.id.moreItemLayout);
        this.moreItemSwitch = findViewById(R.id.moreSwitch);
        this.date1 = findViewById(R.id.updateMedicalRecordDate1);
        this.date2 = findViewById(R.id.updateMedicalRecordDate2);
        this.allergic = findViewById(R.id.updateMedicalRecordAllergic);
        this.updateBtn = findViewById(R.id.updateMedicalRecordUpdate);
        this.cancelBtn = findViewById(R.id.updateMedicalRecordCancel);

        // Set Adapter for Spinner
        ArrayAdapter<CharSequence> adapterAllergic = ArrayAdapter.createFromResource(this,
                R.array.allergic_array, android.R.layout.simple_spinner_item);
        adapterAllergic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getAllergic().setAdapter(adapterAllergic);

        // Switch check button change event
        getMoreItemSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    getMoreItemLayout().setVisibility(View.VISIBLE);
                else
                    getMoreItemLayout().setVisibility(View.GONE);
            }
        });

        // OnClick listeners for buttons
        getUpdateBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMedicalReocrd();
            }
        });

        getCancelBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // OnCick listeners for date Text Views
        getDate1().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(getDate1().getId());
            }
        });

        getDate2().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(getDate2().getId());
            }
        });

        this.medicalRecord = getIntent().getParcelableExtra(MedicalRecord.MEDICAL_RECORD);

        fillData();
    }

    public void setDate(int dateId) {
        Bundle bundle = new Bundle();
        bundle.putInt("dateId", dateId);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void updateMedicalReocrd(){
        String disease = getDisease().getText().toString().trim();
        String medicine = getMedicine().getText().toString().trim();
        String duration = getDate1().getText().toString().trim() + " - " + getDate2().getText().toString().trim();
        String allergic = getAllergic().getSelectedItem().toString().trim();
        String doctor = getDoctor().getText().toString().trim();
        String contact = getContact().getText().toString().trim();
        String description = getNote().getText().toString().trim();
        int syncStatus = getMedicalRecord().getSyncStatus();
        int statusType = getMedicalRecord().getStatusType();

        if(!disease.isEmpty() && !medicine.isEmpty() && !getDate1().getText().toString().isEmpty() &&
                !getDate2().getText().toString().isEmpty() && !allergic.isEmpty()){

            getUser().updateRecord(getMedicalRecord().getRecord_id(), disease, medicine, duration,
                    allergic, doctor, contact, description, syncStatus, statusType);

            MedicalRecord medicalRecord = new MedicalRecord();
            medicalRecord.setMedicine(medicine);
            medicalRecord.setDisease(disease);
            medicalRecord.setDuration(duration);
            medicalRecord.setAllergic(allergic);
            medicalRecord.setDoctor(doctor);
            medicalRecord.setContact(contact);
            medicalRecord.setDescription(description);

            Intent intent = new Intent();
            intent.putExtra(MedicalRecord.MEDICAL_RECORD, medicalRecord);
            setResult(1, intent);
            finish();

        } else{
            Toast.makeText(this, "Please enter required details!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Back Icon on toolbar
    public void backIconClick(View view) {
        onBackPressed();
    }

    // Fill current data in boxes
    private void fillData(){
        getDisease().setText(medicalRecord.getDisease());
        getMedicine().setText(medicalRecord.getMedicine());
        if(medicalRecord.getAllergic().equals("Yes"))
            getAllergic().setSelection(0);
        else
            getAllergic().setSelection(1);
        getDate1().setText(medicalRecord.getDuration().substring(0,12));
        getDate2().setText(medicalRecord.getDuration().substring(15));
        getDoctor().setText(medicalRecord.getDoctor());
        getContact().setText(medicalRecord.getContact());
        getNote().setText(medicalRecord.getDescription());
    }

    //region Getters and Setters
    public EditText getMedicine() {
        return medicine;
    }

    public void setMedicine(EditText medicine) {
        this.medicine = medicine;
    }

    public EditText getDisease() {
        return disease;
    }

    public void setDisease(EditText disease) {
        this.disease = disease;
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

    public EditText getNote() {
        return note;
    }

    public void setNote(EditText note) {
        this.note = note;
    }

    public LinearLayout getMoreItemLayout() {
        return moreItemLayout;
    }

    public void setMoreItemLayout(LinearLayout moreItemLayout) {
        this.moreItemLayout = moreItemLayout;
    }

    public Switch getMoreItemSwitch() {
        return moreItemSwitch;
    }

    public void setMoreItemSwitch(Switch moreItemSwitch) {
        this.moreItemSwitch = moreItemSwitch;
    }

    public TextView getDate1() {
        return date1;
    }

    public void setDate1(TextView date1) {
        this.date1 = date1;
    }

    public TextView getDate2() {
        return date2;
    }

    public void setDate2(TextView date2) {
        this.date2 = date2;
    }

    public Spinner getAllergic() {
        return allergic;
    }

    public void setAllergic(Spinner allergic) {
        this.allergic = allergic;
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

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //endregion
}
