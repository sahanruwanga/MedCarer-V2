package com.sahanruwanga.medcarer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sahanruwanga.medcarer.R;

public class MedicationScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_schedule);
    }

    public void openNewMedicationSchedule(View view) {
        Intent intent = new Intent(this, NewMedicationScheduleActivity.class);
        startActivity(intent);
        finish();
    }
}
