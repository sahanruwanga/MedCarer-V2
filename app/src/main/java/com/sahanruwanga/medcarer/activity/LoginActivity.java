package com.sahanruwanga.medcarer.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sahanruwanga.medcarer.R;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sahanruwanga.medcarer.app.AppConfig;
import com.sahanruwanga.medcarer.app.AppController;
import com.sahanruwanga.medcarer.app.User;
import com.sahanruwanga.medcarer.helper.NetworkStateChecker;
import com.sahanruwanga.medcarer.helper.SQLiteHandler;
import com.sahanruwanga.medcarer.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText email;
    private EditText password;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private SQLiteHandler sqLiteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Email and Password fields initialization
        setEmail((EditText)findViewById(R.id.email));
        setPassword((EditText)findViewById(R.id.password));

        // Progress dialog
        setProgressDialog(new ProgressDialog(this));
        getProgressDialog().setCancelable(false);

        // SQLite database handler
        setSqLiteHandler(new SQLiteHandler(getApplicationContext()));

        // Session manager
        setSessionManager(new SessionManager(getApplicationContext()));

        // Check if user is already logged in or not
        if (getSessionManager().isLoggedIn()) {
            User.setUserId(getSqLiteHandler().getUserDetails().get("user_id"));
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void doLogin(View view) {
        String email = getEmail().getText().toString().trim();
        String password = getPassword().getText().toString().trim();

        // Check for empty data in the form
        if (!email.isEmpty() && !password.isEmpty()) {
            // login user
            checkLogin(email, password);
        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(),
                    "Please enter the credentials!", Toast.LENGTH_LONG)
                    .show();
        }

    }


    //region checkLogin()
    /**
     * function to verify login details in mysql sqLiteHandler
     * */
    public void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        getProgressDialog().setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login sessionManager
                        getSessionManager().setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("unique_id");

                        JSONObject user = jObj.getJSONObject("user");
                        String user_id = user.getString("user_id");
                        String name = user.getString("user_name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        getSqLiteHandler().addUser(Integer.parseInt(user_id), name, email, uid, created_at);
                        User.setUserId(user_id);
                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                HomeActivity.class);
                        startActivity(intent);
                        finish();
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    //endregion


    private void showDialog() {
        if (!getProgressDialog().isShowing())
            getProgressDialog().show();
    }

    private void hideDialog() {
        if (getProgressDialog().isShowing())
            getProgressDialog().dismiss();
    }

    public void openRegisterActivity(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    //region Getters and Setters
    public EditText getEmail() {
        return email;
    }

    public void setEmail(EditText email) {
        this.email = email;
    }

    public EditText getPassword() {
        return password;
    }

    public void setPassword(EditText password) {
        this.password = password;
    }

    public SQLiteHandler getSqLiteHandler() {
        return sqLiteHandler;
    }

    public void setSqLiteHandler(SQLiteHandler sqLiteHandler) {
        this.sqLiteHandler = sqLiteHandler;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    //endregion
}
