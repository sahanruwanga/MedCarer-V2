package com.sahanruwanga.medcarer.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.MedicalRecord;

public class ViewMedicalRecordActivity extends AppCompatActivity {
    private MedicalRecord medicalRecord;
    private Toolbar toolbar;
    private TextView disease;
    private TextView medicine;
    private TextView allergic;
    private TextView duration;
    private TextView doctor;
    private TextView contact;
    private TextView description;
    private ImageView callIcon;
    private ImageView messageIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medical_record);

        // Get medical record object from intent
        this.medicalRecord = getIntent().getParcelableExtra(MedicalRecord.MEDICAL_RECORD);

        // Initialize toolbar
        this.toolbar = findViewById(R.id.toolbarVieMedicalRecord);
        setSupportActionBar(toolbar);

        // Initialize text views
        this.disease = findViewById(R.id.diseaseTextDetail);
        this.medicine = findViewById(R.id.medicineTextDetail);
        this.allergic = findViewById(R.id.allergicTextDetail);
        this.duration = findViewById(R.id.durationTextDetail);
        this.doctor = findViewById(R.id.doctorTextDetail);
        this.contact = findViewById(R.id.contactTextDetail);
        this.description = findViewById(R.id.descriptionTextDetail);
        this.callIcon = findViewById(R.id.callIconViewRecord);
        this.messageIcon = findViewById(R.id.messageIconViewRecord);

        // Set text in text views
        disease.setText(getMedicalRecord().getDisease());
        medicine.setText(getMedicalRecord().getMedicine());
        allergic.setText(getMedicalRecord().getAllergic());
        duration.setText(getMedicalRecord().getDuration());
        doctor.setText(getMedicalRecord().getDoctor());
        contact.setText(getMedicalRecord().getContact());
        if(getMedicalRecord().getContact().isEmpty()){
            getCallIcon().setVisibility(View.GONE);
            getMessageIcon().setVisibility(View.GONE);
        }else {
            getCallIcon().setVisibility(View.VISIBLE);
            getMessageIcon().setVisibility(View.VISIBLE);
        }
        description.setText(getMedicalRecord().getDescription());
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
            Intent intent = new Intent(this, UpdateMedicalRecordActivity.class);
            intent.putExtra(MedicalRecord.MEDICAL_RECORD, getMedicalRecord());
            startActivityForResult(intent, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(2);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            MedicalRecord medicalRecord = data.getParcelableExtra(MedicalRecord.MEDICAL_RECORD);
            if(medicalRecord != null) {
                getMedicine().setText(medicalRecord.getMedicine());
                getDisease().setText(medicalRecord.getDisease());
                getAllergic().setText(medicalRecord.getAllergic());
                getDuration().setText(medicalRecord.getDuration());
                getDoctor().setText(medicalRecord.getDoctor());
                getContact().setText(medicalRecord.getContact());
                getDescription().setText(medicalRecord.getDescription());
            }
        }
    }

    // Back icon click on toolbar
    public void backIconClick(View view) {
        onBackPressed();
    }

    // Dial the Number
    public void dialNumber(View view) {
        Uri number = Uri.parse("tel:" + getContact().getText().toString().trim());
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    // Message to the number
    public void messageNumber(View view) {
        Intent messageIntent = new Intent(Intent.ACTION_SENDTO);
        messageIntent.setData(Uri.parse("smsto:" + Uri.encode(getContact().getText().toString().trim())));
        startActivity(messageIntent);
    }

    //region Getters and Setters
    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public TextView getDisease() {
        return disease;
    }

    public void setDisease(TextView disease) {
        this.disease = disease;
    }

    public TextView getMedicine() {
        return medicine;
    }

    public void setMedicine(TextView medicine) {
        this.medicine = medicine;
    }

    public TextView getAllergic() {
        return allergic;
    }

    public void setAllergic(TextView allergic) {
        this.allergic = allergic;
    }

    public TextView getDuration() {
        return duration;
    }

    public void setDuration(TextView duration) {
        this.duration = duration;
    }

    public TextView getDoctor() {
        return doctor;
    }

    public void setDoctor(TextView doctor) {
        this.doctor = doctor;
    }

    public TextView getContact() {
        return contact;
    }

    public void setContact(TextView contact) {
        this.contact = contact;
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }

    public ImageView getCallIcon() {
        return callIcon;
    }

    public void setCallIcon(ImageView callIcon) {
        this.callIcon = callIcon;
    }

    public ImageView getMessageIcon() {
        return messageIcon;
    }

    public void setMessageIcon(ImageView messageIcon) {
        this.messageIcon = messageIcon;
    }
    //endregion

    public static class UpdateAllergicMedicineActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_update_allergic_medicine);
        }
    }
}
