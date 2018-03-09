package com.sahanruwanga.medcarer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.MedicalRecord;
import com.sahanruwanga.medcarer.app.RecyclerViewAdapter;
import com.sahanruwanga.medcarer.helper.SQLiteHandler;

import java.util.LinkedList;
import java.util.List;

public class AllergicMedicineActivity extends AppCompatActivity {
    private SQLiteHandler sqLiteHandler;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergic_medicine);

        this.setRecyclerView((RecyclerView)findViewById(R.id.allergicRecyclerView));
        this.getRecyclerView().setHasFixedSize(true);
        this.setLayoutManager(new LinearLayoutManager(this));
        this.getRecyclerView().setLayoutManager(getLayoutManager());

//        show();

    }
    private List<MedicalRecord> getMD(){
        List<MedicalRecord> md = new LinkedList<>();
        MedicalRecord m;
        for(int i=0; i<20;i++){
            m = new MedicalRecord();
            m.setRecord_id(i);
            m.setDisease("Fever");
            m.setMedicine("Panadol");
            m.setDuration("Date");
            m.setAllergic("Yes");
            m.setDoctor("Sahan");
            m.setContact("0152");
            m.setDescription("Sample");
            md.add(m);
        }
        return md;
    }

    private void show(){
        //this.setSqLiteHandler(new SQLiteHandler(this));
//        this.setRecyclerViewAdapter(new RecyclerViewAdapter(getMD(), this, getRecyclerView()));
        this.getRecyclerView().setAdapter(getRecyclerViewAdapter());
    }

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

    public RecyclerViewAdapter getRecyclerViewAdapter() {
        return recyclerViewAdapter;
    }

    public void setRecyclerViewAdapter(RecyclerViewAdapter recyclerViewAdapter) {
        this.recyclerViewAdapter = recyclerViewAdapter;
    }
}
