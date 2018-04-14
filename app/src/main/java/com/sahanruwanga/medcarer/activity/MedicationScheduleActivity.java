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
    private static final String TAG = MedicationScheduleActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView toolBarText;
    private MaterialSearchView searchView;
    private ProgressDialog progressDialog;
    private SQLiteHandler sqLiteHandler;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
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
        // Session manager
        this.sessionManager = new SessionManager(getApplicationContext());
        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // Method call to save data in SQLite if not exist
        if(!getSessionManager().isMedicationScheduleCreated()){
            storeInSQLite(User.getUserId());
        }

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

    private List<MedicationSchedule> getSch() {
        List<MedicationSchedule> medicationSchedules = new ArrayList<>();
        MedicationSchedule mds;
        for(int i=0; i<10; i++){
            mds = new MedicationSchedule();
            mds.setMedicine("Paracetmol");
            mds.setQuantity("2");
            mds.setNotifyTime("00:10:00");
            mds.setStartTime("08:00:00");
            medicationSchedules.add(mds);
        }
        return medicationSchedules;
    }

    // Show saved data in Recycler View
    private void showRecyclerView() {
        this.medicationScheduleAdapter = new MedicationScheduleAdapter(getSqLiteHandler().getMedicationScheduleDetails(),
                this, getRecyclerView());
        getRecyclerView().setAdapter(getMedicationScheduleAdapter());
    }

    //region Store data in SQLite database
    // Save data into SQLite database from MySQL database
    private void storeInSQLite(final String user_id){
        // Tag used to cancel the request
        String tag_string_req = "req_medication_schedule";

        getProgressDialog().setMessage("Retrieving data ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MEDICATION_SCHEDULE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Medication Schedule Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        getSessionManager().setMedicationScheduleCreated(true);

                        // Now store the user in SQLite
                        JSONArray schedules = jObj.getJSONArray("schedule");
                        for (int i = 0; i < schedules.length(); i++){
                            JSONArray medicationSchedule = jObj.getJSONArray(schedules.getString(i));
                            String medicine = medicationSchedule.getString(0);
                            String quantity = medicationSchedule.getString(1);
                            String start_time = medicationSchedule.getString(2);
                            String period = medicationSchedule.getString(3);
                            String notify_time = medicationSchedule.getString(4);
                            String created_at = medicationSchedule.getString(5);

                            getSqLiteHandler().addMedicationSchedule(Integer.parseInt(schedules.getString(i)),
                                    medicine, quantity, start_time, period, notify_time, created_at);
                        }

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Retrieving Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to medical history url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    //endregion

    public void openNewMedicationSchedule(View view) {
        Intent intent = new Intent(this, NewMedicationScheduleActivity.class);
        startActivity(intent);
        finish();
    }

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
