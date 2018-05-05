package com.sahanruwanga.medcarer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewAllergicMedicineActivity extends AppCompatActivity {
    private AutoCompleteTextView medicine;
    private EditText description;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_allergic_medicine);

        // Create User object
        if(getUser() == null)
            this.user = new User(this);

        // Initializing edit texts
        this.medicine = findViewById(R.id.newAllergicMedicine);
        String[] medicines = {"Panadol", "Paracetamol", "Panadol1"};
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, medicines);
        adapterMonth.setDropDownViewResource(R.layout.autocomplete_textview_style);
        getMedicine().setAdapter(adapterMonth);
        this.description = findViewById(R.id.newAllergicMedicineNote);
    }

    // Cancel function in toolbar
    public void cancelSaving(View view) {
        onBackPressed();
    }

    // Save function in toolbar
    public void saveMedicine(View view) {
        String medicine = getMedicine().getText().toString().trim();
        String description = getDescription().getText().toString().trim();

        // Get current date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdAt = dateFormat.format(new Date());

        if(!medicine.equals(""))
            getUser().saveNewAllergicMedicine(medicine, description, createdAt);
        else
            Toast.makeText(this, "Please add required fields", Toast.LENGTH_SHORT).show();
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
    //endregion
}
