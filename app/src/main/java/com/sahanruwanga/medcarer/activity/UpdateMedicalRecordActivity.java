package com.sahanruwanga.medcarer.activity;

import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.sahanruwanga.medcarer.app.MedicalRecord;
import com.sahanruwanga.medcarer.app.User;
import com.sahanruwanga.medcarer.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateMedicalRecordActivity extends AppCompatActivity {
    private MedicalRecord medicalRecord;
    private EditText disease;
    private EditText medicine;
    private EditText allergic;
    private static EditText date1;
    private static EditText date2;
    private EditText doctor;
    private EditText contact;
    private EditText description;

    private ProgressDialog progressDialog;

    public static final int SYNCED_WITH_SERVER = 1;
    public static final int NOT_SYNCED_WITH_SERVER = 0;

    private static final int DELETED = 3;
    private static final int UPDATED = 2;
    private static final int SAVED = 1;
    private static final int LOADED = 0;

    private SQLiteHandler sqLiteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medical_record);

        // Progress dialog
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setCancelable(false);

        this.sqLiteHandler = new SQLiteHandler(getApplicationContext());

        this.medicalRecord = getIntent().getParcelableExtra("medicalRecord");

        setDisease((EditText)findViewById(R.id.diseaseUpdate));
        setMedicine((EditText)findViewById(R.id.medicineUpdate));
        setAllergic((EditText)findViewById(R.id.allergicUpdate));
        setDate1((EditText)findViewById(R.id.date1Update));
        setDate2((EditText)findViewById(R.id.date2Update));
        setDoctor((EditText)findViewById(R.id.doctorUpdate));
        setContact((EditText)findViewById(R.id.contactUpdate));
        setDescription((EditText)findViewById(R.id.descriptionUpdate));

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

        // Fill data in edit text boxes
        fillData();
    }

    public void setDate(int dateId) {
        Bundle bundle = new Bundle();
        bundle.putInt("dateId", dateId);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void updateMedicalRecord(View view) {
        String disease = getDisease().getText().toString().trim();
        String medicine = getMedicine().getText().toString().trim();
        String duration = getDate1().getText().toString().trim() + " to " + getDate2().getText().toString().trim();
        String allergic = getAllergic().getText().toString().trim();
        String doctor = getDoctor().getText().toString().trim();
        String contact = getContact().getText().toString().trim();
        String description = getDescription().getText().toString().trim();
        int statusType = SAVED;
        if(getMedicalRecord().getSyncStatus() != NOT_SYNCED_WITH_SERVER && getMedicalRecord().getStatusType() == SAVED)
            statusType = UPDATED;
        if(!disease.isEmpty() && !medicine.isEmpty() && !getDate1().getText().toString().isEmpty() &&
                !getDate2().getText().toString().isEmpty() && !allergic.isEmpty()){
            getSqLiteHandler().updateMedicalRecord(getMedicalRecord().getRecord_id(),
                    disease, medicine, duration, allergic, doctor, contact, description,
                    NOT_SYNCED_WITH_SERVER, statusType);

            Toast.makeText(getApplicationContext(), "Record successfully updated!",
                    Toast.LENGTH_LONG).show();
            updateMedicalRecordInServer(String.valueOf(getMedicalRecord().getRecord_id()), disease,
                    medicine, duration, allergic, doctor, contact, description);
        }else{
            Toast.makeText(this, "Please enter required details!", Toast.LENGTH_LONG).show();
        }
        finish();
    }

    // Fill current data in boxes
    private void fillData(){
        getDisease().setText(medicalRecord.getDisease());
        getMedicine().setText(medicalRecord.getMedicine());
        getAllergic().setText(medicalRecord.getAllergic());
        getDate1().setText(medicalRecord.getDuration().substring(0,11));
        getDate2().setText(medicalRecord.getDuration().substring(14));
        getDoctor().setText(medicalRecord.getDoctor());
        getContact().setText(medicalRecord.getContact());
        getDescription().setText(medicalRecord.getDescription());
    }

    private void updateMedicalRecordInServer(final String localId, final String disease, final String medicine, final String duration,
                                   final String allergic, final String doctor, final String contact,
                                   final String description){

        getProgressDialog().setMessage("Updating record ...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATAE_MEDICAL_RECORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Update Medical Record", "Update Record: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        sqLiteHandler.updateSyncStatus(Integer.parseInt(localId), SYNCED_WITH_SERVER);
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

    //region Process Dialog activities
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

    public EditText getDoctor() {
        return doctor;
    }

    public void setDoctor(EditText doctor) {
        this.doctor = doctor;
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

    public static EditText getDate1() {
        return date1;
    }

    public static void setDate1(EditText date1) {
        UpdateMedicalRecordActivity.date1 = date1;
    }

    public static EditText getDate2() {
        return date2;
    }

    public static void setDate2(EditText date2) {
        UpdateMedicalRecordActivity.date2 = date2;
    }

    public SQLiteHandler getSqLiteHandler() {
        return sqLiteHandler;
    }

    public void setSqLiteHandler(SQLiteHandler sqLiteHandler) {
        this.sqLiteHandler = sqLiteHandler;
    }
    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }
    //endregion
}
