package com.sahanruwanga.medcarer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.AppConfig;
import com.sahanruwanga.medcarer.app.AppController;
import com.sahanruwanga.medcarer.app.MedicationSchedule;
import com.sahanruwanga.medcarer.app.MedicationScheduleAdapter;
import com.sahanruwanga.medcarer.app.User;
import com.sahanruwanga.medcarer.helper.SQLiteHandler;
import com.sahanruwanga.medcarer.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicationScheduleActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolBarText;
    private MaterialSearchView searchView;
    private RecyclerView recyclerView;

    private SQLiteHandler sqLiteHandler;
    private Menu menu;
    private RecyclerView.LayoutManager layoutManager;
    private MedicationScheduleAdapter medicationScheduleAdapter;

    //region OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_schedule);

        // SQLiteHelper initialization
        this.sqLiteHandler = new SQLiteHandler(getApplicationContext());

        //RecyclerView and layout manager initialization
        this.recyclerView = findViewById(R.id.medicationScheduleRecyclerView);
        getRecyclerView().setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        getRecyclerView().setLayoutManager(layoutManager);

        // Add data into RecyclerView
        showRecyclerView();

        // TextView in toolbar
        this.setToolBarText((TextView)findViewById(R.id.toolBarTextMedicationSchedule));

        //Toolbar creation
        this.toolbar = findViewById(R.id.toolbarMedicationSchedule);
        setSupportActionBar(getToolbar());
    }
    //endregion

    //region onCreateOptionMenu and onOptionItemSelected
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.selectAllIcon){
//            getMedicationScheduleAdapter().selectAll();
        }else if(id == R.id.deleteIcon){
//            for(MedicationSchedule medicationSchedule : getMedicationScheduleAdapter().getSelectedSchedules()){
//                Toast.makeText(this, "Schedule ID "+String.valueOf(medicationSchedule.getScheduleId())
//                        +" is deleted!", Toast.LENGTH_LONG).show();
//            }
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region Showing tool bars
    public void showDefaultToolBar() {
        getToolbar().getMenu().clear();
        getToolBarText().setText("Medication Schedule");
        getToolbar().setLogo(R.drawable.ic_download);
    }

    public void showDeletingToolBar() {
        getToolbar().getMenu().clear();
        getMenuInflater().inflate(R.menu.delete_all, menu);
        getToolBarText().setText("");
        getToolbar().setLogo(null);
    }
    //endregion

    //region onBackPressed
    // Back press event
    @Override
    public void onBackPressed() {
//        if (getMedicationScheduleAdapter().getSelectingCount() > 0){
//            getMedicationScheduleAdapter().deseleceAll();
//            showDefaultToolBar();
//        }else
            super.onBackPressed();
    }
    //endregion

    // Show saved data in Recycler View
    private void showRecyclerView() {
        this.medicationScheduleAdapter = new MedicationScheduleAdapter(getSqLiteHandler().getMedicationSchedule(),
                this, getRecyclerView());
        getRecyclerView().setAdapter(getMedicationScheduleAdapter());
    }

    public void openNewMedicationSchedule(View view) {
        Intent intent = new Intent(this, NewMedicationScheduleActivity.class);
        startActivity(intent);
        finish();
    }

    // Back Icon click on toolbar
    public void backIconClick(View view) {
        onBackPressed();
    }

    //region Getters and Setters
    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public TextView getToolBarText() {
        return toolBarText;
    }

    public void setToolBarText(TextView toolBarText) {
        this.toolBarText = toolBarText;
    }

    public MaterialSearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(MaterialSearchView searchView) {
        this.searchView = searchView;
    }

    public SQLiteHandler getSqLiteHandler() {
        return sqLiteHandler;
    }

    public void setSqLiteHandler(SQLiteHandler sqLiteHandler) {
        this.sqLiteHandler = sqLiteHandler;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public MedicationScheduleAdapter getMedicationScheduleAdapter() {
        return medicationScheduleAdapter;
    }

    public void setMedicationScheduleAdapter(MedicationScheduleAdapter medicationScheduleAdapter) {
        this.medicationScheduleAdapter = medicationScheduleAdapter;
    }
    //endregion
}
