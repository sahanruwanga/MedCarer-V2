package com.sahanruwanga.medcarer.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sahanruwanga.medcarer.activity.HomeActivity;
import com.sahanruwanga.medcarer.activity.LoginActivity;
import com.sahanruwanga.medcarer.activity.RegisterActivity;
import com.sahanruwanga.medcarer.helper.SQLiteHandler;
import com.sahanruwanga.medcarer.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sahan Ruwanga on 3/7/2018.
 */

public class User implements Parcelable{
    private static String userId;
    private String name;
    private String address;
    private String dob;
    private String phoneNo;
    private String image;
    private String gender;
    private String email;
    private String password;
    private String bloodType;
    private String note;
    private int age;

    private int syncStatus;
    private int statusType;
    private String createdAt;

    private ProgressDialog progressDialog;
    private Context context;

    private SQLiteHandler sqLiteHandler;
    private SessionManager sessionManager;

    public static final String USER = "user";

    public User(){}

    public User(Context context){
        this.context = context;
        this.progressDialog = new ProgressDialog(getContext());
        getProgressDialog().setCancelable(false);
        this.sqLiteHandler = new SQLiteHandler(getContext());
        this.sessionManager = new SessionManager(getContext());
    }

    public User(String name, String email, String password, Context context){
        this.name = name;
        this.email = email;
        this.password = password;
        this.context = context;
        this.progressDialog = new ProgressDialog(getContext());
        getProgressDialog().setCancelable(false);
        this.sqLiteHandler = new SQLiteHandler(getContext());
    }



