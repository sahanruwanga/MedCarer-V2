package com.sahanruwanga.medcarer.app;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.MedicationScheduleActivity;
import com.sahanruwanga.medcarer.activity.UpdateMedicationScheduleActivity;

import java.util.ArrayList;
import java.util.List;

public class MedicationScheduleAdapter extends RecyclerView.Adapter<MedicationScheduleAdapter.ViewHolder> {
    private List<MedicationSchedule> medicationSchedules;
    private MedicationScheduleActivity context;
    private RecyclerView recyclerView;

    private int selectingCount;
    private ArrayList<MedicationSchedule> selectedSchedules;
    private ArrayList<ImageView> selectedImageViews;
    private ArrayList<ImageView> imageViews;
    private ArrayList<Switch> switches;

    public MedicationScheduleAdapter(List<MedicationSchedule> medicationSchedules,
                                     MedicationScheduleActivity context, RecyclerView recyclerView){
        this.medicationSchedules = medicationSchedules;
        this.context = context;
        this.recyclerView = recyclerView;

        this.setSelectingCount(0);
        this.selectedSchedules = new ArrayList<>();
        this.selectedImageViews = new ArrayList<>();
        this.imageViews = new ArrayList<>();
        this.switches = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view  = inflater.inflate(R.layout.layout_schedule_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MedicationSchedule medicationSchedule = getMedicationSchedules().get(position);
        holder.medicine.setText(medicationSchedule.getMedicine());
        String quantity = medicationSchedule.getQuantity();
        String period = medicationSchedule.getPeriod();
        String perTime = "";
        if(!period.substring(0, 2).equals("00"))
            perTime += period.substring(0, 2) + "hrs";
        if(!period.substring(3, 5).equals("00"))
            perTime += period.substring(3, 5) + " min";
        if(!period.substring(6).equals("00"))
            perTime += period.substring(6) + " sec";
        holder.quantity.setText( quantity + " per" + perTime);

        holder.startedAt.setText("Started at " + medicationSchedule.getStartTime());

        final ImageView checkIcon = holder.layout.findViewById(R.id.scheduleCheckIcon);
        getImageViews().add(checkIcon);

        getSwitches().add((Switch) holder.layout.findViewById(R.id.scheduleSwitch));

        // SetOnLongClick listener for selecting items
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(checkIcon.isSelected()) {
                    setSelectingCount(getSelectingCount() - 1);
                    getSelectedSchedules().remove(medicationSchedule);
                    getSelectedImageViews().remove(checkIcon);
                    if(getSelectingCount() == 0)
                        setVisibleSwitch(View.VISIBLE);
                    checkIcon.setVisibility(View.INVISIBLE);
                    checkIcon.setSelected(false);
                }else{
                    setSelectingCount(getSelectingCount() + 1);
                    getSelectedSchedules().add(medicationSchedule);
                    getSelectedImageViews().add(checkIcon);
                    if(getSelectingCount() == 1)
                        setVisibleSwitch(View.INVISIBLE);
                    checkIcon.setVisibility(View.VISIBLE);
                    checkIcon.setSelected(true);
                }
                notifyParent(getSelectingCount());
                return true;
            }
        });

        // SetOnClickListener for displaying full details or selecting
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSelectingCount() == 0) {
                    Intent intent = new Intent(context, UpdateMedicationScheduleActivity.class);
                    intent.putExtra("MedicationSchedule", medicationSchedule);
                    context.startActivity(intent);
                }
                else{
                    if(checkIcon.isSelected()) {
                        setSelectingCount(getSelectingCount() - 1);
                        getSelectedSchedules().remove(medicationSchedule);
                        selectedImageViews.remove(checkIcon);
                        if(getSelectingCount() == 0)
                            setVisibleSwitch(View.VISIBLE);
                        checkIcon.setVisibility(View.INVISIBLE);
                        checkIcon.setSelected(false);
                    }else{
                        setSelectingCount(getSelectingCount() + 1);
                        getSelectedSchedules().add(medicationSchedule);
                        selectedImageViews.add(checkIcon);
                        if(getSelectingCount() == 1)
                            setVisibleSwitch(View.INVISIBLE);
                        checkIcon.setVisibility(View.VISIBLE);
                        checkIcon.setSelected(true);
                    }
                }
                notifyParent(getSelectingCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    // set switches invisible when check icons are visible or other way around
    private void setVisibleSwitch(int visibility){
        for(Switch aSwitch : getSwitches()){
            aSwitch.setVisibility(visibility);
        }
    }

    // Notify parent to change the toolbar icons
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
        for(Switch aSwitch : getSwitches()){
            aSwitch.setVisibility(View.VISIBLE);
        }
        getSelectedSchedules().clear();
        selectedImageViews.clear();
    }

    public void selectAll(){
        for(Switch aSwitch : getSwitches()){
            if(aSwitch.getVisibility() == View.VISIBLE)
                aSwitch.setVisibility(View.INVISIBLE);
        }
        for(ImageView imageView : imageViews){
            if (!selectedImageViews.contains(imageView)){
                selectedImageViews.add(imageView);
                imageView.setVisibility(View.VISIBLE);
                imageView.setSelected(true);
            }
        }
        for(MedicationSchedule medicationSchedule : getMedicationSchedules()){
            if(!getSelectedSchedules().contains(medicationSchedule)){
                getSelectedSchedules().add(medicationSchedule);
            }
        }
        setSelectingCount(getItemCount());
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

    public int getSelectingCount() {
        return selectingCount;
    }

    public void setSelectingCount(int selectingCount) {
        this.selectingCount = selectingCount;
    }

    public ArrayList<MedicationSchedule> getSelectedSchedules() {
        return selectedSchedules;
    }

    public void setSelectedSchedules(ArrayList<MedicationSchedule> selectedSchedules) {
        this.selectedSchedules = selectedSchedules;
    }

    public ArrayList<ImageView> getSelectedImageViews() {
        return selectedImageViews;
    }

    public void setSelectedImageViews(ArrayList<ImageView> selectedImageViews) {
        this.selectedImageViews = selectedImageViews;
    }

    public ArrayList<ImageView> getImageViews() {
        return imageViews;
    }

    public void setImageViews(ArrayList<ImageView> imageViews) {
        this.imageViews = imageViews;
    }

    public ArrayList<Switch> getSwitches() {
        return switches;
    }

    public void setSwitches(ArrayList<Switch> switches) {
        this.switches = switches;
    }
    //endregion

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView medicine;
        public TextView quantity;
        public TextView startedAt;
        public Switch scheduleSwitch;
        public ImageView checkIcon;
        public View layout;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            medicine = itemView.findViewById(R.id.scheduleMedicine);
            quantity = itemView.findViewById(R.id.scheduleQuantity);
            startedAt = itemView.findViewById(R.id.scheduleStartedAt);
            scheduleSwitch = itemView.findViewById(R.id.scheduleSwitch);
            checkIcon = itemView.findViewById(R.id.scheduleCheckIcon);

        }
    }
}
