package com.sahanruwanga.medcarer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.AllergicMedicineAdapter;
import com.sahanruwanga.medcarer.app.User;

public class AllergicMedicineActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolBarText;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;

    private User user;

    private Menu menu;
    private AllergicMedicineAdapter allergicMedicineAdapter;

    //region onCreate Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergic_medicine);

        // Create User object
        if(getUser() == null)
            this.user = new User(this);

        // SearchView for filtering
        this.searchView = findViewById(R.id.searchViewAllergicMedicine);
        getSearchView().onActionViewExpanded();
        getSearchView().clearFocus();

        // RecyclerView initialization
        this.recyclerView = findViewById(R.id.allergicMedicineRecyclerView);
        getRecyclerView().setHasFixedSize(true);
        this.layoutManager =  new LinearLayoutManager(this);
        getRecyclerView().setLayoutManager(getLayoutManager());

        // Tool bar initialization
        this.toolBarText = findViewById(R.id.toolBarTextAllergicMedicine);
        this.toolbar = findViewById(R.id.toolbarAllergicMedicine);
        setSupportActionBar(toolbar);

        // Add data into RecyclerView
        showRecyclerView();

        // SearchBar function
        getSearchView().setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getAllergicMedicineAdapter().getFilter().filter(newText);
                return false;
            }
        });

    }
    //endregion

    //region Show RecyclerView in Allergic Medicine
    private void showRecyclerView(){
        this.allergicMedicineAdapter = new AllergicMedicineAdapter(getUser().getAllergicMedicines(), this, getRecyclerView());
        getRecyclerView().setAdapter(getAllergicMedicineAdapter());
    }
    //endregion


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.selectAllIcon){
            getAllergicMedicineAdapter().selectAll();
        }else if(id == R.id.deleteIcon){
            // To confirm the deletion
            openDialogBox();
        }
        return super.onOptionsItemSelected(item);
    }

    //region Dialog Box
    // Open dialog box after pressing delete icon
    private void openDialogBox(){
        int selectedCount = getAllergicMedicineAdapter().getSelectingCount();
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
                        getUser().deleteAllergicMedicine(getAllergicMedicineAdapter().getSelectedMedicines());
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


    @Override
    public void onBackPressed() {
        if(getSearchView().isFocused()){
            getSearchView().clearFocus();
        }else if (getAllergicMedicineAdapter().getSelectingCount() > 0){
            getAllergicMedicineAdapter().deselectAll();
            showDefaultToolBar();
        }else
            super.onBackPressed();
    }

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

    public void openAddAllergicMedicine(View view) {
        Intent intent = new Intent(this, NewAllergicMedicineActivity.class);
        startActivity(intent);
        finish();
    }

    // Back Icon click on toolbar
    public void backIconClick(View view) {
        onBackPressed();
    }


    //region Getters and Setters
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

    public AllergicMedicineAdapter getAllergicMedicineAdapter() {
        return allergicMedicineAdapter;
    }

    public void setAllergicMedicineAdapter(AllergicMedicineAdapter allergicMedicineAdapter) {
        this.allergicMedicineAdapter = allergicMedicineAdapter;
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

    public SearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }
    //endregion
}