    public void register(){
        getProgressDialog().setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("User Registration", "Register Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Then store the user in SQLite
                        String uid = jObj.getString("unique_id");

                        JSONObject user = jObj.getJSONObject("user");
                        String user_id = user.getString("user_id");
                        String name = user.getString("user_name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");

                        // Inserting row in users table
                        getSqLiteHandler().addUser(Integer.parseInt(user_id), name,
                                email, uid, created_at);

                        Toast.makeText(getContext(),
                                "User successfully registered. Try login now!",
                                Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        getContext().startActivity(intent);
                        // To finish the Registration activity (cast to call finish)
                        ((RegisterActivity)getContext()).finish();
                    } else {
                        Toast.makeText(getContext(), "Error occurred, please try again!",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Enter correct details again",
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("User Registration", "Registration Error: " + error.getMessage());
                Toast.makeText(getContext(), "Internet connection is required!",
                        Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("user_name", getName());
                params.put("email", getEmail());
                params.put("password", getPassword());

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public void checkLoginStatus(){
        // Check if user is already logged in or not
        if (getSessionManager().isLoggedIn()) {
            User.setUserId(getSqLiteHandler().getUserDetails().get("user_id"));
            // User is already logged in. Take him to home activity
            Intent intent = new Intent(getContext(), HomeActivity.class);
            getContext().startActivity(intent);
            ((LoginActivity)getContext()).finish();
        }
    }

    public void login(){
        getProgressDialog().setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("User Login", "Login Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login sessionManager
                        getSessionManager().setLogin(true);

                        // Then store the user in SQLite
                        String uid = jObj.getString("unique_id");

                        JSONObject user = jObj.getJSONObject("user");
                        String user_id = user.getString("user_id");
                        String name = user.getString("user_name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");

                        // Inserting row in users table
                        getSqLiteHandler().addUser(Integer.parseInt(user_id), name, email, uid, created_at);
                        User.setUserId(user_id);

                        // Store data in SQLite from MySql
                        storeMedicalHistoryInSQLite();
                        storeAppointmentInSQLite();
                        storeMedicationScheduleInSQLite();
                        storeAllergicMedicineInSQLite();
                        storeAlternativeMedicineInSQLite();

                        // Launch main activity
                        openHomeActivity();
                    } else {
                        // Error in login. Get the error message
                        Toast.makeText(getContext(),
                                "Error occurred, please try again!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(),
                            "Enter correct details again!", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("User Login", "Login Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        "Internet connection is required!", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("email", getEmail());
                params.put("password", getPassword());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public void logout(){
        getSessionManager().setLogin(false);
        getSqLiteHandler().deleteTables();

        // Launching the login activity
        Intent intent = new Intent(getContext(), LoginActivity.class);
        getContext().startActivity(intent);
        HomeActivity homeActivity = (HomeActivity) getContext();
        homeActivity.finish();
    }


    //region Medical History maintaining
    // Store Medical Records in SQLite from MySql
    private void storeMedicalHistoryInSQLite(){
        progressDialog.setMessage("Retrieving data ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MEDICAL_HISTORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Medical History", "Medical History Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
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
                                    disease, medicine, duration, allergic, doctor, contact,
                                    description, created_at, SQLiteHandler.SYNCED_WITH_SERVER,
                                    SQLiteHandler.LOADED);
                        }

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Please activate network access and Try again", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Medical History", "Retrieving Error: " + error.getMessage());
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to medical history url
                Map<String, String> params = new HashMap<>();
                params.put("user_id", User.getUserId());

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    // Get medical records from SQLite
    public List<MedicalRecord> getMedicalRecords(){
        return getSqLiteHandler().getMedicalRecords();
    }

    // Save new record in SQLite
    public long saveNewRecord(String disease, String medicine, String duration,
                              String allergic, String doctor, String contact, String description,
                              String createdAt){
        long localId = getSqLiteHandler().addMedicalRecord(disease, medicine,
                duration, allergic, doctor, contact, description, createdAt,
                SQLiteHandler.NOT_SYNCED_WITH_SERVER, SQLiteHandler.SAVED);

        Toast.makeText(getContext(), "Record successfully inserted!", Toast.LENGTH_LONG).show();
        saveNewRecordInMySQL(String.valueOf(localId), disease, medicine, duration, allergic,
                doctor, contact, description, createdAt);
        return localId;

    }

    // Save new record in MySQL
    public void saveNewRecordInMySQL(final String localId, final String disease,
                                      final String medicine, final String duration,
                                      final String allergic, final String doctor,
                                      final String contact, final String description,
                                      final String createdAt){
        getProgressDialog().setMessage("Saving record ...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT_MEDICAL_RECORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Saving record in MySQL", "Insert Record: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // Inserting row in users table
                        getSqLiteHandler().updateMedicalRecordSyncStatus(Integer.parseInt(localId), SQLiteHandler.SYNCED_WITH_SERVER);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Saving record in MySQL", "Registration Error: " + error.getMessage());
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", User.getUserId());
                params.put("local_record_id", localId);
                params.put("disease", disease);
                params.put("medicine", medicine);
                params.put("duration", duration);
                params.put("allergic", allergic);
                params.put("doctor", doctor);
                params.put("contact", contact);
                params.put("description", description);
                params.put("created_at", createdAt);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    // Update record in SQLite
    public void updateRecord(int recordId, String disease, String medicine, String duration,
                             String allergic, String doctor, String contact,
                             String description, int syncStatus, int statusType){
        if(statusType == SQLiteHandler.UPDATED || statusType == SQLiteHandler.LOADED) {
            getSqLiteHandler().updateMedicalRecord(recordId, disease, medicine, duration, allergic,
                    doctor, contact, description, SQLiteHandler.NOT_SYNCED_WITH_SERVER,
                    SQLiteHandler.UPDATED);
        }else if(statusType == SQLiteHandler.SAVED) {
            if(syncStatus == SQLiteHandler.SYNCED_WITH_SERVER)
                getSqLiteHandler().updateMedicalRecord(recordId, disease, medicine, duration, allergic,
                        doctor, contact, description, SQLiteHandler.NOT_SYNCED_WITH_SERVER,
                        SQLiteHandler.UPDATED);
            else
                getSqLiteHandler().updateMedicalRecord(recordId, disease, medicine, duration, allergic,
                        doctor, contact, description, SQLiteHandler.NOT_SYNCED_WITH_SERVER,
                        SQLiteHandler.SAVED);
        }

        Toast.makeText(getContext(), "Record successfully updated!", Toast.LENGTH_LONG).show();
        updateRecordInMySQL((String.valueOf(recordId)), disease, medicine, duration, allergic,
                doctor, contact, description);

    }

    // Update record in MySQL
    public void updateRecordInMySQL(final String localId, final String disease, final String medicine,
                                     final String duration, final String allergic, final String doctor,
                                     final String contact, final String description){
        getProgressDialog().setMessage("Updating record ...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_MEDICAL_RECORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Update Medical Record", "Update Record: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        getSqLiteHandler().updateMedicalRecordSyncStatus(Integer.parseInt(localId), SQLiteHandler.SYNCED_WITH_SERVER);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Update Medical Record", "Updating Error: " + error.getMessage());
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", User.getUserId());
                params.put("local_record_id", localId);
                params.put("disease", disease);
                params.put("medicine", medicine);
                params.put("duration", duration);
                params.put("allergic", allergic);
                params.put("doctor", doctor);
                params.put("contact", contact);
                params.put("description", description);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    // Delete from SQLite
    public void deleteMedicalRecord(ArrayList<MedicalRecord> medicalRecords){
        for(MedicalRecord medicalRecord : medicalRecords) {
            if(medicalRecord.getStatusType() != SQLiteHandler.DELETED){
                getSqLiteHandler().makeDeletedMedicalRecord(medicalRecord.getRecord_id(), SQLiteHandler.NOT_SYNCED_WITH_SERVER);
                deleteMedicalRecordFromMySql(String.valueOf(medicalRecord.getRecord_id()));
            }
        }
    }

    // Delete from MySQL
    public void deleteMedicalRecordFromMySql(final String recordID){
        progressDialog.setMessage("Deleting Record ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_MEDICAL_RECORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Delete Medical Record", "Medical Record Deleting: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        getSqLiteHandler().deleteMedicalRecord(Integer.parseInt(recordID));
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Delete Medical Record", "Deleting Error: " + error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to medical history url
                Map<String, String> params = new HashMap<>();
                params.put("user_id", getUserId());
                params.put("local_record_id", recordID);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }
    //endregion


    //region Appointment maintaining
    // Store Appointment in SQLite from MySQL
    private void storeAppointmentInSQLite(){
        progressDialog.setMessage("Retrieving data ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_APPOINTMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Appointment", "Appointment Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
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
                            String created_at = appointment.getString(7);
                            int notificationStatus = appointment.getInt(8);
                            String localAppointmentId = appointment.getString(9);

                            getSqLiteHandler().addAppointmentFromMySQL(Integer.parseInt(localAppointmentId),
                                    reason, date, time, venue, doctor, clinicContact, notifyTime, created_at, notificationStatus,
                                    SQLiteHandler.SYNCED_WITH_SERVER, SQLiteHandler.LOADED);
                        }

                    } else {
                        // Error in fetching. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Appointment", "Retrieving Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to appointment url
                Map<String, String> params = new HashMap<>();
                params.put("user_id", User.getUserId());

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    // Get appointment from SQLite
    public List<Appointment> getAppointment(){
        return getSqLiteHandler().getAppointment();
    }

    // Save new Appointment in SQLite
    public long saveNewAppointment(String reason, String date, String time, String venue, String doctor, String clinicContact,
                                   String notifyTime, String createdAt){
        long localId = getSqLiteHandler().addAppointment(reason, date, time, venue, doctor, clinicContact,
                notifyTime, createdAt, SQLiteHandler.NOTIFICATION_STATUS_ON, SQLiteHandler.NOT_SYNCED_WITH_SERVER, SQLiteHandler.SAVED);

        Toast.makeText(getContext(), "Record successfully inserted!", Toast.LENGTH_LONG).show();
        saveNewAppointmentInMySQL(String.valueOf(localId), reason, date, time, venue, doctor, clinicContact,
                notifyTime, createdAt, SQLiteHandler.NOTIFICATION_STATUS_ON);
        return localId;
    }

    // Save new Appointment in MySQL
    public void saveNewAppointmentInMySQL(final String localAppointmentId, final String reason, final String date,
                                           final String time, final String venue, final String doctor, final String clinicContact,
                                           final String notifyTime, final String createdAt, final int notificationStatus){
        progressDialog.setMessage("Saving appointment ...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT_APPOINTMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Add Appointment", "Insert Appointment: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        getSqLiteHandler().updateAppointmentSyncStatus(Integer.parseInt(localAppointmentId),
                                SQLiteHandler.SYNCED_WITH_SERVER);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", User.getUserId());
                params.put("local_appointment_id", localAppointmentId);
                params.put("reason", reason);
                params.put("venue", venue);
                params.put("doctor", doctor);
                params.put("clinic_contact", clinicContact);
                params.put("date", date);
                params.put("time", time);
                params.put("notify_time", notifyTime);
                params.put("created_at", createdAt);
                params.put("notification_status", String.valueOf(notificationStatus));

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    // Update Appointment in SQLite
    public void updateAppointment(int appointmentId, String venue, String reason, String doctor,
                                  String clinicPhone, String date, String time, String notifyTime,
                                  int syncStatus, int statusType){
        if(statusType == SQLiteHandler.UPDATED || statusType == SQLiteHandler.LOADED) {
            getSqLiteHandler().updateAppointment(appointmentId, reason, date, time, venue, doctor,
                    clinicPhone,notifyTime, SQLiteHandler.NOTIFICATION_STATUS_ON,
                    SQLiteHandler.NOT_SYNCED_WITH_SERVER, SQLiteHandler.UPDATED);
        }else if(statusType == SQLiteHandler.SAVED) {
            if(syncStatus == SQLiteHandler.SYNCED_WITH_SERVER)
                getSqLiteHandler().updateAppointment(appointmentId, reason, date, time, venue, doctor,
                        clinicPhone,notifyTime, SQLiteHandler.NOTIFICATION_STATUS_ON,
                        SQLiteHandler.NOT_SYNCED_WITH_SERVER, SQLiteHandler.UPDATED);
            else
                getSqLiteHandler().updateAppointment(appointmentId, reason, date, time, venue, doctor,
                        clinicPhone,notifyTime, SQLiteHandler.NOTIFICATION_STATUS_ON,
                        SQLiteHandler.NOT_SYNCED_WITH_SERVER, SQLiteHandler.SAVED);
        }

        Toast.makeText(getContext(), "Record successfully updated!", Toast.LENGTH_LONG).show();
        updateAppointmentInMySQL(String.valueOf(appointmentId), reason, date, time, venue, doctor,
                clinicPhone, notifyTime, String.valueOf(SQLiteHandler.NOTIFICATION_STATUS_ON));
    }

    // Update Appointment in MySQL
    public void updateAppointmentInMySQL(final String localAppointmetId, final String reason, final String date,
                                          final String time, final String venue, final String doctor, final String clinicPhone,
                                          final String notifyTime, final String notificationStatus){
        getProgressDialog().setMessage("Updating...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_APPOINTMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Update Appointment", "Update Appointment: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        getSqLiteHandler().updateAppointmentSyncStatus(Integer.parseInt(localAppointmetId),
                                SQLiteHandler.SYNCED_WITH_SERVER);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Update Appointment", "Updating Error: " + error.getMessage());
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", User.getUserId());
                params.put("local_appointment_id", localAppointmetId);
                params.put("reason", reason);
                params.put("venue", venue);
                params.put("doctor", doctor);
                params.put("clinic_contact", clinicPhone);
                params.put("date", date);
                params.put("time", time);
                params.put("notify_time", notifyTime);
                params.put("notification_status", notificationStatus);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    // Delete Appointment from SQLite
    public void deleteAppointment(ArrayList<Appointment> appointments){
        for(Appointment appointment : appointments) {
            if(appointment.getStatusType() != SQLiteHandler.DELETED){
                getSqLiteHandler().makeDeletedAppointment(appointment.getAppointmentId(), SQLiteHandler.NOT_SYNCED_WITH_SERVER);
                deleteAppointmentFromMySQL(String.valueOf(appointment.getAppointmentId()));
            }
        }
    }

    // Delete Appointment from MySQL
    public void deleteAppointmentFromMySQL(final String localAppointmentId){
        progressDialog.setMessage("Deleting...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_APPOINTMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Delete Appointment", "Appointment Deleting: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        getSqLiteHandler().deleteAppointment(Integer.parseInt(localAppointmentId));
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Delete Appointment", "Deleting Error: " + error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to medical history url
                Map<String, String> params = new HashMap<>();
                params.put("user_id", getUserId());
                params.put("local_appointment_id", localAppointmentId);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }
    //endregion


    //region Medication Schedule maintaining
    // Store Medication Schedule in SQLite from MySQL
    private void storeMedicationScheduleInSQLite(){
        getProgressDialog().setMessage("Retrieving data ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MEDICATION_SCHEDULE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Medication Schedule", "Medication Schedule Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
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
                            int notification_status = medicationSchedule.getInt(6);
                            String localScheduleId = medicationSchedule.getString(7);
                            String nextNotifyTime = medicationSchedule.getString(8);

                            getSqLiteHandler().addMedicationScheduleFromMySQL(Integer.parseInt(localScheduleId),
                                    medicine, quantity, start_time, period, notify_time, nextNotifyTime,
                                    created_at, notification_status, SQLiteHandler.SYNCED_WITH_SERVER,
                                    SQLiteHandler.LOADED);
                        }

                    } else {
                        // Error in login. Get the error message
                        Toast.makeText(getContext(),
                                "Error occurred, try again later!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Check Network Connection to load data", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Medication Schedule", "Retrieving Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        "Error occurred, try again later!", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to medical history url
                Map<String, String> params = new HashMap<>();
                params.put("user_id", User.getUserId());

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    // Get medication schedule from SQLite
    public List<MedicationSchedule> getMedicationSchedules(){
        return getSqLiteHandler().getMedicationSchedule();
    }

    // Save new Medication Schedule in SQLite
    public long saveNewMedicationSchedule(String medicine, String quantity, String startTime,
                                          String period, String notifyTime, String nextNotifyTime,
                                          String createdAt){
        long localScheduleId = getSqLiteHandler().addMedicationSchedule(medicine, quantity, startTime, period, notifyTime,
                nextNotifyTime, createdAt, SQLiteHandler.NOTIFICATION_STATUS_ON, SQLiteHandler.NOT_SYNCED_WITH_SERVER,
                SQLiteHandler.SAVED);
        Toast.makeText(getContext(), "Schedule successfully inserted!", Toast.LENGTH_LONG).show();
        saveNewMedicationScheduleInMySQL(String.valueOf(localScheduleId), medicine, quantity, startTime,
                period, notifyTime, nextNotifyTime, createdAt, SQLiteHandler.NOTIFICATION_STATUS_ON);
        return localScheduleId;
    }

    // Save new Medication Schedule in MySQL
    public void saveNewMedicationScheduleInMySQL(final String localScheduleId, final String medicine,
                                                  final String quantity, final String startTime, final String period,
                                                  final String notifyTime, final String nextNotifyTime,
                                                  final String createdAt, final int notificationStatus){
        getProgressDialog().setMessage("Saving Schedule ...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT_MEDICATION_SCHEDULE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Add medication schedule", "Insert Schedule: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        getSqLiteHandler().updateMedicationScheduleSyncStatus(Integer.parseInt(localScheduleId),
                                SQLiteHandler.SYNCED_WITH_SERVER);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Add medication schedule", "Registration Error: " + error.getMessage());
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", User.getUserId());
                params.put("local_schedule_id", localScheduleId);
                params.put("medicine", medicine);
                params.put("quantity", quantity);
                params.put("start_time", startTime);
                params.put("period", period);
                params.put("notify_time", notifyTime);
                params.put("next_notify_time", nextNotifyTime);
                params.put("created_at", createdAt);
                params.put("notification_status", String.valueOf(notificationStatus));

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    // Update Medication Schedule in SQLite
    public void updateMedicationSchedule(int localScheduleId, String medicine, String quantity,
                                         String startTime, String peiod, String notifyTime,
                                         String nextNotifyTime, int syncStatus, int statusType){
        if(statusType == SQLiteHandler.UPDATED || statusType == SQLiteHandler.LOADED) {
            getSqLiteHandler().updateMedicationSchedule(localScheduleId, medicine, quantity, startTime, peiod,
                    notifyTime, nextNotifyTime, SQLiteHandler.NOTIFICATION_STATUS_ON,
                    SQLiteHandler.NOT_SYNCED_WITH_SERVER, SQLiteHandler.UPDATED);
        }else if(statusType == SQLiteHandler.SAVED) {
            if(syncStatus == SQLiteHandler.SYNCED_WITH_SERVER)
                getSqLiteHandler().updateMedicationSchedule(localScheduleId, medicine, quantity, startTime, peiod,
                        notifyTime, nextNotifyTime, SQLiteHandler.NOTIFICATION_STATUS_ON,
                        SQLiteHandler.NOT_SYNCED_WITH_SERVER, SQLiteHandler.UPDATED);
            else
                getSqLiteHandler().updateMedicationSchedule(localScheduleId, medicine, quantity, startTime, peiod,
                        notifyTime, nextNotifyTime, SQLiteHandler.NOTIFICATION_STATUS_ON,
                        SQLiteHandler.NOT_SYNCED_WITH_SERVER, SQLiteHandler.SAVED);
        }

        Toast.makeText(getContext(), "Updated!", Toast.LENGTH_LONG).show();
        updateMedicationScheduleInMySQL(String.valueOf(localScheduleId), medicine, quantity, startTime,
                peiod, notifyTime, nextNotifyTime, String.valueOf(SQLiteHandler.NOTIFICATION_STATUS_ON));
    }

    // Update Medication Schedule in MySQL
    public void updateMedicationScheduleInMySQL(final String localScheduleId, final String medicine,
                                                 final String quantity, final String startTime, final String period,
                                                 final String notifyTime, final String nextNotifyTime,
                                                 final String notificationStatus){
        getProgressDialog().setMessage("Updating...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_MEDICATION_SCHEDULE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Update Medication Schedule", "Update Schedule: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        getSqLiteHandler().updateMedicationScheduleSyncStatus(Integer.parseInt(localScheduleId),
                                SQLiteHandler.SYNCED_WITH_SERVER);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Update Medication Schedule", "Updating Error: " + error.getMessage());
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", User.getUserId());
                params.put("local_schedule_id", localScheduleId);
                params.put("medicine", medicine);
                params.put("quantity", quantity);
                params.put("start_time", startTime);
                params.put("period", period);
                params.put("notify_time", notifyTime);
                params.put("next_notify_time", nextNotifyTime);
                params.put("notification_status", notificationStatus);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    // Delete Medication Schedule from SQLite
    public void deleteMedicationSchedule(ArrayList<MedicationSchedule> medicationSchedules){
        for(MedicationSchedule medicationSchedule : medicationSchedules) {
            if(medicationSchedule.getStatusType() != SQLiteHandler.DELETED){
                getSqLiteHandler().makeDeletedMedicationSchedule(medicationSchedule.getScheduleId(),
                        SQLiteHandler.NOT_SYNCED_WITH_SERVER);
                deleteMedicationScheduleFromMySQL(String.valueOf(medicationSchedule.getScheduleId()));
            }
        }
    }

    // Delete Medication Schedule from MySQL
    public void deleteMedicationScheduleFromMySQL(final String localScheduleId){
        progressDialog.setMessage("Deleting...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_MEDICATION_SCHEDULE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Delete Medication Schedule", "Medication Schedule Deleting: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        getSqLiteHandler().deleteMedicationSchedule(Integer.parseInt(localScheduleId));
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Delete Medication Schedule", "Deleting Error: " + error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to medical history url
                Map<String, String> params = new HashMap<>();
                params.put("user_id", getUserId());
                params.put("local_schedule_id", localScheduleId);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }
    //endregion


    //region Allergic Medicine maintaining
    // Store Allergic Medicine in SQLite from MySQL
    private void storeAllergicMedicineInSQLite(){
        getProgressDialog().setMessage("Retrieving data ...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ALLERGIC_MEDICINE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Fetch Allergic Medicine", "Allergic Medicine Response: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        // Store allergic medicine in SQLite
                        JSONArray medicines= jObj.getJSONArray("medicines");
                        for (int i=0; i<medicines.length();i++){
                            JSONArray allergicMedicine = jObj.getJSONArray(medicines.getString(i));
                            String medicine = allergicMedicine.getString(0);
                            String description = allergicMedicine.getString(1);
                            String createdAt = allergicMedicine.getString(2);
                            String localAllergicMedicineId = allergicMedicine.getString(3);

                            getSqLiteHandler().addAllergicMedicineFromMySQL(Integer.parseInt(localAllergicMedicineId),
                                    medicine, description, createdAt, SQLiteHandler.SYNCED_WITH_SERVER, SQLiteHandler.LOADED);
                        }

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Fetch Allergic Medicine", "Retrieving Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to medical history url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", User.getUserId());

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    // Get allergic medicine from SQLite
    public List<AllergicMedicine> getAllergicMedicines(){
        return getSqLiteHandler().getAllergiceMedicine();
    }

    // Save new Allergic Medicine in SQLite
    public long saveNewAllergicMedicine(String medicine, String description, String createdAt){
        long localAllergicMedicineId = getSqLiteHandler().addAllergicMedicine(medicine, description,
                createdAt, SQLiteHandler.NOT_SYNCED_WITH_SERVER, SQLiteHandler.SAVED);
        Toast.makeText(getContext(), "Allergic Medicine successfully inserted!", Toast.LENGTH_LONG).show();
        saveNewAllergicMedicineInMySQL(String.valueOf(localAllergicMedicineId), medicine,
                description, createdAt);
        return localAllergicMedicineId;

    }

    // Save new Allergic Medicine in MySQL
    public void saveNewAllergicMedicineInMySQL(final String localAllergicMedicineId, final String medicine,
                                                final String description, final String createdAt){
        getProgressDialog().setMessage("Saving allergic medicine ...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT_ALLERGIC_MEDICINE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Add Allergic Medicine", "Insert allergic medicine: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        getSqLiteHandler().updateAllergicMedicineSyncStatus(Integer.parseInt(localAllergicMedicineId),
                                SQLiteHandler.SYNCED_WITH_SERVER);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Add Allergic Medicine", "Inserting Error: " + error.getMessage());
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", User.getUserId());
                params.put("local_allergic_medicine_id", localAllergicMedicineId);
                params.put("medicine", medicine);
                params.put("description", description);
                params.put("created_at", createdAt);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    // Update Allergic Medicine in SQLite
    public void updateAllergicMedicine(int allergicMedicineId, String medicine, String note,
                                       int syncStatus, int statusType){
        if(statusType == SQLiteHandler.UPDATED || statusType == SQLiteHandler.LOADED) {
            getSqLiteHandler().updateAllergicMedicine(allergicMedicineId, medicine, note,
                    SQLiteHandler.NOT_SYNCED_WITH_SERVER, SQLiteHandler.UPDATED);
        }else if(statusType == SQLiteHandler.SAVED) {
            if(syncStatus == SQLiteHandler.SYNCED_WITH_SERVER)
                getSqLiteHandler().updateAllergicMedicine(allergicMedicineId, medicine, note,
                        SQLiteHandler.NOT_SYNCED_WITH_SERVER, SQLiteHandler.UPDATED);
            else
                getSqLiteHandler().updateAllergicMedicine(allergicMedicineId, medicine, note,
                        SQLiteHandler.NOT_SYNCED_WITH_SERVER, SQLiteHandler.SAVED);
        }
        Toast.makeText(getContext(), "Updated!", Toast.LENGTH_LONG).show();
        updateAllergicMedicineInMySQL((String.valueOf(allergicMedicineId)), medicine, note);
    }

    // Update Allergic Medicine in MySQL
    public void updateAllergicMedicineInMySQL(final String localAllergicMedicineId, final String medicine, final String note){
        getProgressDialog().setMessage("Updating...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_ALLERGIC_MEDICINE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Update Allergic Medicine", "Update Allergic Medicine: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        getSqLiteHandler().updateAllergicMedicineSyncStatus(Integer.parseInt(localAllergicMedicineId),
                                SQLiteHandler.SYNCED_WITH_SERVER);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Update Allergic Medicine", "Updating Error: " + error.getMessage());
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", User.getUserId());
                params.put("local_allergic_medicine_id", localAllergicMedicineId);
                params.put("medicine", medicine);
                params.put("description", note);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    // Delete Allergic Medicine from SQLite
    public void deleteAllergicMedicine(ArrayList<AllergicMedicine> allergicMedicines){
        for(AllergicMedicine allergicMedicine : allergicMedicines) {
            if(allergicMedicine.getStatusType() != SQLiteHandler.DELETED){
                getSqLiteHandler().makeDeletedAllergicMedicine(allergicMedicine.getAllergicMedicineId(),
                        SQLiteHandler.NOT_SYNCED_WITH_SERVER);
                deleteAllergicMedicineFromMySQL(String.valueOf(allergicMedicine.getAllergicMedicineId()));
            }
        }
    }

    // Delete Allergic Medicine from MySQL
    public void deleteAllergicMedicineFromMySQL(final String localAllergicMedicineId){
        progressDialog.setMessage("Deleting...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_ALLERGIC_MEDICINE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Delete Allergic Medicine", "Allergic Medicine Deleting: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        getSqLiteHandler().deleteAllergicMedicine(Integer.parseInt(localAllergicMedicineId));
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Delete Allergic Medicine", "Deleting Error: " + error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to medical history url
                Map<String, String> params = new HashMap<>();
                params.put("user_id", getUserId());
                params.put("local_allergic_medicine_id", localAllergicMedicineId);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }
    //endregion


    //region Alternative Medicine maintaining
    public void storeAlternativeMedicineInSQLite(){
        getProgressDialog().setMessage("Retrieving data ...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ALTERNATIVE_MEDICINE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Alternative Medicine", "Alternative Medicine Response: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        // Store allergic medicine in SQLite
                        JSONArray medicines= jObj.getJSONArray("medicines");
                        for (int i=0; i<medicines.length();i++){
                            JSONArray alternativeMedicine = jObj.getJSONArray(medicines.getString(i));
                            String medicine = alternativeMedicine.getString(0);
                            String genericName = alternativeMedicine.getString(1);
                            String price = alternativeMedicine.getString(2);
                            String note = alternativeMedicine.getString(3);

                            getSqLiteHandler().addAlternativeMedicineFromMySQL(medicine, genericName,
                                    price, note);
                        }

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Alternative Medicine", "Retrieving Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    // Get Alternative Medicines from SQLite
    public List<AlternativeMedicine> getAlternativeMedicine(){
        return getSqLiteHandler().getAlternativeMedicine();
    }

    // Get Specific Alternative Medicines from SQLite
    public List<AlternativeMedicine> getSpecificAlternativeMedicine(String medicine){
        return getSqLiteHandler().getSpeicificAlternativeMedicine(medicine);
    }

    // Get Generic Name of an Alternative Medicines from SQLite
    public String getGenericName(String medicine){
        return getSqLiteHandler().getGenericName(medicine);
    }

    public void deleteAlternativeMedicineTable(){
        getSqLiteHandler().deleteAlternativeMedicineTable();
    }
    //endregion


    //region User Profile Maintaining
    public User getUserDetails(){
        return getSqLiteHandler().getUserDetail();
    }

    private void openHomeActivity(){
        Intent intent = new Intent(getContext(), HomeActivity.class);
        getContext().startActivity(intent);
        ((LoginActivity)getContext()).finish();
    }

    public void updateUserProfilePersonalInfo(String dob, String gender, String bloodType){
        getSqLiteHandler().updatePersonalInfo(dob, gender, bloodType,
                SQLiteHandler.NOT_SYNCED_WITH_SERVER, SQLiteHandler.UPDATED);
    }

    public void updateUserProfileContactInfo(String phoneNo, String address){
        getSqLiteHandler().updateContactInfo(phoneNo, address,
                SQLiteHandler.NOT_SYNCED_WITH_SERVER, SQLiteHandler.UPDATED);
    }

    public void updateUserNote(String note){
        getSqLiteHandler().updateOtherNotes(note, SQLiteHandler.NOT_SYNCED_WITH_SERVER,
                SQLiteHandler.UPDATED);
    }

    public void updateUserName(String name){
        getSqLiteHandler().updateName(name, SQLiteHandler.NOT_SYNCED_WITH_SERVER,
                SQLiteHandler.UPDATED);
    }

    private void changeImage(){}

    //endregion

    public void downloadUserManual(){}

    public void rateApp(){}

    public String calculateAge(String dob){
        if(dob != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            int dobSaved = Integer.parseInt(dob.substring(0, 4));
            int date = Integer.parseInt(dateFormat.format(new Date()));
            return String.valueOf(date - dobSaved) + " years";
        }else return "";
    }


    //region Progress Dialog Functions
    private void showDialog() {
        if (!getProgressDialog().isShowing())
            getProgressDialog().show();
    }

    private void hideDialog() {
        if (getProgressDialog().isShowing())
            getProgressDialog().dismiss();
    }
    //endregion

    //region Getters and Setters
    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        User.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
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

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }

    public int getStatusType() {
        return statusType;
    }

    public void setStatusType(int statusType) {
        this.statusType = statusType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(dob);
        parcel.writeString(phoneNo);
        parcel.writeString(image);
        parcel.writeString(gender);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(bloodType);
        parcel.writeString(note);
        parcel.writeInt(age);
        parcel.writeInt(syncStatus);
        parcel.writeInt(statusType);
        parcel.writeString(createdAt);
    }
    //endregion

    //region Creator for Parcelable
    protected User(Parcel in) {
        name = in.readString();
        address = in.readString();
        dob = in.readString();
        phoneNo = in.readString();
        image = in.readString();
        gender = in.readString();
        email = in.readString();
        password = in.readString();
        bloodType = in.readString();
        note = in.readString();
        age = in.readInt();
        syncStatus = in.readInt();
        statusType = in.readInt();
        createdAt = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    //endregion
}
