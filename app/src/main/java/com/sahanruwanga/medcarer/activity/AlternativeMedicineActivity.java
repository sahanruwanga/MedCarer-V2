package com.sahanruwanga.medcarer.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sahanruwanga.medcarer.R;

import java.util.Calendar;

public class AlternativeMedicineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternative_medicine);
    }

    // Back Icon click on toolbar
    public void backIconClick(View view) {
        onBackPressed();
    }
}
