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
import com.sahanruwanga.medcarer.app.AllergicMedicine;
import com.sahanruwanga.medcarer.app.AppConfig;
import com.sahanruwanga.medcarer.app.AppController;
import com.sahanruwanga.medcarer.app.Appointment;
import com.sahanruwanga.medcarer.app.MedicalRecord;
import com.sahanruwanga.medcarer.app.MedicationSchedule;
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

    private User user;

    public static final int SYNCED_WITH_SERVER = 1;
    public static final int NOT_SYNCED_WITH_SERVER = 0;

    public static final String DATA_SYNCED_BROADCAST = "dataSynced";

    public NetworkStateChecker(){
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        this.sqLiteHandler = new SQLiteHandler(context);
        this.user = new User(getContext());

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
                "Sync-Online",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//
                        performSyncForMedicalRecord();
                        performSyncForAppointment();
                        performSyncForAllergicMedicine();
                        performSyncForMedicationSchedule();
                        getContext().sendBroadcast(new Intent(DATA_SYNCED_BROADCAST));
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

    // Medical Record functions
    private void performSyncForMedicalRecord(){
        // Getting all the un-synced saved medical records
        List<MedicalRecord> saveddMedicalRecords = getSqLiteHandler().getUnsyncedSavedMedicalRecords();
        for(MedicalRecord medicalRecord : saveddMedicalRecords){
            getUser().saveNewRecordInMySQL(String.valueOf(medicalRecord.getRecord_id()), medicalRecord.getDisease(),
                    medicalRecord.getMedicine(), medicalRecord.getDuration(), medicalRecord.getAllergic(),
                    medicalRecord.getDoctor(), medicalRecord.getContact(), medicalRecord.getDescription(),
                    medicalRecord.getCreatedAt());
        }

        // Getting all the un-synced updated medical records
        List<MedicalRecord> updatedMedicalRecords = getSqLiteHandler().getUnsyncedUpdatedMedicalRecords();
        for(MedicalRecord medicalRecord : updatedMedicalRecords) {
            getUser().updateRecordInMySQL(String.valueOf(medicalRecord.getRecord_id()),
                    medicalRecord.getDisease(), medicalRecord.getMedicine(), medicalRecord.getDuration(),
                    medicalRecord.getAllergic(), medicalRecord.getDoctor(), medicalRecord.getContact(),
                    medicalRecord.getDescription());
        }

        // Getting all the un-synced deleted medical records
        List<MedicalRecord> deletedMedicalRecords = getSqLiteHandler().getUnsyncedDeletedMedicalRecords();
        for(MedicalRecord medicalRecord : deletedMedicalRecords) {
            getUser().deleteMedicalRecordFromMySql(String.valueOf(medicalRecord.getRecord_id()));
        }
    }

    // Appointment Functions
    private void performSyncForAppointment(){
        // Getting all the un-synced saved appointments
        List<Appointment> savedAppointments = getSqLiteHandler().getUnsyncedSavedAppointment();
        for(Appointment appointment : savedAppointments){
            getUser().saveNewAppointmentInMySQL(String.valueOf(appointment.getAppointmentId()),
                    appointment.getReason(), appointment.getDate(), appointment.getTime(),
                    appointment.getVenue(),appointment.getDoctor(), appointment.getClinicContact(),
                    appointment.getNotifyTime(), appointment.getCreated_at(), appointment.getNotificationStatus());
        }

        // Getting all the un-synced updated appointments
        List<Appointment> updatedAppointments = getSqLiteHandler().getUnsyncedUpdatedAppointment();
        for(Appointment appointment : updatedAppointments) {
            getUser().updateAppointmentInMySQL(String.valueOf(appointment.getAppointmentId()),
                    appointment.getReason(), appointment.getDate(), appointment.getTime(),
                    appointment.getVenue(),appointment.getDoctor(), appointment.getClinicContact(),
                    appointment.getNotifyTime(), String.valueOf(appointment.getNotificationStatus()));
        }

        // Getting all the un-synced deleted appointments
        List<Appointment> deletedAppointments = getSqLiteHandler().getUnsyncedDeletedAppointment();
        for(Appointment appointment : deletedAppointments) {
            getUser().deleteAppointmentFromMySQL(String.valueOf(appointment.getAppointmentId()));
        }
    }

    // Medication Schedule Functions
    private void performSyncForMedicationSchedule(){
        // Getting all the un-synced saved medication schedule
        List<MedicationSchedule> savedMedicationSchedules = getSqLiteHandler().getUnsyncedSavedMedicationSchedule();
        for(MedicationSchedule medicationSchedule : savedMedicationSchedules){
            getUser().saveNewMedicationScheduleInMySQL(String.valueOf(medicationSchedule.getScheduleId()),
                    medicationSchedule.getMedicine(), medicationSchedule.getQuantity(), medicationSchedule.getStartTime(),
                    medicationSchedule.getPeriod(), medicationSchedule.getNotifyTime(), medicationSchedule.getNextNotifyTime(),
                    medicationSchedule.getCreatedAt(), medicationSchedule.getNotificationStatus());
        }

        // Getting all the un-synced updated medication schedule
        List<MedicationSchedule> updatedMedicationSchedules = getSqLiteHandler().getUnsyncedUpdatedMedicationSchedule();
        for(MedicationSchedule medicationSchedule : updatedMedicationSchedules) {
            getUser().updateMedicationScheduleInMySQL(String.valueOf(medicationSchedule.getScheduleId()),
                    medicationSchedule.getMedicine(), medicationSchedule.getQuantity(), medicationSchedule.getStartTime(),
                    medicationSchedule.getPeriod(), medicationSchedule.getNotifyTime(), medicationSchedule.getNextNotifyTime(),
                    String.valueOf(medicationSchedule.getNotificationStatus()));
        }

        // Getting all the un-synced deleted medication schedule
        List<MedicationSchedule> deletedMedicationSchedules = getSqLiteHandler().getUnsyncedDeletedMedicationSchedule();
        for(MedicationSchedule medicationSchedule : deletedMedicationSchedules) {
            getUser().deleteMedicalRecordFromMySql(String.valueOf(medicationSchedule.getScheduleId()));
        }
    }

    // Allergic Medicine Function
    private void performSyncForAllergicMedicine(){
        // Getting all the un-synced saved Allergic Medicine
        List<AllergicMedicine> savedAllergicMedicines = getSqLiteHandler().getUnsyncedSavedAllergicMedicine();
        for(AllergicMedicine allergicMedicine : savedAllergicMedicines){
            getUser().saveNewAllergicMedicineInMySQL(String.valueOf(allergicMedicine.getAllergicMedicineId()),
                    allergicMedicine.getMedicineName(), allergicMedicine.getDescription(), allergicMedicine.getCreatedAt());
        }

        // Getting all the un-synced updated Allergic Medicine
        List<AllergicMedicine> updatedAllergicMedicines = getSqLiteHandler().getUnsyncedUpdatedAllergicMedicine();
        for(AllergicMedicine allergicMedicine : updatedAllergicMedicines){
            getUser().updateAllergicMedicineInMySQL(String.valueOf(allergicMedicine.getAllergicMedicineId()),
                    allergicMedicine.getMedicineName(), allergicMedicine.getDescription());
        }

        // Getting all the un-synced deleted Allergic Medicine
        List<AllergicMedicine> deletedAllergicMedicines = getSqLiteHandler().getUnsyncedDeletedAllergicMedicine();
        for(AllergicMedicine allergicMedicine : deletedAllergicMedicines) {
            getUser().deleteAllergicMedicineFromMySQL(String.valueOf(allergicMedicine.getAllergicMedicineId()));
        }
    }


    // Check whether the Data is altered in SQLite
    private boolean isUpdateRequired(){
        if(getSqLiteHandler().getUnsyncedSavedMedicalRecords().size() == 0 &&
                getSqLiteHandler().getUnsyncedUpdatedMedicalRecords().size() == 0 &&
                getSqLiteHandler().getUnsyncedDeletedMedicalRecords().size() == 0 &&
                getSqLiteHandler().getUnsyncedSavedAppointment().size() == 0 &&
                getSqLiteHandler().getUnsyncedUpdatedAppointment().size() == 0 &&
                getSqLiteHandler().getUnsyncedDeletedAppointment().size() == 0 &&
                getSqLiteHandler().getUnsyncedSavedMedicationSchedule().size() == 0 &&
                getSqLiteHandler().getUnsyncedUpdatedMedicationSchedule().size() == 0 &&
                getSqLiteHandler().getUnsyncedDeletedMedicationSchedule().size() == 0 &&
                getSqLiteHandler().getUnsyncedSavedAllergicMedicine().size() == 0 &&
                getSqLiteHandler().getUnsyncedUpdatedAllergicMedicine().size() == 0 &&
                getSqLiteHandler().getUnsyncedDeletedAllergicMedicine().size() == 0)
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
                                getSqLiteHandler().updateMedicalRecordSyncStatus(medicalRecord.getRecord_id(), SYNCED_WITH_SERVER);

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
                        getSqLiteHandler().deleteMedicalRecord(Integer.parseInt(localRecordId));
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
                AppConfig.URL_UPDATE_MEDICAL_RECORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // Updating the status in SQLite
                        getSqLiteHandler().updateMedicalRecordSyncStatus(medicalRecord.getRecord_id(), SYNCED_WITH_SERVER);

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

    //region Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SQLiteHandler getSqLiteHandler() {
        return sqLiteHandler;
    }

    public void setSqLiteHandler(SQLiteHandler sqLiteHandler) {
        this.sqLiteHandler = sqLiteHandler;
    }
    //endregion
}
