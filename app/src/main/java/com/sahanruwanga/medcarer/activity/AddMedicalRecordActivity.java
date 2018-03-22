package com.sahanruwanga.medcarer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.AppConfig;
import com.sahanruwanga.medcarer.app.AppController;
import com.sahanruwanga.medcarer.app.DatePickerFragment;
import com.sahanruwanga.medcarer.app.User;
import com.sahanruwanga.medcarer.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddMedicalRecordActivity extends AppCompatActivity {
    private static final String TAG = AddMedicalRecordActivity.class.getSimpleName();
    private EditText disease;
    private EditText medicine;
    private EditText allergic;
    private EditText date1;
    private EditText date2;
    private EditText doctorName;
    private EditText contact;
    private EditText description;
    private Toolbar toolbar;

    private ProgressDialog pDialog;
    private SQLiteHandler sqLiteHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_record);

        setToolbar((Toolbar) findViewById(R.id.toolbarAddMedicalRecord));
        setSupportActionBar(getToolbar());

        sqLiteHandler = new SQLiteHandler(getApplicationContext());

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Define all Edit Texts
        this.disease = findViewById(R.id.disease);
        this.medicine = findViewById(R.id.medicine);
        this.allergic = findViewById(R.id.allergic);
        this.date1 = findViewById(R.id.date1);
        this.date2 = findViewById(R.id.date2);
        this.doctorName = findViewById(R.id.doctor);
        this.contact = findViewById(R.id.contact);
        this.description = findViewById(R.id.description);

        // Set onFocusListener to open up calender in date text
        getDate1().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    setDate(getDate1().getId());
            }
        });
        getDate2().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    setDate(getDate2().getId());
            }
        });

    }

    public void saveRecord(View view){
        String disease = getDisease().getText().toString().trim();
        String medicine = getMedicine().getText().toString().trim();
        String duration = getDate1().getText().toString().trim() + " to " + getDate2().getText().toString().trim();
        String allergic = getAllergic().getText().toString().trim();
        String doctor = getDoctorName().getText().toString().trim();
        String contact = getContact().getText().toString().trim();
        String description = getDescription().getText().toString().trim();
        if(!disease.isEmpty() && !medicine.isEmpty() && !getDate1().getText().toString().isEmpty() &&
                !getDate2().getText().toString().isEmpty() && !allergic.isEmpty()){
            saveMedicalRecord(disease, medicine, duration, allergic,
                    doctor, contact, description);
        }else{
            Toast.makeText(this, "Please enter required details!", Toast.LENGTH_LONG).show();
        }
    }

    private void saveMedicalRecord(final String disease, final String medicine, final String duration,
                                   final String allergic, final String doctor, final String contact, final String description){
        // Tag used to cancel the request
        String tag_string_req = "req_insert_medical_record";

        pDialog.setMessage("Saving record ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT_MEDICAL_RECORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Insert Record: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // Record successfully stored in MySQL
                        // Now store the record in sqlite

                        JSONObject medicalRecord = jObj.getJSONObject("medical_record");
                        String record_id = medicalRecord.getString("record_id");
                        String created_at = medicalRecord.getString("created_at");

                        // Inserting row in users table
                        sqLiteHandler.addMedicalRecord(Integer.parseInt(record_id), disease, medicine,
                                duration, allergic, doctor, contact, description, created_at);

                        Toast.makeText(getApplicationContext(), "Record successfully inserted!", Toast.LENGTH_LONG).show();
                        clearAll();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Enter correct details again",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", User.getUserId());
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void clearAll(){
        getDisease().setText("");
        getMedicine().setText("");
        getDate1().setText("");
        getDate2().setText("");
        getAllergic().setText("");
        getDoctorName().setText("");
        getContact().setText("");
        getDescription().setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MedicalHistoryActivity.class);
        startActivity(intent);
        finish();
    }

    public void setDate(int dateId) {
//        DialogFragment newFragment = new TimePickerFragment();
//        newFragment.show(getSupportFragmentManager(), "timePicker");
        Bundle bundle = new Bundle();
        bundle.putInt("dateId", dateId);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void openCalender1(View view) {
        setDate(view.getId());
    }

    public void openCalender2(View view) {
        setDate(view.getId());
    }

    //region Getters and Setters
    public EditText getDate2() {
        return date2;
    }

    public void setDate2(EditText date2) {
        this.date2 = date2;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public EditText getDisease() {
        return disease;
    }

    public void setDisease(EditText disease) {
        this.disease = disease;
    }

    public EditText getMedicine() {
        return medicine;
    }

    public void setMedicine(EditText medicine) {
        this.medicine = medicine;
    }

    public EditText getAllergic() {
        return allergic;
    }

    public void setAllergic(EditText allergic) {
        this.allergic = allergic;
    }

    public EditText getDate1() {
        return date1;
    }

    public void setDate1(EditText date1) {
        this.date1 = date1;
    }

    public EditText getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(EditText doctorName) {
        this.doctorName = doctorName;
    }

    public EditText getContact() {
        return contact;
    }

    public void setContact(EditText contact) {
        this.contact = contact;
    }

    public EditText getDescription() {
        return description;
    }

    public void setDescription(EditText description) {
        this.description = description;
    }
    //endregion
}
