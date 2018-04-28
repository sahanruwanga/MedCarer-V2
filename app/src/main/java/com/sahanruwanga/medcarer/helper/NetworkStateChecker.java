package com.sahanruwanga.medcarer.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sahanruwanga.medcarer.app.AppConfig;
import com.sahanruwanga.medcarer.app.AppController;
import com.sahanruwanga.medcarer.app.MedicalRecord;
import com.sahanruwanga.medcarer.app.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// To check the availability of the network
public class NetworkStateChecker extends BroadcastReceiver {

    //context and database helper object
    private Context context;
    private SQLiteHandler sqLiteHandler;

    public static final int SYNCED_WITH_SERVER = 1;
    public static final int NOT_SYNCED_WITH_SERVER = 0;

    public static final String DATA_SYNCED_BROADCAST = "dataSynced";

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        sqLiteHandler = new SQLiteHandler(context);

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        // If there is a network connection
        if (activeNetwork != null) {
            // If connected to wifi or mobile data
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                if(isUpdateRequired())
                    openDialogBox();
            }
        }
    }

    //region Dialog Box for Choosing update details
    // Open dialog box when network connection is available
    private void openDialogBox(){
        String title = "Network Available!";
        String message = "Update all details!";
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "UPDATE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                      // Getting all the un-synced saved medical records
                        List<MedicalRecord> saveddMedicalRecords = sqLiteHandler.getUnsyncedSavedMedicalRecords();
                        for(MedicalRecord medicalRecord : saveddMedicalRecords){
                            saveMedicalRecord(medicalRecord);
                        }

                        // Getting all the un-synced updated medical records
                        List<MedicalRecord> updatedMedicalRecords = sqLiteHandler.getUnsyncedUpdatedMedicalRecords();
                        for(MedicalRecord medicalRecord : updatedMedicalRecords) {
                            updateMedicalRecord(medicalRecord);
                        }

                        // Getting all the un-synced deleted medical records
                        List<MedicalRecord> deletedMedicalRecords = sqLiteHandler.getUnsyncedDeletedMedicalRecords();
                        for(MedicalRecord medicalRecord : deletedMedicalRecords){
                            deleteMedicalRecord(String.valueOf(medicalRecord.getRecord_id()));

                        }
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                "LATER",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }
    //endregion

    private boolean isUpdateRequired(){
        if(sqLiteHandler.getUnsyncedSavedMedicalRecords().size() == 0 &&
                sqLiteHandler.getUnsyncedUpdatedMedicalRecords().size() == 0 &&
                sqLiteHandler.getUnsyncedDeletedMedicalRecords().size() == 0)
            return false;
        else
            return true;
    }

    private void saveMedicalRecord(final MedicalRecord medicalRecord) {
        String tag_string_req = "req_insert_medical_record";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_MEDICAL_RECORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                // Updating the status in SQLite
                                sqLiteHandler.updateSyncStatus(medicalRecord.getRecord_id(), SYNCED_WITH_SERVER);

                                // Sending the broadcast to refresh the list
                                getContext().sendBroadcast(new Intent(DATA_SYNCED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", User.getUserId());
                params.put("local_record_id", String.valueOf(medicalRecord.getRecord_id()));
                params.put("disease", medicalRecord.getDisease());
                params.put("medicine", medicalRecord.getMedicine());
                params.put("duration", medicalRecord.getDuration());
                params.put("allergic", medicalRecord.getAllergic());
                params.put("doctor", medicalRecord.getDoctor());
                params.put("contact", medicalRecord.getContact());
                params.put("description", medicalRecord.getDescription());
                params.put("created_at", medicalRecord.getCreatedAt());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void deleteMedicalRecord(final String localRecordId){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_MEDICAL_RECORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        sqLiteHandler.deleteMedicalRecord(Integer.parseInt(localRecordId));
                        // Sending the broadcast to refresh the list
                        getContext().sendBroadcast(new Intent(DATA_SYNCED_BROADCAST));
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to medical history url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", User.getUserId());
                params.put("local_record_id", localRecordId);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void updateMedicalRecord(final MedicalRecord medicalRecord){

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATAE_MEDICAL_RECORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // Updating the status in SQLite
                        sqLiteHandler.updateSyncStatus(medicalRecord.getRecord_id(), SYNCED_WITH_SERVER);

                        // Sending the broadcast to refresh the list
                        getContext().sendBroadcast(new Intent(DATA_SYNCED_BROADCAST));
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", User.getUserId());
                params.put("local_record_id", String.valueOf(medicalRecord.getRecord_id()));
                params.put("disease", medicalRecord.getDisease());
                params.put("medicine", medicalRecord.getMedicine());
                params.put("duration", medicalRecord.getDuration());
                params.put("allergic", medicalRecord.getAllergic());
                params.put("doctor", medicalRecord.getDoctor());
                params.put("contact", medicalRecord.getContact());
                params.put("description", medicalRecord.getDescription());
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
