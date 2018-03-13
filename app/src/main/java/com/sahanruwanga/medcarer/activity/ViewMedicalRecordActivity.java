package com.sahanruwanga.medcarer.activity;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.MedicalRecord;
import com.sahanruwanga.medcarer.app.UpdateMedicalRecordFragment;

public class ViewMedicalRecordActivity extends AppCompatActivity {
    private MedicalRecord medicalRecord;
    private Toolbar toolbar;
    private TextView disease, medicine, allergic, duration, doctor, contact, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medical_record);

        medicalRecord = getIntent().getParcelableExtra("MedicalRecord");
        this.toolbar = findViewById(R.id.toolbarVieMedicalRecord);
        setSupportActionBar(toolbar);

        this.disease = findViewById(R.id.diseaseTextDetail);
        this.medicine = findViewById(R.id.medicineTextDetail);
        this.allergic = findViewById(R.id.allergicTextDetail);
        this.duration = findViewById(R.id.durationTextDetail);
        this.doctor = findViewById(R.id.doctorTextDetail);
        this.contact = findViewById(R.id.contactTextDetail);
        this.description = findViewById(R.id.descriptionTextDetail);

        disease.setText(medicalRecord.getDisease());
        medicine.setText(medicalRecord.getMedicine());
        allergic.setText(medicalRecord.getAllergic());
        duration.setText(medicalRecord.getDuration());
        doctor.setText(medicalRecord.getDoctor());
        contact.setText(medicalRecord.getContact());
        description.setText(medicalRecord.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.editIcon){
//            UpdateMedicalRecordFragment dialog = new UpdateMedicalRecordFragment();
//            dialog.show(getFragmentManager(), "");
            Intent intent = new Intent(this, UpdateMedicalRecordActivity.class);
            intent.putExtra("medicalRecord", medicalRecord);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
