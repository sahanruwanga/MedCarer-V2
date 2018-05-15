package com.sahanruwanga.medcarer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.AllergicMedicine;
import com.sahanruwanga.medcarer.app.User;

public class UpdateAllergicMedicineActivity extends AppCompatActivity {
    private AllergicMedicine allergicMedicine;
    private AutoCompleteTextView medicine;
    private EditText note;
    private Button updateBtn;
    private Button cancelBtn;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_allergic_medicine);

        // Create User Object
        this.user = new User(this);

        // Get Allergic Medicine object from Intent
        this.allergicMedicine = getIntent().getParcelableExtra(AllergicMedicine.ALLERGIC_MEDICINE);

        // Initializing all widgets
        this.medicine = findViewById(R.id.updateAllergicMedicine);
        this.note = findViewById(R.id.updateAllergicMedicineNote);
        this.updateBtn = findViewById(R.id.updateAllergicMedicineUpdate);
        this.cancelBtn = findViewById(R.id.cancelUpdateAllergicMedicine);

        // OnClick listeners for buttons
        getUpdateBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAllergicMedicine();
            }
        });

        getCancelBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Fill data from object to widgets
        filldata();
    }

    // Update Allergic Medicine
    private void updateAllergicMedicine(){
        String medicine = getMedicine().getText().toString().trim();
        String note = getNote().getText().toString().trim();
        if(!medicine.equals(getAllergicMedicine().getMedicineName()) || !note.equals(getAllergicMedicine().getDescription())){
            if (!medicine.isEmpty()){
                getUser().updateAllergicMedicine(getAllergicMedicine().getAllergicMedicineId(),
                        medicine, note, getAllergicMedicine().getSyncStatus(), getAllergicMedicine().getStatusType());
            }else{
                Toast.makeText(this, "Please fill Medicine field!", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    // Fill data
    private void filldata(){
        getMedicine().setText(getAllergicMedicine().getMedicineName());
        getNote().setText(getAllergicMedicine().getDescription());
    }

    // Back Icon click on toolbar
    public void backIconClickUpdateAllergicMedicine(View view) {
        onBackPressed();
    }

    //region Getters and Setters
    public AutoCompleteTextView getMedicine() {
        return medicine;
    }

    public void setMedicine(AutoCompleteTextView medicine) {
        this.medicine = medicine;
    }

    public EditText getNote() {
        return note;
    }

    public void setNote(EditText note) {
        this.note = note;
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

    public AllergicMedicine getAllergicMedicine() {
        return allergicMedicine;
    }

    public void setAllergicMedicine(AllergicMedicine allergicMedicine) {
        this.allergicMedicine = allergicMedicine;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //endregion
}
