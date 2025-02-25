package com.sahanruwanga.medcarer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.AllergicMedicine;
import com.sahanruwanga.medcarer.app.MedicalRecord;
import com.sahanruwanga.medcarer.app.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewAllergicMedicineActivity extends AppCompatActivity {
    private AutoCompleteTextView medicine;
    private EditText description;
    private Button saveBtn;
    private Button cancelBtn;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_allergic_medicine);

        // Create User object
        if(getUser() == null)
            this.setUser(new User(this));

        // Initializing widgets
        this.medicine = findViewById(R.id.newAllergicMedicine);
        ArrayList<String> medicines = getMedicnieNames();
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, medicines);
        adapterMonth.setDropDownViewResource(R.layout.layout_schedule_list_item);
        getMedicine().setAdapter(adapterMonth);

        this.description = findViewById(R.id.newAllergicMedicineNote);
        this.saveBtn = findViewById(R.id.saveNewAllergicMedicine);
        this.cancelBtn = findViewById(R.id.cancelNewAllergicMedicine);

        // OnCLick listeners for buttons
        getSaveBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMedicine();
            }
        });

        getCancelBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private  ArrayList<String> getMedicnieNames(){
        ArrayList<String> medicines = new ArrayList<>();
        for(MedicalRecord medicalRecord : getUser().getMedicalRecords()){
            medicines.add(medicalRecord.getMedicine());
        }
        return medicines;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Save function in toolbar
    public void saveMedicine() {
        String medicine = getMedicine().getText().toString().trim();
        String description = getDescription().getText().toString().trim();

        // Get current date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdAt = dateFormat.format(new Date());

        if(!medicine.equals("")) {
            long id = getUser().saveNewAllergicMedicine(medicine, description, createdAt);
            AllergicMedicine allergicMedicine = new AllergicMedicine();
            allergicMedicine.setAllergicMedicineId((int) id);
            allergicMedicine.setMedicineName(medicine);
            allergicMedicine.setDescription(description);
            Intent intent = new Intent();
            intent.putExtra(AllergicMedicine.ALLERGIC_MEDICINE, allergicMedicine);
            setResult(1, intent);
            finish();
        } else
            Toast.makeText(this, "Please add required fields", Toast.LENGTH_SHORT).show();
    }

    // Back Icon click on toolbar
    public void backIconClick(View view) {
        onBackPressed();
    }


    //region Getters and Setters
    public AutoCompleteTextView getMedicine() {
        return medicine;
    }

    public void setMedicine(AutoCompleteTextView medicine) {
        this.medicine = medicine;
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

    //endregion
}
