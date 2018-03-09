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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.AppConfig;
import com.sahanruwanga.medcarer.app.AppController;
import com.sahanruwanga.medcarer.app.RecyclerViewAdapter;
import com.sahanruwanga.medcarer.app.User;
import com.sahanruwanga.medcarer.helper.SQLiteHandler;
import com.sahanruwanga.medcarer.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MedicalHistoryActivity extends AppCompatActivity {
    private static final String TAG = MedicalHistoryActivity.class.getSimpleName();
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private ProgressDialog progressDialog;
    private SQLiteHandler sqLiteHandler;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;

    //region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);

        // SQLiteHelper initialization
        setSqLiteHandler(new SQLiteHandler(getApplicationContext()));
        // Session manager
        setSessionManager(new SessionManager(getApplicationContext()));

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        //if(sqlite for medicalhistory doesn't exists netconnection needs to add new data)
        if(!getSessionManager().isMHCreated()){
            storeInSQLite(User.getUserId());
            Toast.makeText(this, "Working", Toast.LENGTH_LONG).show();
        }

        Toast.makeText(this, "RecyclerView is Working", Toast.LENGTH_LONG).show();
        //RecyclerView and layoutmanagrer initialization
        setRecyclerView((RecyclerView)findViewById(R.id.allergicRecyclerView));
        getRecyclerView().setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        getRecyclerView().setLayoutManager(layoutManager);
        showRecyclerView();

        //Toolbar creation
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(getToolbar());
        showDefaultToolBar();

        //region Search Bar Functions
        setSearchView((MaterialSearchView) findViewById(R.id.searchViewMM));
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
    //endregion

    //region Call to show RecyclerView in Medical History
    private void showRecyclerView(){
        setRecyclerViewAdapter(new RecyclerViewAdapter(getSqLiteHandler().getMedicalRecords(), this, getRecyclerView()));
        getRecyclerView().setAdapter(getRecyclerViewAdapter());
    }
    //endregion

    //region onCreateOptionMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem item = menu.findItem(R.id.itemSearch);
        getSearchView().setMenuItem(item);
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    //endregion

    //region Showing tool bars
    public void showDefaultToolBar() {
        getSupportActionBar().setTitle(R.string.medical_history);
        getToolbar().setTitleTextColor(Color.parseColor("#000000"));
        getToolbar().setLogo(R.drawable.ic_download);
    }

    public void showDeletingToolBar() {
        getSupportActionBar().setTitle("");
        getToolbar().setLogo(null);
        getToolbar().getMenu().clear();
    }
    //endregion

    //region onBackPressed
    // Back press event
    @Override
    public void onBackPressed() {
        if(getSearchView().isSearchOpen()){
            getSearchView().closeSearch();
        }else if (getRecyclerViewAdapter().getSelectingCount() > 0){
            getRecyclerViewAdapter().deseleceAll();
            showDefaultToolBar();
        }else
            super.onBackPressed();
    }
    //endregion

    //region open Add Medical Record activity
    // Open new activity to add new medical record
    public void openAddMedicalRecord(View view){
        Intent intent = new Intent(this, AddMedicalRecordActivity.class);
        startActivity(intent);
    }
    //endregion

    //region Save and Share Medical History
    public void saveRecord(View view){

    }
    //endregion

    //region Store data in SQLite database
    // Save data into SQLite database from MySQL database
    private void storeInSQLite(final String user_id){
        // Tag used to cancel the request
        String tag_string_req = "req_medical_history";

        progressDialog.setMessage("Retrieving data ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_MEDICAL_HISTORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Medical History Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        getSessionManager().setMHCreated(true);

                        // Now store the user in SQLite
                        JSONArray records= jObj.getJSONArray("records");
                        for (int i=0; i<records.length();i++){
                            JSONArray medicalRecord = jObj.getJSONArray(records.getString(i));
                            String disease = medicalRecord.getString(0);
                            String medicine = medicalRecord.getString(1);
                            String duration = medicalRecord.getString(2);
                            String allergic = medicalRecord.getString(3);
                            String doctor = medicalRecord.getString(4);
                            String contact = medicalRecord.getString(5);
                            String description = medicalRecord.getString(6);

                            getSqLiteHandler().addMedicalRecord(Integer.parseInt(records.getString(i)), disease, medicine, duration, allergic, doctor, contact, description);
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

    public MaterialSearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(MaterialSearchView searchView) {
        this.searchView = searchView;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public RecyclerViewAdapter getRecyclerViewAdapter() {
        return recyclerViewAdapter;
    }

    public void setRecyclerViewAdapter(RecyclerViewAdapter recyclerViewAdapter) {
        this.recyclerViewAdapter = recyclerViewAdapter;
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
    //endregion
}
