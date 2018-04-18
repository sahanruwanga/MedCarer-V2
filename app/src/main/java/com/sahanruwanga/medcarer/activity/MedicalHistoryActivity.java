package com.sahanruwanga.medcarer.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.AppConfig;
import com.sahanruwanga.medcarer.app.AppController;
import com.sahanruwanga.medcarer.app.MedicalHistoryAdapter;
import com.sahanruwanga.medcarer.app.MedicalRecord;
import com.sahanruwanga.medcarer.app.PDFCreator;
import com.sahanruwanga.medcarer.app.User;
import com.sahanruwanga.medcarer.helper.NetworkStateChecker;
import com.sahanruwanga.medcarer.helper.SQLiteHandler;
import com.sahanruwanga.medcarer.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicalHistoryActivity extends AppCompatActivity {
    private static final String TAG = MedicalHistoryActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView toolBarText;
    private MaterialSearchView searchView;
    private ProgressDialog progressDialog;
    private SQLiteHandler sqLiteHandler;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private Menu menu;
    private RecyclerView.LayoutManager layoutManager;
    private MedicalHistoryAdapter medicalHistoryAdapter;

    public static final int SYNCED_WITH_SERVER = 1;
    public static final int NOT_SYNCED_WITH_SERVER = 0;

    //region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);

        // SQLiteHelper initialization
        this.sqLiteHandler = new SQLiteHandler(getApplicationContext());
        // Session manager
        this.sessionManager = new SessionManager(getApplicationContext());

        //RecyclerView and layout manager initialization
        this.recyclerView = findViewById(R.id.medicalHistoryRecyclerView);
        getRecyclerView().setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        getRecyclerView().setLayoutManager(layoutManager);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        //Toolbar creation
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(getToolbar());
        getToolbar().setLogo(R.drawable.ic_download);

        // TextView in toolbar
        this.setToolBarText((TextView)findViewById(R.id.toolBarText));

        //if(sqlite for medicalhistory doesn't exists netconnection needs to add new data)
        if(!getSessionManager().isMHCreated()){
            storeInSQLite(User.getUserId());
        }

        // Add data into RecyclerView
        showRecyclerView();

        //region Search Bar Functions
        setSearchView((MaterialSearchView) findViewById(R.id.searchViewMH));
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

    //region Show RecyclerView in Medical History
    private void showRecyclerView(){
        setMedicalHistoryAdapter(new MedicalHistoryAdapter(getSqLiteHandler().getMedicalRecords(),
                this, getRecyclerView()));
        getRecyclerView().setAdapter(getMedicalHistoryAdapter());
    }
    //endregion

    //region onCreateOptionMenu and onOptionItemSelected
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.menu = menu;
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem item = menu.findItem(R.id.itemSearch);
        getSearchView().setMenuItem(item);
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.savePDF) {
            savePdf();
        }else if(id == R.id.previewPDF){
            new PDFCreator(getSqLiteHandler().getMedicalRecords()).createPdf();
            openPdf();
        }else if(id == R.id.selectAllIcon){
            getMedicalHistoryAdapter().selectAll();
        }else if(id == R.id.deleteIcon){
            // To confirm the deletion
            openDialogBox();
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region Dialog Box
    // Open dialog box after pressing delete icon
    private void openDialogBox(){
        int selectedCount = getMedicalHistoryAdapter().getSelectingCount();
        String message = " record will be deleted.";
        if(selectedCount > 1)
            message = " records will be deleted.";
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(String.valueOf(selectedCount) + message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "DELETE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for(MedicalRecord medicalRecord : getMedicalHistoryAdapter().getSelectedRecords()) {
                            getSqLiteHandler().makeDeletedMedicalRecord(medicalRecord.getRecord_id(), NOT_SYNCED_WITH_SERVER);
                            showDefaultToolBar();
                            showRecyclerView();
                            deleteMedicalRecord(User.getUserId(), String.valueOf(medicalRecord.getRecord_id()));
                        }
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    //endregion

    //region Delete medical record from MySQL
    private void deleteMedicalRecord(final String userId, final String localRecordId){
        progressDialog.setMessage("Deleting Record ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_DELETE_MEDICAL_RECORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Medical Record Deleting: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        getSqLiteHandler().deleteMedicalRecord(Integer.parseInt(localRecordId));
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Deleting Error: " + error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to medical history url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userId);
                params.put("local_record_id", localRecordId);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    //endregion

    //region opendPdf and savePdf Functions
    private void openPdf(){
        String dir="/Medical_History";
        File file = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOCUMENTS)+dir+ "/MedicalHistory.pdf");

        if(file.exists()) {
//            Toast.makeText(getApplication(), file.toString() , Toast.LENGTH_LONG).show();
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
                Toast.makeText(this, "You don't have any PDF reader.\nPlease install one and try again!", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(getApplication(), "File path is incorrect." , Toast.LENGTH_LONG).show();
    }

    private void savePdf(){

    }

    //endregion

    //region Showing tool bars
    public void showDefaultToolBar() {
        getToolbar().getMenu().clear();
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem item = menu.findItem(R.id.itemSearch);
        getSearchView().setMenuItem(item);
        getMenuInflater().inflate(R.menu.home, menu);
        getToolBarText().setText("Medical History");
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
        if(getSearchView().isSearchOpen()){
            getSearchView().closeSearch();
        }else if (getMedicalHistoryAdapter().getSelectingCount() > 0){
            getMedicalHistoryAdapter().deseleceAll();
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
        finish();
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
                            String created_at = medicalRecord.getString(7);
                            String localRecordID = medicalRecord.getString(8);

                            getSqLiteHandler().addMedicalRecordFromMySQL(Integer.parseInt(localRecordID),
                                    disease, medicine, duration, allergic,
                                    doctor, contact, description, created_at, SYNCED_WITH_SERVER);
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
                    Toast.makeText(getApplicationContext(), "Please activate network access and Try again", Toast.LENGTH_LONG).show();
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

    public MedicalHistoryAdapter getMedicalHistoryAdapter() {
        return medicalHistoryAdapter;
    }

    public void setMedicalHistoryAdapter(MedicalHistoryAdapter medicalHistoryAdapter) {
        this.medicalHistoryAdapter = medicalHistoryAdapter;
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

    public TextView getToolBarText() {
        return toolBarText;
    }

    public void setToolBarText(TextView toolBarText) {
        this.toolBarText = toolBarText;
    }
    //endregion
}
