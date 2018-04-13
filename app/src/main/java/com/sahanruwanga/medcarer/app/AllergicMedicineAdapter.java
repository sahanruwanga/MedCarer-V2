package com.sahanruwanga.medcarer.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.AllergicMedicineActivity;

import java.util.List;

/**
 * Created by Sahan Ruwanga on 4/12/2018.
 */

public class AllergicMedicineAdapter extends
        RecyclerView.Adapter<AllergicMedicineAdapter.ViewHolder> {
    private List<AllergicMedicine> allergicMedicines;
    private AllergicMedicineActivity context;
    private RecyclerView recyclerView;

    public AllergicMedicineAdapter(List<AllergicMedicine> allergicMedicines,
                                   AllergicMedicineActivity context, RecyclerView recyclerView){
        this.allergicMedicines = allergicMedicines;
        this.context = context;
        this.recyclerView = recyclerView;
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
    }

    @Override
    public int getItemCount() {
        return allergicMedicines.size();
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
