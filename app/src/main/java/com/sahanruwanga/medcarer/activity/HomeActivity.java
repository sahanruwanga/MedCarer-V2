package com.sahanruwanga.medcarer.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
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

    private DrawerLayout drawer;

    private NetworkStateChecker networkStateChecker;
    private IntentFilter intentFilter;
//    private BroadcastReceiver broadcastReceiver;
    private RequestPermissionHandler requestPermissionHandler;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Create User Object
        this.user = new User(this);

        this.requestPermissionHandler = new RequestPermissionHandler();
        requestPermission();

        this.networkStateChecker = new NetworkStateChecker();
        this.intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(getNetworkStateChecker(), getIntentFilter());

        this.drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

        if (id == R.id.savePDF) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openNaviBar(View view) {
        getDrawer().openDrawer(Gravity.LEFT);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.myProfile) {
            Intent intent = new Intent(this, MyProfileActivity.class);
            startActivity(intent);
//        } else if (id == R.id.notification) {

        } else if (id == R.id.logout) {
            openDialogBox();

        } else if (id == R.id.downloadUM) {

        } else if (id == R.id.aboutApp) {
            Intent intent = new Intent(this, AppInfoActivity.class);
            startActivity(intent);

        } else if (id == R.id.aboutDevSR) {
            Intent intent = new Intent(this, AboutMeActivity.class);
            startActivity(intent);

        } else if (id == R.id.rate) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //region Dialog Box
    // Open dialog box after pressing logout icon
    private void openDialogBox(){
        String message = "Are you sure want to Logout?";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        unregisterReceiver(getNetworkStateChecker());
                        getUser().logout();
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }
    //endregion


    //region Launching other main activities
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
        String url = "http://maps.google.co.uk/maps?q=Pharmacy&hl=en";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
        startActivity(intent);
    }
    //endregion


    //region Getters and Setters
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //endregion
}
