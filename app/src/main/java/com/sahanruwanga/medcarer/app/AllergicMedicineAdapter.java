package com.sahanruwanga.medcarer.app;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.AllergicMedicineActivity;
import com.sahanruwanga.medcarer.activity.UpdateAllergicMedicineActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahan Ruwanga on 4/12/2018.
 */

public class AllergicMedicineAdapter extends
        RecyclerView.Adapter<AllergicMedicineAdapter.ViewHolder> implements Filterable {
    private List<AllergicMedicine> allergicMedicines;
    private ArrayList<AllergicMedicine> filterList;
    private AllergicMedicineActivity context;
    private RecyclerView recyclerView;
    private AllergicMedicineFilter allergicMedicineFilter;

    public AllergicMedicineAdapter(List<AllergicMedicine> allergicMedicines,
                                   AllergicMedicineActivity context, RecyclerView recyclerView){
        this.allergicMedicines = allergicMedicines;
        this.context = context;
        this.recyclerView = recyclerView;
        this.filterList = new ArrayList<>();

        putToFilterList();
    }

    private void putToFilterList(){
        for(AllergicMedicine allergicMedicine : getAllergicMedicines()){
            getFilterList().add(allergicMedicine);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_allergic_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AllergicMedicine allergicMedicine = allergicMedicines.get(position);

        holder.medicine.setText(allergicMedicine.getMedicineName());
        holder.description.setText(allergicMedicine.getDescription());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UpdateAllergicMedicineActivity.class);
                intent.putExtra(AllergicMedicine.ALLERGIC_MEDICINE, allergicMedicine);
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allergicMedicines.size();
    }

    @Override
    public Filter getFilter() {
        if(getAllergicMedicineFilter() == null)
        {
            this.allergicMedicineFilter = new AllergicMedicineFilter(getFilterList(),this);
        }
        return getAllergicMedicineFilter();
    }

    //region Getters and Setters
    public List<AllergicMedicine> getAllergicMedicines() {
        return allergicMedicines;
    }

    public void setAllergicMedicines(List<AllergicMedicine> allergicMedicines) {
        this.allergicMedicines = allergicMedicines;
    }

    public AllergicMedicineActivity getContext() {
        return context;
    }

    public void setContext(AllergicMedicineActivity context) {
        this.context = context;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public AllergicMedicineFilter getAllergicMedicineFilter() {
        return allergicMedicineFilter;
    }

    public void setAllergicMedicineFilter(AllergicMedicineFilter allergicMedicineFilter) {
        this.allergicMedicineFilter = allergicMedicineFilter;
    }

    public ArrayList<AllergicMedicine> getFilterList() {
        return filterList;
    }

    public void setFilterList(ArrayList<AllergicMedicine> filterList) {
        this.filterList = filterList;
    }
    //endregion

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        public TextView medicine;
        public TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            medicine = itemView.findViewById(R.id.medicineAllergic);
            description = itemView.findViewById(R.id.descriptionAllergic);
        }
    }
}
