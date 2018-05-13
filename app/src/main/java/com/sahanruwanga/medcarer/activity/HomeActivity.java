package com.sahanruwanga.medcarer.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.User;
import com.sahanruwanga.medcarer.helper.NetworkStateChecker;
import com.sahanruwanga.medcarer.helper.RequestPermissionHandler;
import com.sahanruwanga.medcarer.helper.SQLiteHandler;
import com.sahanruwanga.medcarer.helper.SessionManager;

//from blog

import android.content.Intent;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SQLiteHandler sqLiteHandler;
    private SessionManager sessionManager;

    private DrawerLayout drawer;

    private NetworkStateChecker networkStateChecker;
    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;
    private RequestPermissionHandler requestPermissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.requestPermissionHandler = new RequestPermissionHandler();
        requestPermission();

        this.networkStateChecker = new NetworkStateChecker();
        this.intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(getNetworkStateChecker(), getIntentFilter());

        this.drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //region From Blog
        // SqLite database handler
        setSqLiteHandler(new SQLiteHandler(getApplicationContext()));

        // sessionManager manager
        setSessionManager(new SessionManager(getApplicationContext()));

        if (!getSessionManager().isLoggedIn()) {
            logoutUser();
        }
        //endregion
    }

    private void requestPermission(){
        getRequestPermissionHandler().requestPermission(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onFailed() {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getRequestPermissionHandler().onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    //region From Blog
    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        getSessionManager().setLogin(false);
        getSqLiteHandler().deleteTables();

        // Launching the login activity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    //endregion

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            unregisterReceiver(getNetworkStateChecker());
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.savePDF) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openNaviBar(View view) {
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.myProfile) {
            Intent intent = new Intent(this, MyProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.notification) {

        } else if (id == R.id.logout) {
            logoutUser();
        } else if (id == R.id.downloadUM) {

        } else if (id == R.id.aboutApp) {

        } else if (id == R.id.aboutDevSR) {

        } else if (id == R.id.rate) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openMedicalHistory(View view) {
        Intent intent = new Intent(this, MedicalHistoryActivity.class);
        startActivity(intent);
    }

    public void openAllergicMedicine(View view) {
        Intent intent = new Intent(this, AllergicMedicineActivity.class);
        startActivity(intent);
    }

    public void openAlternativeMedicine(View view) {
        Intent intent = new Intent(this, AlternativeMedicineActivity.class);
        startActivity(intent);
    }

    public void openAppointment(View view) {
        Intent intent = new Intent(this, AppointmentActivity.class);
        startActivity(intent);
    }

    public void openMedicationSchedule(View view) {
        Intent intent = new Intent(this, MedicationScheduleActivity.class);
        startActivity(intent);
    }

    public void openPharmacy(View view) {
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

    public NetworkStateChecker getNetworkStateChecker() {
        return networkStateChecker;
    }

    public void setNetworkStateChecker(NetworkStateChecker networkStateChecker) {
        this.networkStateChecker = networkStateChecker;
    }

    public IntentFilter getIntentFilter() {
        return intentFilter;
    }

    public void setIntentFilter(IntentFilter intentFilter) {
        this.intentFilter = intentFilter;
    }

    public RequestPermissionHandler getRequestPermissionHandler() {
        return requestPermissionHandler;
    }

    public void setRequestPermissionHandler(RequestPermissionHandler requestPermissionHandler) {
        this.requestPermissionHandler = requestPermissionHandler;
    }


    public DrawerLayout getDrawer() {
        return drawer;
    }

    public void setDrawer(DrawerLayout drawer) {
        this.drawer = drawer;
    }
}
