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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.AllergicMedicine;
import com.sahanruwanga.medcarer.app.AllergicMedicineAdapter;
import com.sahanruwanga.medcarer.app.AppConfig;
import com.sahanruwanga.medcarer.app.AppController;
import com.sahanruwanga.medcarer.helper.SQLiteHandler;
import com.sahanruwanga.medcarer.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllergicMedicineActivity extends AppCompatActivity {
    private static final String TAG = AllergicMedicineActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView toolBarText;
    private MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressDialog progressDialog;

    private SQLiteHandler sqLiteHandler;
    private SessionManager sessionManager;
    private Menu menu;
    private AllergicMedicineAdapter allergicMedicineAdapter;

    //region onCreate Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergic_medicine);

        // SQLiteHelper and SessionManager initialization
        this.sqLiteHandler = new SQLiteHandler(getApplicationContext());
        this.sessionManager = new SessionManager(getApplicationContext());

        // RecyclerView initialization
        this.recyclerView = findViewById(R.id.allergicMedicineRecyclerView);
        getRecyclerView().setHasFixedSize(true);
        this.layoutManager =  new LinearLayoutManager(this);
        getRecyclerView().setLayoutManager(getLayoutManager());

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // Tool bar initialization
        this.toolBarText = findViewById(R.id.toolBarTextAllergicMedicine);
        this.toolbar = findViewById(R.id.toolbarAddMedicalRecord);
        setSupportActionBar(toolbar);

        // Check for Allergic Medicine table created event
//        if(!getSessionManager().isAllergicMedicineCreated()){
//            storeInSQLite(User.getUserId());
//            Toast.makeText(getApplicationContext(), "Call to save in SQLite", Toast.LENGTH_LONG).show();
//        }

        // Add data into RecyclerView
        showRecyclerView();

        //region SearchBar function
        this.searchView = findViewById(R.id.searchViewAllergicMedicine);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
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

    //region Store data in SQLite database
    // Save data into SQLite database from MySQL database
    private void storeInSQLite(final String user_id){
        // Tag used to cancel the request
        String tag_string_req = "req_allergic_medicine";

        progressDialog.setMessage("Retrieving data ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ALLERGIC_MEDICINE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Allergic Medicine Response: " + response.toString());
                hideDialog();

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

                            getSqLiteHandler().addAllergicMedicine(Integer.parseInt(medicines.getString(i)),
                                    medicine, description, createdAt);
                        }
                        Toast.makeText(getApplicationContext(), "Added to SQLite", Toast.LENGTH_LONG).show();

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

    private List<AllergicMedicine> getMedicines(){
        List<AllergicMedicine> allergicMedicines = new ArrayList<>();
        AllergicMedicine allergicMedicine;
        for (int i=0; i<10; i++){
            allergicMedicine = new AllergicMedicine();
            allergicMedicine.setDescription("Sample DEscription");
            allergicMedicine.setMedicineName("Panadol");
            allergicMedicine.setCreatedAt("20147");
            allergicMedicine.setAllergicMedicineId(i+1);
            allergicMedicines.add(allergicMedicine);
        }
        return allergicMedicines;
    }

    //region Show RecyclerView in Allergic Medicine
    private void showRecyclerView(){
        this.allergicMedicineAdapter = new AllergicMedicineAdapter(getMedicines(), this, getRecyclerView());
        getRecyclerView().setAdapter(getAllergicMedicineAdapter());
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


    public void openAddAllergicMedicine(View view) {
        Intent intent = new Intent(this, NewAllergicMedicineActivity.class);
        startActivity(intent);
        finish();
    }

    //region Getters and Setters
    public SQLiteHandler getSqLiteHandler() {
        return sqLiteHandler;
    }

    public void setSqLiteHandler(SQLiteHandler sqLiteHandler) {
        this.sqLiteHandler = sqLiteHandler;
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

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public AllergicMedicineAdapter getAllergicMedicineAdapter() {
        return allergicMedicineAdapter;
    }

    public void setAllergicMedicineAdapter(AllergicMedicineAdapter allergicMedicineAdapter) {
        this.allergicMedicineAdapter = allergicMedicineAdapter;
    }
    //endregion
}
