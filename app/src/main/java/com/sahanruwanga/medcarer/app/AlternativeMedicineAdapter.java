package com.sahanruwanga.medcarer.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.AlternativeMedicineActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahan Ruwanga on 5/12/2018.
 */

public class AlternativeMedicineAdapter extends
        RecyclerView.Adapter<AlternativeMedicineAdapter.ViewHolder> implements Filterable {
    private List<AlternativeMedicine> alternativeMedicineList;
    private ArrayList<AlternativeMedicine> alternativeMedicineFilterList;
    private AlternativeMedicineActivity context;
    private RecyclerView recyclerView;
    private AlternativeMedicineFilter alternativeMedicineFilter;

    public AlternativeMedicineAdapter(List<AlternativeMedicine> alternativeMedicines, AlternativeMedicineActivity context,
                                      RecyclerView recyclerView){
        this.alternativeMedicineList = alternativeMedicines;
        this.context = context;
        this.recyclerView = recyclerView;

        // Filter array
        this.alternativeMedicineFilterList = new ArrayList<>();
        addToFilterList();
    }

    private void addToFilterList(){
        for(AlternativeMedicine alternativeMedicine : getAlternativeMedicineList()){
            getAlternativeMedicineFilterList().add(alternativeMedicine);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_alternative_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AlternativeMedicine alternativeMedicine = getAlternativeMedicineList().get(position);

        holder.medicine.setText(alternativeMedicine.getMedicine());
        holder.price.setText(alternativeMedicine.getPrice());
        holder.note.setText(alternativeMedicine.getNote());
    }

    @Override
    public int getItemCount() {
        return getAlternativeMedicineList().size();
    }

    @Override
    public Filter getFilter() {
        if(getAlternativeMedicineFilter() == null)
        {
            this.alternativeMedicineFilter = new AlternativeMedicineFilter(
                    getAlternativeMedicineFilterList(),this, getContext());
        }
        return getAlternativeMedicineFilter();
    }

    //region Getters and Setters
    public List<AlternativeMedicine> getAlternativeMedicineList() {
        return alternativeMedicineList;
    }

    public void setAlternativeMedicineList(List<AlternativeMedicine> alternativeMedicineList) {
        this.alternativeMedicineList = alternativeMedicineList;
    }

    public ArrayList<AlternativeMedicine> getAlternativeMedicineFilterList() {
        return alternativeMedicineFilterList;
    }

    public void setAlternativeMedicineFilterList(ArrayList<AlternativeMedicine> alternativeMedicineFilterList) {
        this.alternativeMedicineFilterList = alternativeMedicineFilterList;
    }

    public AlternativeMedicineActivity getContext() {
        return context;
    }

    public void setContext(AlternativeMedicineActivity context) {
        this.context = context;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public AlternativeMedicineFilter getAlternativeMedicineFilter() {
        return alternativeMedicineFilter;
    }

    public void setAlternativeMedicineFilter(AlternativeMedicineFilter alternativeMedicineFilter) {
        this.alternativeMedicineFilter = alternativeMedicineFilter;
    }
    //endregion

    public class ViewHolder extends RecyclerView.ViewHolder {
        View layout;
        TextView medicine;
        TextView price;
        TextView note;

        public ViewHolder(View itemView) {
            super(itemView);
            this.layout = itemView;
            this.medicine = itemView.findViewById(R.id.medicineAlternative);
            this.price = itemView.findViewById(R.id.priceAlternative);
            this.note = itemView.findViewById(R.id.descriptionAlternative);
        }
    }
}
