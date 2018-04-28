package com.sahanruwanga.medcarer.app;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.MedicalHistoryActivity;
import com.sahanruwanga.medcarer.activity.ViewMedicalRecordActivity;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahan Ruwanga on 3/8/2018.
 */

public class MedicalHistoryAdapter extends RecyclerView.Adapter<MedicalHistoryAdapter.ViewHolder>
        implements Filterable{
    private List<MedicalRecord> medicalRecords;
    private ArrayList<MedicalRecord> filterList;
    private MedicalHistoryActivity context;
    private RecyclerView recyclerView;
    CustomFilter filter;

    private int selectingCount;
    private ArrayList<MedicalRecord> selectedRecords;
    private ArrayList<ImageView> selectedImageViews;
    private ArrayList<ImageView> imageViews;

    public MedicalHistoryAdapter(List<MedicalRecord> medicalRecords, MedicalHistoryActivity context, RecyclerView recyclerView){
        this.setMedicalRecords(medicalRecords);
        this.context = context;
        this.recyclerView = recyclerView;
        // For the search filter
        filterList = new ArrayList<>();
        putToFilterList();

        this.setSelectingCount(0);
        this.selectedRecords = new ArrayList<>();
        this.selectedImageViews = new ArrayList<>();
        this.imageViews = new ArrayList<>();
    }

    private void putToFilterList(){
        for(MedicalRecord medicalRecord : getMedicalRecords()){
            filterList.add(medicalRecord);
        }
    }

    @Override
    public MedicalHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view  = inflater.inflate(R.layout.layout_history_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MedicalRecord medicalRecord = getMedicalRecords().get(position);
        holder.disease.setText("Disease: "+medicalRecord.getDisease());
        holder.medicine.setText(medicalRecord.getMedicine());
        holder.duration.setText(medicalRecord.getDuration());
        String allergic = medicalRecord.getAllergic();
        setCardColor(holder.layout, allergic);
        holder.allergic.setText(allergic);
        final ImageView checkIcon = holder.layout.findViewById(R.id.checkIconMH);
        imageViews.add(checkIcon);
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(checkIcon.isSelected()) {
                    setSelectingCount(getSelectingCount() - 1);
                    getSelectedRecords().remove(medicalRecord);
                    selectedImageViews.remove(checkIcon);
                    checkIcon.setVisibility(View.INVISIBLE);
                    checkIcon.setSelected(false);
                }else{
                    setSelectingCount(getSelectingCount() + 1);
                    getSelectedRecords().add(medicalRecord);
                    selectedImageViews.add(checkIcon);
                    checkIcon.setVisibility(View.VISIBLE);
                    checkIcon.setSelected(true);
                }
                notifyParent(getSelectingCount());
                return true;
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSelectingCount() == 0) {
                    Intent intent = new Intent(context, ViewMedicalRecordActivity.class);
                    intent.putExtra("MedicalRecord", medicalRecord);
                    context.startActivity(intent);
                }
                else{
                    if(checkIcon.isSelected()) {
                        setSelectingCount(getSelectingCount() - 1);
                        getSelectedRecords().remove(medicalRecord);
                        selectedImageViews.remove(checkIcon);
                        checkIcon.setVisibility(View.INVISIBLE);
                        checkIcon.setSelected(false);
                    }else{
                        setSelectingCount(getSelectingCount() + 1);
                        getSelectedRecords().add(medicalRecord);
                        selectedImageViews.add(checkIcon);
                        checkIcon.setVisibility(View.VISIBLE);
                        checkIcon.setSelected(true);
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
        for(ImageView imageView : selectedImageViews){
            imageView.setVisibility(View.INVISIBLE);
            imageView.setSelected(false);
        }
        getSelectedRecords().clear();
        selectedImageViews.clear();
    }

    public void selectAll(){
        for(ImageView imageView : imageViews){
            if (!selectedImageViews.contains(imageView)){
                selectedImageViews.add(imageView);
                imageView.setVisibility(View.VISIBLE);
                imageView.setSelected(true);
            }
        }
        for(MedicalRecord medicalRecord : getMedicalRecords()){
            if(!getSelectedRecords().contains(medicalRecord)){
                getSelectedRecords().add(medicalRecord);
            }
        }
        setSelectingCount(getItemCount());
    }


    private void setCardColor(View view, String allergic){
        CardView cardView = view.findViewById(R.id.cardViewMH);
        if(allergic.equals("Yes")){
            cardView.setCardBackgroundColor(Color.parseColor("#FFF10004"));
        }else{
            cardView.setCardBackgroundColor(Color.parseColor("#FF9893D6"));
        }
    }

    @Override
    public int getItemCount() {
        return getMedicalRecords().size();
    }

    public void add(int position, MedicalRecord medicalRecord) {
        getMedicalRecords().add(position, medicalRecord);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        getMedicalRecords().remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(getFilterList(),this);
        }
        return filter;
    }

    public List<MedicalRecord> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<MedicalRecord> filterList) {
        this.filterList = (ArrayList<MedicalRecord>) filterList;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
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
    public ArrayList<MedicalRecord> getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(ArrayList<MedicalRecord> selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public int getSelectingCount() {
        return selectingCount;
    }

    public void setSelectingCount(int selectingCount) {
        this.selectingCount = selectingCount;
    }
    //endregion
}
