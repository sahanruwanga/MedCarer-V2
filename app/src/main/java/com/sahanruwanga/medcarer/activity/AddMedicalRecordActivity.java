package com.sahanruwanga.medcarer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.sahanruwanga.medcarer.app.AllergicMedicine;
import com.sahanruwanga.medcarer.app.DatePickerFragment;
import com.sahanruwanga.medcarer.app.MedicalRecord;
import com.sahanruwanga.medcarer.app.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMedicalRecordActivity extends AppCompatActivity{
    private EditText medicine;
    private EditText disease;
    private EditText doctor;
    private EditText contact;
    private EditText note;
    private LinearLayout moreItemLayout;
    private Switch moreItemSwitch;
    private TextView date1;
    private TextView date2;
    private Button saveBtn;
    private Button cancelBtn;
    private Toolbar toolbar;

    private User user;

    private static String NOT_ALLERGIC = "No";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_record);

        // Create User object
        if(getUser() == null) {
            this.setUser(new User(this));
        }

        // ToolBar initializing
        this.toolbar = findViewById(R.id.toolbarAddMedicalRecord);
        setSupportActionBar(getToolbar());

        // Initiating widgets
        this.medicine = findViewById(R.id.addMedicalRecordMedicine);
        this.disease = findViewById(R.id.addMedicalRecordDisease);
        this.doctor = findViewById(R.id.addMedicalRecordDoctor);
        this.contact = findViewById(R.id.addMedicalRecordContact);
        this.note = findViewById(R.id.addMedicalRecordNote);
        this.moreItemLayout = findViewById(R.id.moreItemLayoutAddMR);
        this.moreItemSwitch = findViewById(R.id.moreSwitchAddMR);
        this.date1 = findViewById(R.id.addMedicalRecordDate1);
        this.date2 = findViewById(R.id.addMedicalRecordDate2);
        this.saveBtn = findViewById(R.id.addMedicalRecordUpdate);
        this.cancelBtn = findViewById(R.id.addMedicalRecordCancel);

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

        getMedicine().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    checkAllergic();
                }else{
                    getMedicine().setTextColor(Color.parseColor("#22d3ff"));
                    getMedicine().setSelectAllOnFocus(true);
                }
            }
        });

        // OnClick listeners for buttons
        getSaveBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRecord();
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

    }

    private void checkAllergic(){
        for (AllergicMedicine allergicMedicine : getUser().getAllergicMedicines()){
            if(getMedicine().getText().toString().trim().equals(allergicMedicine.getMedicineName())){
                getMedicine().setTextColor(Color.parseColor("#ec5a68"));
                break;
            }
        }
    }

    // Save function in toolbar
    public void saveRecord(){
        String disease = getDisease().getText().toString().trim();
        String medicine = getMedicine().getText().toString().trim();
        String duration = getDate1().getText().toString().trim() + " - " + getDate2().getText().toString().trim();
        String doctor = getDoctor().getText().toString().trim();
        String contact = getContact().getText().toString().trim();
        String description = getNote().getText().toString().trim();

        // Get current date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdAt = dateFormat.format(new Date());

        if(!disease.isEmpty() && !medicine.isEmpty() && !getDate1().getText().toString().isEmpty() &&
                !getDate2().getText().toString().isEmpty()){

            clearAll();
            long localId = getUser().saveNewRecord(disease, medicine, duration, NOT_ALLERGIC, doctor, contact, description, createdAt);

            Intent intent = new Intent();
            intent.putExtra(MedicalRecord.MEDICAL_RECORD, new MedicalRecord(Integer.parseInt(String.valueOf(localId)),
                    disease, medicine, duration, NOT_ALLERGIC, doctor, contact, description));
            setResult(1, intent);
            finish();

        }else{
            Toast.makeText(this, "Please enter required details!", Toast.LENGTH_LONG).show();
        }
    }

    // Clear all text in Text Boxes
    public void clearAll(){
        getDisease().setText("");
        getMedicine().setText("");
        getDate1().setText("");
        getDate2().setText("");
        getDoctor().setText("");
        getContact().setText("");
        getNote().setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Open calender in DialogFragment
    public void setDate(int dateId) {
        Bundle bundle = new Bundle();
        bundle.putInt("dateId", dateId);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Back Icon click on TooBar
    public void backIconClick(View view) {
        onBackPressed();
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

    public Button getSaveBtn() {
        return saveBtn;
    }

    public void setSaveBtn(Button saveBtn) {
        this.saveBtn = saveBtn;
    }

    public Button getCancelBtn() {
        return cancelBtn;
    }

    public void setCancelBtn(Button cancelBtn) {
        this.cancelBtn = cancelBtn;
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
