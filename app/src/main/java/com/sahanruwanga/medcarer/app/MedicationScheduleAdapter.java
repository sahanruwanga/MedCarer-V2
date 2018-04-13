package com.sahanruwanga.medcarer.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.MedicationScheduleActivity;

import java.util.List;

/**
 * Created by Sahan Ruwanga on 4/12/2018.
 */

public class MedicationScheduleAdapter extends
        RecyclerView.Adapter<MedicationScheduleAdapter.ViewHolder> {
    private List<MedicationSchedule> medicationSchedules;
    private MedicationScheduleActivity context;
    private RecyclerView recyclerView;

    public MedicationScheduleAdapter(List<MedicationSchedule> medicationSchedules,
                                     MedicationScheduleActivity context, RecyclerView recyclerView){
        this.medicationSchedules = medicationSchedules;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    public MedicationScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_schedule_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MedicationScheduleAdapter.ViewHolder holder, int position) {
        final MedicationSchedule medicationSchedule = medicationSchedules.get(position);

        holder.medicine.setText(medicationSchedule.getMedicine());
        holder.quantity.setText(medicationSchedule.getQuantity());
        holder.startedAt.setText(medicationSchedule.getStartTime());
    }

    @Override
    public int getItemCount() {
        return medicationSchedules.size();
    }

    //region Getters and Setters
    public List<MedicationSchedule> getMedicationSchedules() {
        return medicationSchedules;
    }

    public void setMedicationSchedules(List<MedicationSchedule> medicationSchedules) {
        this.medicationSchedules = medicationSchedules;
    }

    public MedicationScheduleActivity getContext() {
        return context;
    }

    public void setContext(MedicationScheduleActivity context) {
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
        public TextView quantity;
        public TextView startedAt;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            medicine = itemView.findViewById(R.id.scheduleMedicine);
            quantity = itemView.findViewById(R.id.scheduleQuantity);
            startedAt = itemView.findViewById(R.id.scheduleStartedAt);
        }
    }
}
