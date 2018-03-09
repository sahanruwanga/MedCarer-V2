package com.sahanruwanga.medcarer.app;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.MedicalHistoryActivity;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahan Ruwanga on 3/8/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<MedicalRecord> medicalRecords;
    private MedicalHistoryActivity context;
    private RecyclerView recyclerView;
    private int selectingCount;
    private ArrayList<LinearLayout> selectedLinearlayouts;

    public RecyclerViewAdapter(List<MedicalRecord> medicalRecords, MedicalHistoryActivity context, RecyclerView recyclerView){
        this.medicalRecords = medicalRecords;
        this.context = context;
        this.recyclerView = recyclerView;
        this.setSelectingCount(0);
        selectedLinearlayouts = new ArrayList<>();
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view  =inflater.inflate(R.layout.layout_history_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MedicalRecord medicalRecord = medicalRecords.get(position);
        holder.disease.setText("Disease: "+medicalRecord.getDisease());
        holder.medicine.setText(medicalRecord.getMedicine());
        holder.duration.setText(medicalRecord.getDuration());
        String allergic = medicalRecord.getAllergic();
        setCardColor(holder.layout, allergic);
        holder.allergic.setText(allergic);

        final LinearLayout[] linearLayout = new LinearLayout[1];
        linearLayout[0] = holder.layout.findViewById(R.id.mainLayout);
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "Long click", Toast.LENGTH_LONG).show();

                if(linearLayout[0].isSelected()) {
                    linearLayout[0].setBackgroundColor(Color.parseColor("#ffffff"));
                    setSelectingCount(getSelectingCount() - 1);
                    selectedLinearlayouts.remove(linearLayout[0]);
                    linearLayout[0].setSelected(false);
                }else{
                    linearLayout[0].setBackgroundColor(Color.parseColor("#FF53A7D4"));
                    setSelectingCount(getSelectingCount() + 1);
                    selectedLinearlayouts.add(linearLayout[0]);
                    linearLayout[0].setSelected(true);
                }
                notifyParent(getSelectingCount());
                return true;
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSelectingCount() == 0)
                    Toast.makeText(context, "Record id :" + String.valueOf(medicalRecord.getRecord_id()),
                            Toast.LENGTH_LONG).show();
                else{
                    if(linearLayout[0].isSelected()) {
                        linearLayout[0].setBackgroundColor(Color.parseColor("#ffffff"));
                        setSelectingCount(getSelectingCount() - 1);
                        selectedLinearlayouts.remove(linearLayout[0]);
                        linearLayout[0].setSelected(false);
                    }else{
                        linearLayout[0].setBackgroundColor(Color.parseColor("#FF53A7D4"));
                        setSelectingCount(getSelectingCount() + 1);
                        selectedLinearlayouts.add(linearLayout[0]);
                        linearLayout[0].setSelected(true);
                    }
                }
                notifyParent(getSelectingCount());
            }

        });
    }

    private void notifyParent(int selectingCount){
        if(getSelectingCount() == 0){
            context.showDefaultToolBar();
        }else{
            context.showDeletingToolBar();
        }
    }

    public void deseleceAll(){
        setSelectingCount(0);
        setSelectingCount(0);
        for(LinearLayout layout : selectedLinearlayouts){
            layout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        selectedLinearlayouts.clear();
    }

    private void setCardColor(View view, String allergic){
        CardView cardView = view.findViewById(R.id.recordRow);
        if(allergic.equals("Yes")){
            cardView.setCardBackgroundColor(Color.parseColor("#FF4081"));
        }else{
            cardView.setCardBackgroundColor(Color.parseColor("#848383"));
        }
    }

    @Override
    public int getItemCount() {
        return medicalRecords.size();
    }

    public void add(int position, MedicalRecord medicalRecord) {
        medicalRecords.add(position, medicalRecord);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        medicalRecords.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView disease;
        public TextView medicine;
        public TextView duration;
        public TextView allergic;
        public View layout;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            disease = itemView.findViewById(R.id.mrDisease);
            medicine = itemView.findViewById(R.id.mrMedicine);
            duration = itemView.findViewById(R.id.mrDate);
            allergic = itemView.findViewById(R.id.mrAllergic);
        }
    }

    //region Getters and Setters
    public int getSelectingCount() {
        return selectingCount;
    }

    public void setSelectingCount(int selectingCount) {
        this.selectingCount = selectingCount;
    }
    //endregion
}
