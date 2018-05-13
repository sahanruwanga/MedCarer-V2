package com.sahanruwanga.medcarer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.AlternativeMedicine;
import com.sahanruwanga.medcarer.app.AlternativeMedicineAdapter;
import com.sahanruwanga.medcarer.app.User;

import java.util.ArrayList;
import java.util.List;

public class AlternativeMedicineActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;

    private AlternativeMedicineAdapter alternativeMedicineAdapter;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternative_medicine);

        // Create User object
        if(getUser() == null)
            this.user = new User(this);

        // SearchView for filtering
        this.searchView = findViewById(R.id.searchViewAlternativeMedicine);
        getSearchView().onActionViewExpanded();
        getSearchView().clearFocus();

        // SearchBar function
        getSearchView().setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getAlternativeMedicineAdapter().getFilter().filter(newText);
                return false;
            }
        });

        // RecyclerView initialization
        this.recyclerView = findViewById(R.id.alternativeMedicineRecyclerView);
        getRecyclerView().setHasFixedSize(true);
        this.layoutManager =  new LinearLayoutManager(this);
        getRecyclerView().setLayoutManager(getLayoutManager());
        showRecyclerView();
    }

    private ArrayList<AlternativeMedicine> getList(){
        ArrayList<AlternativeMedicine> alternativeMedicines = new ArrayList<>();
        alternativeMedicines.add(new AlternativeMedicine(1, "panadol",
                "para", "2.50", "Smaple"));
        alternativeMedicines.add(new AlternativeMedicine(1, "Sitka",
                "para", "2.50", "Smaple ksdv vs ijsiisjiv sijjisjdv sij" +
                "sks sd is sids iijsdjijv sijijdks isjmsdv sSmaple ksdv vs ijsiisjiv sijjisjdv sij" +
                "sks sd is sids iijsdjijv sijijdks isjmsdv sSmaple ksdv vs ijsiisjiv sijjisjdv sij" +
                "sks sd is sids iijsdjijv sijijdks isjmsdv sSmaple ksdv vs ijsiisjiv sijjisjdv sij" +
                "sks sd is sids iijsdjijv sijijdks isjmsdv sSmaple ksdv vs ijsiisjiv sijjisjdv sij" +
                "sks sd is sids iijsdjijv sijijdks isjmsdv s"));
        alternativeMedicines.add(new AlternativeMedicine(1, "Yespin",
                "para1", "2.50", "Smaple"));
        alternativeMedicines.add(new AlternativeMedicine(1, "Lawania",
                "para1", "2.50", "Smaple ksdv vs ijsiisjiv sijjisjdv sij" +
                "sks sd is sids iijsdjijv sijijdks isjmsdv s"));
        alternativeMedicines.add(new AlternativeMedicine(1, "Paracetomol",
                "para", "2.50", "Smaple"));
        alternativeMedicines.add(new AlternativeMedicine(1, "Donkia",
                "para1", "2.50", "Smaple ksdv vs ijsiisjiv sijjisjdv sij" +
                "sks sd is sids iijsdjijv sijijdks isjmsdv sSmaple ksdv vs ijsiisjiv sijjisjdv sij" +
                "sks sd is sids iijsdjijv sijijdks isjmsdv sSmaple ksdv vs ijsiisjiv sijjisjdv sij" +
                "sks sd is sids iijsdjijv sijijdks isjmsdv sSmaple ksdv vs ijsiisjiv sijjisjdv sij" +
                "sks sd is sids iijsdjijv sijijdks isjmsdv sSmaple ksdv vs ijsiisjiv sijjisjdv sij" +
                "sks sd is sids iijsdjijv sijijdks isjmsdv sSmaple ksdv vs ijsiisjiv sijjisjdv sij" +
                "sks sd is sids iijsdjijv sijijdks isjmsdv s"));

        return alternativeMedicines;
    }

    //region Show RecyclerView in Allergic Medicine
    private void showRecyclerView(){
        this.alternativeMedicineAdapter = new AlternativeMedicineAdapter(getUser().getAlternativeMedicine(), this, getRecyclerView());
        getRecyclerView().setAdapter(getAlternativeMedicineAdapter());
    }
    //endregion

    // Back Icon click on toolbar
    public void backIconClick(View view) {
        onBackPressed();
    }

    // Refresh icon function
    public void refreshAlternativeMedicine(View view) {
        getUser().deleteAlternativeMedicineTable();
        getUser().storeAlternativeMedicineInSQLite();
        getAlternativeMedicineAdapter().setAlternativeMedicineList(getUser().getAlternativeMedicine());
        getAlternativeMedicineAdapter().notifyDataSetChanged();
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

    public SearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AlternativeMedicineAdapter getAlternativeMedicineAdapter() {
        return alternativeMedicineAdapter;
    }

    public void setAlternativeMedicineAdapter(AlternativeMedicineAdapter alternativeMedicineAdapter) {
        this.alternativeMedicineAdapter = alternativeMedicineAdapter;
    }

    //endregion
}
