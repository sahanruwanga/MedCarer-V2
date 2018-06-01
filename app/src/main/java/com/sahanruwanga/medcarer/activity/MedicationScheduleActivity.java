package com.sahanruwanga.medcarer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.MedicationSchedule;
import com.sahanruwanga.medcarer.app.MedicationScheduleAdapter;
import com.sahanruwanga.medcarer.app.User;

public class MedicationScheduleActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolBarText;
    private MaterialSearchView searchView;
    private RecyclerView recyclerView;

    private Menu menu;
    private RecyclerView.LayoutManager layoutManager;
    private MedicationScheduleAdapter medicationScheduleAdapter;

    private User user;

    //region OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_schedule);

        // Create User object
        this.user = new User(this);

        //RecyclerView and layout manager initialization
        this.recyclerView = findViewById(R.id.medicationScheduleRecyclerView);
        getRecyclerView().setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        getRecyclerView().setLayoutManager(layoutManager);

        // TextView in toolbar
        this.setToolBarText((TextView)findViewById(R.id.toolBarTextMedicationSchedule));

        //Toolbar creation
        this.toolbar = findViewById(R.id.toolbarMedicationSchedule);
        setSupportActionBar(getToolbar());

        // Add data into RecyclerView
        showRecyclerView();
    }
    //endregion


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            MedicationSchedule medicationSchedule = data.getParcelableExtra(MedicationSchedule.MEDICATION_SCHEDULE);
            if(medicationSchedule != null){
                getMedicationScheduleAdapter().getMedicationSchedules().add(0, medicationSchedule);
                getMedicationScheduleAdapter().notifyDataSetChanged();
            }
        }else if(resultCode == 2) {
            showRecyclerView();
        }
    }

    //region onCreateOptionMenu and onOptionItemSelected
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.selectAllIcon){
            getMedicationScheduleAdapter().selectAll();
        }else if(id == R.id.deleteIcon){
            openDialogBox();
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region Dialog Box
    // Open dialog box after pressing delete icon
    private void openDialogBox(){
        int selectedCount = getMedicationScheduleAdapter().getSelectingCount();
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
                        getUser().deleteMedicationSchedule(getMedicationScheduleAdapter().getSelectedSchedules());
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

    //region Showing tool bars
    public void showDefaultToolBar() {
        getToolbar().getMenu().clear();
        getToolBarText().setVisibility(View.VISIBLE);
    }

    public void showDeletingToolBar() {
        getToolbar().getMenu().clear();
        getMenuInflater().inflate(R.menu.delete_all, menu);
        getToolBarText().setVisibility(View.GONE);
    }
    //endregion

    //region onBackPressed
    // Back press event
    @Override
    public void onBackPressed() {
        if (getMedicationScheduleAdapter().getSelectingCount() > 0){
            getMedicationScheduleAdapter().deseleceAll();
            showDefaultToolBar();
        }else
            super.onBackPressed();
    }
    //endregion

    // Show saved data in Recycler View
    private void showRecyclerView() {
        this.medicationScheduleAdapter = new MedicationScheduleAdapter(getUser().getMedicationSchedules(),
                this, getRecyclerView());
        getRecyclerView().setAdapter(getMedicationScheduleAdapter());
    }

    public void openNewMedicationSchedule(View view) {
        Intent intent = new Intent(this, NewMedicationScheduleActivity.class);
        startActivityForResult(intent, 1);
    }

    // Back Icon click on toolbar
    public void backIconClick(View view) {
        onBackPressed();
    }

    //region Getters and Setters
    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public TextView getToolBarText() {
        return toolBarText;
    }

    public void setToolBarText(TextView toolBarText) {
        this.toolBarText = toolBarText;
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

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public MedicationScheduleAdapter getMedicationScheduleAdapter() {
        return medicationScheduleAdapter;
    }

    public void setMedicationScheduleAdapter(MedicationScheduleAdapter medicationScheduleAdapter) {
        this.medicationScheduleAdapter = medicationScheduleAdapter;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //endregion
}
