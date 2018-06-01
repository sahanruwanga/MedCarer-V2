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
import com.sahanruwanga.medcarer.activity.ViewMedicationScheduleActivity;
import com.sahanruwanga.medcarer.helper.DateTimeFormatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahan Ruwanga on 4/12/2018.
 */

public class MedicationScheduleAdapter extends
        RecyclerView.Adapter<MedicationScheduleAdapter.ViewHolder> {
    private List<MedicationSchedule> medicationSchedules;
    private MedicationScheduleActivity context;
    private RecyclerView recyclerView;

    private int selectingCount;
    private ArrayList<MedicationSchedule> selectedSchedules;
    private ArrayList<ImageView> selectedImageViews;
    private ArrayList<ImageView> imageViews;
    private ArrayList<Switch> switches;

    private DateTimeFormatting dateTimeFormatting;

    private static final int NOTIFICATION_STATUS_ON = 1;
    private static final int NOTIFICATION_STATUS_OFF = 0;

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

        this.dateTimeFormatting = new DateTimeFormatting();
    }

    @Override
    public MedicationScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_schedule_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MedicationScheduleAdapter.ViewHolder holder, int position) {
        final MedicationSchedule medicationSchedule = medicationSchedules.get(position);

        holder.medicine.setText(medicationSchedule.getMedicine());
        holder.quantity.setText(medicationSchedule.getQuantity());
        holder.startedAt.setText(getDateTimeFormatting().getNextTimeToTakeMedicine(
                medicationSchedule.getNextNotifyTime(), medicationSchedule.getNotifyTime()));

        int notificationStatus = medicationSchedule.getNotificationStatus();
        if(notificationStatus == NOTIFICATION_STATUS_ON)
            holder.scheduleSwitch.setChecked(true);
        else
            holder.scheduleSwitch.setChecked(false);

        getImageViews().add(holder.scheduleCheckIcon);

        getSwitches().add(holder.scheduleSwitch);

        // Set OnClick listeners for layouts
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSelectingCount() == 0) {
                    Intent intent = new Intent(getContext(), ViewMedicationScheduleActivity.class);
                    intent.putExtra(MedicationSchedule.MEDICATION_SCHEDULE, medicationSchedule);
                    getContext().startActivityForResult(intent, 1);
                }else {
                    if(holder.scheduleCheckIcon.isSelected()) {
                        setSelectingCount(getSelectingCount() - 1);
                        getSelectedSchedules().remove(medicationSchedule);
                        selectedImageViews.remove(holder.scheduleCheckIcon);
                        if(getSelectingCount() == 0)
                            setVisibleSwitch(View.VISIBLE);
                        holder.scheduleCheckIcon.setVisibility(View.GONE);
                        holder.scheduleCheckIcon.setSelected(false);
                    }else{
                        setSelectingCount(getSelectingCount() + 1);
                        getSelectedSchedules().add(medicationSchedule);
                        selectedImageViews.add(holder.scheduleCheckIcon);
                        if(getSelectingCount() == 1)
                            setVisibleSwitch(View.GONE);
                        holder.scheduleCheckIcon.setVisibility(View.VISIBLE);
                        holder.scheduleCheckIcon.setSelected(true);
                    }
                }
                notifyParent();
            }
        });

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(holder.scheduleCheckIcon.isSelected()) {
                    setSelectingCount(getSelectingCount() - 1);
                    getSelectedSchedules().remove(medicationSchedule);
                    getSelectedImageViews().remove(holder.scheduleCheckIcon);
                    if(getSelectingCount() == 0)
                        setVisibleSwitch(View.VISIBLE);
                    holder.scheduleCheckIcon.setVisibility(View.GONE);
                    holder.scheduleCheckIcon.setSelected(false);
                }else{
                    setSelectingCount(getSelectingCount() + 1);
                    getSelectedSchedules().add(medicationSchedule);
                    getSelectedImageViews().add(holder.scheduleCheckIcon);
                    if(getSelectingCount() == 1)
                        setVisibleSwitch(View.GONE);
                    holder.scheduleCheckIcon.setVisibility(View.VISIBLE);
                    holder.scheduleCheckIcon.setSelected(true);
                }
                notifyParent();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicationSchedules.size();
    }

    // set switches invisible when check icons are visible or other way around
    private void setVisibleSwitch(int visibility){
        for(Switch aSwitch : getSwitches()){
            aSwitch.setVisibility(visibility);
        }
    }

    // Notify parent activity about change in selecting
    private void notifyParent(){
        if(getSelectingCount() == 0){
            context.showDefaultToolBar();
        }else{
            context.showDeletingToolBar();
        }
    }

    //region SelectAll and DeselectAll functions
    public void deseleceAll(){
        setSelectingCount(0);
        for(ImageView imageView : selectedImageViews){
            imageView.setVisibility(View.GONE);
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
                aSwitch.setVisibility(View.GONE);
        }
        for(ImageView imageView : imageViews){
            if (!selectedImageViews.contains(imageView)){
                selectedImageViews.add(imageView);
                imageView.setVisibility(View.VISIBLE);
                imageView.setSelected(true);
            }
        }
        for(MedicationSchedule medicationSchedule : getSelectedSchedules()){
            if(!getSelectedSchedules().contains(medicationSchedule)){
                getSelectedSchedules().add(medicationSchedule);
            }
        }
        setSelectingCount(getItemCount());
    }
    //endregion

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

    public DateTimeFormatting getDateTimeFormatting() {
        return dateTimeFormatting;
    }

    public void setDateTimeFormatting(DateTimeFormatting dateTimeFormatting) {
        this.dateTimeFormatting = dateTimeFormatting;
    }
    //endregion

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        public TextView medicine;
        public TextView quantity;
        public TextView startedAt;
        public Switch scheduleSwitch;
        public ImageView scheduleCheckIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            medicine = itemView.findViewById(R.id.scheduleMedicine);
            quantity = itemView.findViewById(R.id.scheduleQuantity);
            startedAt = itemView.findViewById(R.id.scheduleStartedAt);
            scheduleSwitch = itemView.findViewById(R.id.scheduleSwitch);
            scheduleCheckIcon = itemView.findViewById(R.id.scheduleCheckIcon);
        }
    }
}
