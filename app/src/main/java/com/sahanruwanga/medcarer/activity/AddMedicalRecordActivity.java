package com.sahanruwanga.medcarer.activity;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.DatePickerFragment;
import com.sahanruwanga.medcarer.app.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMedicalRecordActivity extends AppCompatActivity {
    private EditText disease;
    private EditText medicine;
    private EditText allergic;
    private EditText date1;
    private EditText date2;
    private EditText doctorName;
    private EditText contact;
    private EditText description;
    private Toolbar toolbar;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_record);

        // Create User object
        if(getUser() == null) {
            this.user = new User(this);
        }

        // ToolBar initializing
        this.toolbar = findViewById(R.id.toolbarAddMedicalRecord);
        setSupportActionBar(getToolbar());

        // Define all Edit Texts
        this.disease = findViewById(R.id.disease);
        this.medicine = findViewById(R.id.medicine);
        this.allergic = findViewById(R.id.allergic);
        this.date1 = findViewById(R.id.date1);
        this.date2 = findViewById(R.id.date2);
        this.doctorName = findViewById(R.id.doctor);
        this.contact = findViewById(R.id.contact);
        this.description = findViewById(R.id.description);

        // Set onFocusListener to open up calender in date text
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

    }

    // Cancel function in toolbar
    public void cancelUpdating(View view) {
        onBackPressed();
    }

    // Save function in toolbar
    public void saveRecord(View view){
        String disease = getDisease().getText().toString().trim();
        String medicine = getMedicine().getText().toString().trim();
        String duration = getDate1().getText().toString().trim() + " - " + getDate2().getText().toString().trim();
        String allergic = getAllergic().getText().toString().trim();
        String doctor = getDoctorName().getText().toString().trim();
        String contact = getContact().getText().toString().trim();
        String description = getDescription().getText().toString().trim();

        // Get current date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdAt = dateFormat.format(new Date());

        if(!disease.isEmpty() && !medicine.isEmpty() && !getDate1().getText().toString().isEmpty() &&
                !getDate2().getText().toString().isEmpty() && !allergic.isEmpty()){

            clearAll();
            getUser().saveNewRecord(disease, medicine, duration, allergic, doctor, contact, description, createdAt);
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
        getAllergic().setText("");
        getDoctorName().setText("");
        getContact().setText("");
        getDescription().setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MedicalHistoryActivity.class);
        startActivity(intent);
        finish();
    }

    // Open calender in DialogFragment
    public void setDate(int dateId) {
        Bundle bundle = new Bundle();
        bundle.putInt("dateId", dateId);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //region Getters and Setters
    public EditText getDate2() {
        return date2;
    }

    public void setDate2(EditText date2) {
        this.date2 = date2;
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

    public EditText getDate1() {
        return date1;
    }

    public void setDate1(EditText date1) {
        this.date1 = date1;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //endregion
}
