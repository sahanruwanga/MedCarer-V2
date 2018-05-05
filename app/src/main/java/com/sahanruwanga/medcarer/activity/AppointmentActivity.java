package com.sahanruwanga.medcarer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.sahanruwanga.medcarer.app.Appointment;
import com.sahanruwanga.medcarer.app.AppointmentAdapter;
import com.sahanruwanga.medcarer.app.MedicationSchedule;
import com.sahanruwanga.medcarer.app.User;
import com.sahanruwanga.medcarer.helper.SQLiteHandler;
import com.sahanruwanga.medcarer.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AppointmentActivity extends AppCompatActivity {
    private static final String TAG = MedicalHistoryActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView toolBarText;
    private TextView emptyImage;
    private ImageView backIcon;
    private MaterialSearchView searchView;
    private ProgressDialog progressDialog;
    private SQLiteHandler sqLiteHandler;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private Menu menu;
    private RecyclerView.LayoutManager layoutManager;
    private AppointmentAdapter appointmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        this.emptyImage = findViewById(R.id.emptyMessage);

        // SQLiteHelper initialization
        this.setSqLiteHandler(new SQLiteHandler(getApplicationContext()));
        // Session manager
        this.setSessionManager(new SessionManager(getApplicationContext()));

        // Progress dialog
        this.setProgressDialog(new ProgressDialog(this));
        getProgressDialog().setCancelable(false);

        //RecyclerView and layoutmanagrer initialization
        setRecyclerView((RecyclerView)findViewById(R.id.appointmentRecyclerView));
        getRecyclerView().setHasFixedSize(true);
        setLayoutManager(new LinearLayoutManager(this));
        getRecyclerView().setLayoutManager(getLayoutManager());
        showRecyclerView();

        //Toolbar creation
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(getToolbar());

        this.backIcon = findViewById(R.id.backIconAppointment);

        // Toolbar Text
        this.toolBarText = findViewById(R.id.toolBarTextAppointment);

        showDefaultToolBar();

    }

    //region onBackPressed
    // Back press event
    @Override
    public void onBackPressed() {
        if (getAppointmentAdapter().getSelectingCount() > 0){
            getAppointmentAdapter().deseleceAll();
            showDefaultToolBar();
        }else
            super.onBackPressed();
    }
    //endregion

    // Back icon click in toolbar
    public void backIconClick(View view) {
        onBackPressed();
    }

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
            getAppointmentAdapter().selectAll();
        }else if(id == R.id.deleteIcon){
            for(Appointment appointment : getAppointmentAdapter().getSelectedAppointments()){
                Toast.makeText(this, "Schedule ID "+String.valueOf(appointment.getAppointmentId())
                        +" is deleted!", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region Show RecyclerView in Appointment
    private void showRecyclerView(){
        this.appointmentAdapter = new AppointmentAdapter(getSqLiteHandler().getAppointment(), this, getRecyclerView());
        getRecyclerView().setAdapter(getAppointmentAdapter());
    }
    //endregion

    public void showEmptyMessage(int visibility){
        getEmptyImage().setVisibility(visibility);
    }
    //region Showing tool bars
    public void showDefaultToolBar() {
        getToolbar().getMenu().clear();
        getToolBarText().setText("Appointment");
        getBackIcon().setVisibility(View.VISIBLE);
    }

    public void showDeletingToolBar() {
        getToolbar().getMenu().clear();
        getMenuInflater().inflate(R.menu.delete_all, menu);
        getToolBarText().setText("");
        getBackIcon().setVisibility(View.INVISIBLE);
    }
    //endregion

    //region Process Dialog activities
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
    //endregion

    //region Open Add Appointment Activity
    public void openAddAppointment(View view) {
        Intent intent = new Intent(this, AddAppointmentActivity.class);
        startActivity(intent);
        finish();
    }
    //endregion

    //region Getters and Setters
    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public MaterialSearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(MaterialSearchView searchView) {
        this.searchView = searchView;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public SQLiteHandler getSqLiteHandler() {
        return sqLiteHandler;
    }

    public void setSqLiteHandler(SQLiteHandler sqLiteHandler) {
        this.sqLiteHandler = sqLiteHandler;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public AppointmentAdapter getAppointmentAdapter() {
        return appointmentAdapter;
    }

    public void setAppointmentAdapter(AppointmentAdapter appointmentAdapter) {
        this.appointmentAdapter = appointmentAdapter;
    }

    public TextView getToolBarText() {
        return toolBarText;
    }

    public void setToolBarText(TextView toolBarText) {
        this.toolBarText = toolBarText;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public ImageView getBackIcon() {
        return backIcon;
    }

    public void setBackIcon(ImageView backIcon) {
        this.backIcon = backIcon;
    }

    public TextView getEmptyImage() {
        return emptyImage;
    }

    public void setEmptyImage(TextView emptyImage) {
        this.emptyImage = emptyImage;
    }
    //endregion
}
