package com.sahanruwanga.medcarer.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sahan Ruwanga on 3/7/2018.
 */

public class User {
    private static String userId;
    private String name;
    private String address;
    private String bod;
    private String phoneNo;
    private String image;
    private String gender;
    private String email;
    private String password;

    private ProgressDialog progressDialog;
    private Context context;

    private SQLiteHandler sqLiteHandler;
    private SessionManager sessionManager;

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
                Log.d("User Registration", "Register Response: " + response.toString());
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

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
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
                Map<String, String> params = new HashMap<String, String>();
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
                Log.d("User Login", "Login Response: " + response.toString());
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
                        // Launch main activity
                        Intent intent = new Intent(getContext(), HomeActivity.class);
                        getContext().startActivity(intent);
                        ((LoginActivity)getContext()).finish();
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", getEmail());
                params.put("password", getPassword());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public void updateUserProfile(){}

    private void changeImage(){}

    public void logout(){}

    public void seeMedicalHistory(){}

    public void seeAppointment(){}

    public void seeMedicationSchedule(){}

    public void seeAllergicMedicine(){}

    public void searchAlternative(){}

    public void shareMedicalHistory(){}

    public void saveMediaclHistory(){}

    public void downloadUserManual(){}

    public void readAboutApp(){}

    public void readAboutDevSR(){}

    public void rateApp(){}

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

    public String getBod() {
        return bod;
    }

    public void setBod(String bod) {
        this.bod = bod;
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
    //endregion
}
