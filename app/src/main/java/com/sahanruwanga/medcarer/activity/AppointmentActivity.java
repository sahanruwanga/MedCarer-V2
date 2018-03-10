package com.sahanruwanga.medcarer.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.AppConfig;
import com.sahanruwanga.medcarer.app.AppController;
import com.sahanruwanga.medcarer.app.AppointmentAdapter;
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
    private MaterialSearchView searchView;
    private ProgressDialog progressDialog;
    private SQLiteHandler sqLiteHandler;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AppointmentAdapter appointmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        // SQLiteHelper initialization
        this.setSqLiteHandler(new SQLiteHandler(getApplicationContext()));
        // Session manager
        this.setSessionManager(new SessionManager(getApplicationContext()));

        // Progress dialog
        this.setProgressDialog(new ProgressDialog(this));
        getProgressDialog().setCancelable(false);

        //if(sqlite for medicalhistory doesn't exists netconnection needs to add new data)
        if(!getSessionManager().isAppointmentCreated()){
            storeInSQLite(User.getUserId());
        }

        //RecyclerView and layoutmanagrer initialization
        setRecyclerView((RecyclerView)findViewById(R.id.appointmentRecyclerView));
        getRecyclerView().setHasFixedSize(true);
        setLayoutManager(new LinearLayoutManager(this));
        getRecyclerView().setLayoutManager(getLayoutManager());
        showRecyclerView();

        //Toolbar creation
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(getToolbar());
        showDefaultToolBar();

        //region Search Bar Functions
        setSearchView((MaterialSearchView) findViewById(R.id.searchViewAllergic));
        getSearchView().setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }
            @Override
            public void onSearchViewClosed() {
            }
        });
        getSearchView().setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //endregion
    }

    //region Show RecyclerView in Appointment
    private void showRecyclerView(){
        this.appointmentAdapter = new AppointmentAdapter(getSqLiteHandler().getAppointmentDetails(), this, getRecyclerView());
        getRecyclerView().setAdapter(getAppointmentAdapter());
    }
    //endregion

    //region Showing tool bars
    public void showDefaultToolBar() {
        getSupportActionBar().setTitle(R.string.appointment);
        getToolbar().setTitleTextColor(Color.parseColor("#000000"));
        getToolbar().setLogo(R.drawable.ic_download);
    }

    public void showDeletingToolBar() {
        getSupportActionBar().setTitle("");
        getToolbar().setLogo(null);
        getToolbar().getMenu().clear();
    }
    //endregion

    //region Store data in SQLite database
    // Save data into SQLite database from MySQL database
    private void storeInSQLite(final String user_id){
        // Tag used to cancel the request
        String tag_string_req = "req_appointment";

        progressDialog.setMessage("Retrieving data ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_APPOINTMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Appointment Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        getSessionManager().setAppointmentCreated(true);

                        // Now store appointments in SQLite
                        JSONArray appointments = jObj.getJSONArray("appointments");
                        for (int i=0; i<appointments.length();i++){
                            JSONArray appointment = jObj.getJSONArray(appointments.getString(i));
                            String reason = appointment.getString(0);
                            String date = appointment.getString(1);
                            String time = appointment.getString(2);
                            String venue = appointment.getString(3);
                            String doctor = appointment.getString(4);
                            String clinicContact = appointment.getString(5);
                            String notifyTime = appointment.getString(6);

                            getSqLiteHandler().addAppointment(Integer.parseInt(appointments.getString(i)), reason, date, time, venue, doctor, clinicContact, notifyTime);
                        }

                    } else {
                        // Error in fetching. Get the error message
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
                // Posting parameters to appointment url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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

    public void openAddAppointment(View view) {
    }

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
    //endregion
}
