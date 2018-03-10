package com.sahanruwanga.medcarer.helper;

/**
 * Created by Sahan Ruwanga on 3/5/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sahanruwanga.medcarer.app.Appointment;
import com.sahanruwanga.medcarer.app.MedicalRecord;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "med_carer";

    // Table names
    private static final String TABLE_USER = "user";
    private static final String TABLE_MEDICAL_HISTORY = "medical_history";
    private static final String TABLE_APPOINTMENT = "appointment";
    private static final String TABLE_MEDICATION_SCHEDULE = "medication_schedule";
    private static final String TABLE_ALLERGIC_MEDICINE = "allergic_medicine";

    //region Table Columns declaration
    // Common columns
    private static final String KEY_ID = "user_id";
    private static  final String KEY_DOCTOR = "doctor";
    private static  final String KEY_NOTIFY_TIME = "notify_time";
    private static  final String KEY_DESCRIPTION = "description";

    // Login Table Columns names

    private static final String KEY_NAME = "user_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "unique_id";
    private static final String KEY_CREATED_AT = "created_at";
    private static  final String KEY_MEDICINE = "medicine";

    // Medical_history table Columns names
    private static  final String KEY_RECORD_ID = "record_id";
    private static  final String KEY_DISEASE = "disease";
    private static  final String KEY_DURATION= "duration";
    private static  final String KEY_ALLERGIC = "allergic";
    private static  final String KEY_CONTACT = "contact";

    // Appointment table Columns names
    private static  final String KEY_APPOINTMENT_ID = "appointment_id";
    private static  final String KEY_REASON = "reason";
    private static  final String KEY_DATE = "date";
    private static  final String KEY_TIME= "time";
    private static  final String KEY_VENUE = "venue";
    private static  final String KEY_CLINIC_CONTACT = "clinic_contact";

    // Medication Schedule table Columns names
    private static  final String KEY_SCHEDULE_ID = "appointment_id";
    private static  final String KEY_QUANTITY = "date";
    private static  final String KEY_PERIOD= "time";

    // Allergic Medicine table Columns names
    private static  final String KEY_ALLERGIC_MEDICINE_ID = "allergic_medicine_id";

    //endregion

    //region Queries for creating tables
    //Create users (login) table query
    private final String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
            + KEY_CREATED_AT + " TEXT" + ")";

    // Create Medical History Table query
    private final String CREATE_MEDICAL_HISTORY_TABLE = "CREATE TABLE " + TABLE_MEDICAL_HISTORY + "("
            + KEY_RECORD_ID + " INTEGER PRIMARY KEY," + KEY_DISEASE + " TEXT,"
            + KEY_MEDICINE + " TEXT," + KEY_DURATION + " TEXT," + KEY_ALLERGIC + " TEXT,"
            + KEY_DOCTOR + " TEXT," + KEY_CONTACT + " TEXT," + KEY_DESCRIPTION + " TEXT" + ")";

    // Create Appointment table query
    private final String CREATE_APPOINTMENT_TABLE = "CREATE TABLE " + TABLE_APPOINTMENT + "("
            + KEY_APPOINTMENT_ID + " INTEGER PRIMARY KEY," + KEY_REASON + " TEXT,"
            + KEY_DATE + " TEXT," + KEY_TIME + " TEXT," + KEY_VENUE + " TEXT,"
            + KEY_DOCTOR + " TEXT," + KEY_CLINIC_CONTACT + " TEXT," + KEY_NOTIFY_TIME + " TEXT" + ")";

    // Create Medication Schedule table query
    private final String CREATE_MEDICATION_SCHEDULE_TABLE = "CREATE TABLE " + TABLE_MEDICATION_SCHEDULE + "("
            + KEY_SCHEDULE_ID + " INTEGER PRIMARY KEY," + KEY_MEDICINE + " TEXT,"
            + KEY_QUANTITY + " TEXT," + KEY_PERIOD + " TEXT," + KEY_NOTIFY_TIME + " TEXT" + ")";

    // Create Allergic Medicine Schedule table query
    private final String CREATE_ALLERGIC_MEDICINE_TABLE = "CREATE TABLE " + TABLE_ALLERGIC_MEDICINE + "("
            + KEY_ALLERGIC_MEDICINE_ID + " INTEGER PRIMARY KEY," + KEY_MEDICINE + " TEXT,"
            + KEY_DESCRIPTION + " TEXT" + ")";

    //endregion

    // Constructor call
    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //region Creating Tables
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_MEDICAL_HISTORY_TABLE);
        db.execSQL(CREATE_ALLERGIC_MEDICINE_TABLE);
        db.execSQL(CREATE_APPOINTMENT_TABLE);
        db.execSQL(CREATE_MEDICATION_SCHEDULE_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICAL_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALLERGIC_MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICATION_SCHEDULE);

        // Create tables again
        onCreate(db);
    }
    //endregion

    //region User Details (LOGIN)
    /**
     * Storing user details in database
     * */
    public void addUser(int user_id, String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user_id);
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("user_id", cursor.getString(0));
            user.put("user_name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("unique_id", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Recreate database Delete all tables and create them again
     * */
    public void deleteTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.delete(TABLE_MEDICAL_HISTORY, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
    //endregion

    //region Medical History Details
    // Storing Medical Record in database
    public void addMedicalRecord(int record_id, String disease, String medicine, String duration,
                                 String allergic, String doctor, String contact, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RECORD_ID, record_id); // Disease
        values.put(KEY_DISEASE, disease); // Disease
        values.put(KEY_MEDICINE, medicine); // Medicine
        values.put(KEY_DURATION, duration); // Duration
        values.put(KEY_ALLERGIC, allergic); // Allergic
        values.put(KEY_DOCTOR, doctor); // Doctor
        values.put(KEY_CONTACT, contact); // Contact
        values.put(KEY_DESCRIPTION, description); // Description

        // Inserting Row
        long id = db.insert(TABLE_MEDICAL_HISTORY, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New medical record is inserted into sqlite: " + id);
    }

    // Getting medical records from database
    public List<MedicalRecord> getMedicalRecords() {
        List<MedicalRecord> medicalRecords = new LinkedList<>();
        String query = "SELECT  * FROM " + TABLE_MEDICAL_HISTORY;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        MedicalRecord medicalRecord;

        if(cursor.moveToFirst()){
            do{
                medicalRecord = new MedicalRecord();
                medicalRecord.setRecord_id(cursor.getInt(cursor.getColumnIndex(KEY_RECORD_ID)));
                medicalRecord.setDisease(cursor.getString(cursor.getColumnIndex(KEY_DISEASE)));
                medicalRecord.setMedicine(cursor.getString(cursor.getColumnIndex(KEY_MEDICINE)));
                medicalRecord.setDuration(cursor.getString(cursor.getColumnIndex(KEY_DURATION)));
                medicalRecord.setAllergic(cursor.getString(cursor.getColumnIndex(KEY_ALLERGIC)));
                medicalRecord.setDoctor(cursor.getString(cursor.getColumnIndex(KEY_DOCTOR)));
                medicalRecord.setContact(cursor.getString(cursor.getColumnIndex(KEY_CONTACT)));
                medicalRecord.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                medicalRecords.add(medicalRecord);
            }while (cursor.moveToNext());
        }
        // return user
        Log.d(TAG, "Fetching medical records from Sqlite: ");

        return medicalRecords;
    }
    //endregion

    //region Appointment Details
    // Storing Appointment in database
    public void addAppointment(int appointment_id, String reason, String date, String time,
                                 String venue, String doctor, String clinic_contact, String notify_time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_APPOINTMENT_ID, appointment_id); // Appointment ID
        values.put(KEY_REASON, reason); // Reason
        values.put(KEY_DATE, date); // Date
        values.put(KEY_TIME, time); // Time
        values.put(KEY_VENUE, venue); // Venue
        values.put(KEY_DOCTOR, doctor); // Doctor
        values.put(KEY_CLINIC_CONTACT, clinic_contact); // Clinic Contact
        values.put(KEY_NOTIFY_TIME, notify_time); // Notify Time

        // Inserting Row
        long id = db.insert(TABLE_APPOINTMENT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New medical record is inserted into sqlite: " + id);
    }

    // Getting medical records from database
    public List<Appointment> getAppointmentDetails() {
        List<Appointment> appointments = new LinkedList<>();
        String query = "SELECT  * FROM " + TABLE_APPOINTMENT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Appointment appointment;

        if(cursor.moveToFirst()){
            do{
                appointment = new Appointment();
                appointment.setAppointmentId(cursor.getInt(cursor.getColumnIndex(KEY_APPOINTMENT_ID)));
                appointment.setReason(cursor.getString(cursor.getColumnIndex(KEY_REASON)));
                appointment.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                appointment.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME)));
                appointment.setVenue(cursor.getString(cursor.getColumnIndex(KEY_VENUE)));
                appointment.setDoctor(cursor.getString(cursor.getColumnIndex(KEY_DOCTOR)));
                appointment.setClinicContact(cursor.getString(cursor.getColumnIndex(KEY_CLINIC_CONTACT)));
                appointment.setNotifyTime(cursor.getString(cursor.getColumnIndex(KEY_NOTIFY_TIME)));
                appointments.add(appointment);
            }while (cursor.moveToNext());
        }
        // return user
        Log.d(TAG, "Fetching appointments from Sqlite: ");

        return appointments;
    }
    //endregion
}
