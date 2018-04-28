package com.sahanruwanga.medcarer.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.MedicalHistoryAdapter;
import com.sahanruwanga.medcarer.app.PDFCreator;
import com.sahanruwanga.medcarer.app.User;
import com.sahanruwanga.medcarer.helper.SQLiteHandler;

import java.io.File;

public class MedicalHistoryActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolBarText;
    private MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    android.support.v7.widget.SearchView searchView2;

    private SQLiteHandler sqLiteHandler;

    private User user;

    private Menu menu;
    private MedicalHistoryAdapter medicalHistoryAdapter;

    //region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);

        // Example filtering
        searchView2 = findViewById(R.id.mSearch);


        // Creating User object
        this.user = new User(this);

        // SQLiteHelper initialization
        this.sqLiteHandler = new SQLiteHandler(getApplicationContext());

        //RecyclerView and layout manager initialization
        this.recyclerView = findViewById(R.id.medicalHistoryRecyclerView);
        getRecyclerView().setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        getRecyclerView().setLayoutManager(layoutManager);
        // From extra to decorate
        getRecyclerView().setItemAnimator(new DefaultItemAnimator());


        //Toolbar creation
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(getToolbar());
        getToolbar().setLogo(R.drawable.ic_download);

        // TextView in toolbar
        this.setToolBarText((TextView)findViewById(R.id.toolBarText));

        // Add data into RecyclerView
        showRecyclerView();

        //region Search Bar Functions
        setSearchView((MaterialSearchView) findViewById(R.id.searchViewMH));
        getSearchView().setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }
            @Override
            public void onSearchViewClosed() {
            }
        });
        getSearchView().setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
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

        searchView2.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getMedicalHistoryAdapter().getFilter().filter(newText);
                return false;
            }
        });

    }
    //endregion

    //region Show RecyclerView in Medical History
    private void showRecyclerView(){
        setMedicalHistoryAdapter(new MedicalHistoryAdapter(getSqLiteHandler().getMedicalRecords(),
                this, getRecyclerView()));
        getRecyclerView().setAdapter(getMedicalHistoryAdapter());
    }
    //endregion

    //region onCreateOptionMenu and onOptionItemSelected
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.menu = menu;
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem item = menu.findItem(R.id.itemSearch);
        getSearchView().setMenuItem(item);
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.savePDF) {
            savePdf();
        }else if(id == R.id.previewPDF){
            new PDFCreator(getSqLiteHandler().getMedicalRecords()).createPdf();
            openPdf();
        }else if(id == R.id.selectAllIcon){
            getMedicalHistoryAdapter().selectAll();
        }else if(id == R.id.deleteIcon){
            // To confirm the deletion
            openDialogBox();
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region Dialog Box
    // Open dialog box after pressing delete icon
    private void openDialogBox(){
        int selectedCount = getMedicalHistoryAdapter().getSelectingCount();
        String message = " record will be deleted.";
        if(selectedCount > 1)
            message = " records will be deleted.";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(String.valueOf(selectedCount) + message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "DELETE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        for(MedicalRecord medicalRecord : getMedicalHistoryAdapter().getSelectedRecords()) {
//                            getSqLiteHandler().makeDeletedMedicalRecord(medicalRecord.getRecord_id(), NOT_SYNCED_WITH_SERVER);
//                            deleteMedicalRecord(User.getUserId(), String.valueOf(medicalRecord.getRecord_id()));
//                        }
                        getUser().deleteMedicalRecord(getMedicalHistoryAdapter().getSelectedRecords());
                        showDefaultToolBar();
                        showRecyclerView();
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }
    //endregion

    //region opendPdf and savePdf Functions
    private void openPdf(){
        String dir="/Medical_History";
        File file = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOCUMENTS)+dir+ "/MedicalHistory.pdf");

        if(file.exists()) {
//            Toast.makeText(getApplication(), file.toString() , Toast.LENGTH_LONG).show();
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
                Toast.makeText(this, "You don't have any PDF reader.\nPlease install one and try again!", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(getApplication(), "File path is incorrect." , Toast.LENGTH_LONG).show();
    }

    private void savePdf(){

    }

    //endregion

    //region Showing tool bars
    public void showDefaultToolBar() {
        getToolbar().getMenu().clear();
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem item = menu.findItem(R.id.itemSearch);
        getSearchView().setMenuItem(item);
        getMenuInflater().inflate(R.menu.home, menu);
        getToolBarText().setText("Medical History");
        getToolbar().setLogo(R.drawable.ic_download);
    }

    public void showDeletingToolBar() {
        getToolbar().getMenu().clear();
        getMenuInflater().inflate(R.menu.delete_all, menu);
        getToolBarText().setText("");
        getToolbar().setLogo(null);
    }
    //endregion

    //region onBackPressed
    // Back press event
    @Override
    public void onBackPressed() {
        if(getSearchView().isSearchOpen()){
            getSearchView().closeSearch();
        }else if (getMedicalHistoryAdapter().getSelectingCount() > 0){
            getMedicalHistoryAdapter().deseleceAll();
            showDefaultToolBar();
        }else
            super.onBackPressed();
    }
    //endregion

    //region open Add Medical Record activity
    // Open new activity to add new medical record
    public void openAddMedicalRecord(View view){
        Intent intent = new Intent(this, AddMedicalRecordActivity.class);
        startActivity(intent);
        finish();
    }
    //endregion

    //region Getters and Setters
    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public MaterialSearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(MaterialSearchView searchView) {
        this.searchView = searchView;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public MedicalHistoryAdapter getMedicalHistoryAdapter() {
        return medicalHistoryAdapter;
    }

    public void setMedicalHistoryAdapter(MedicalHistoryAdapter medicalHistoryAdapter) {
        this.medicalHistoryAdapter = medicalHistoryAdapter;
    }

    public SQLiteHandler getSqLiteHandler() {
        return sqLiteHandler;
    }

    public void setSqLiteHandler(SQLiteHandler sqLiteHandler) {
        this.sqLiteHandler = sqLiteHandler;
    }

    public TextView getToolBarText() {
        return toolBarText;
    }

    public void setToolBarText(TextView toolBarText) {
        this.toolBarText = toolBarText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //endregion
}
