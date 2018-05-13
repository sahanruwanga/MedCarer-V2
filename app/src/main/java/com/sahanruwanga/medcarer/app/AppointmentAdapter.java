package com.sahanruwanga.medcarer.app;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.AppointmentActivity;
import com.sahanruwanga.medcarer.activity.ViewAppointmentActivity;
import com.sahanruwanga.medcarer.helper.DateTimeFormatting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;


/**
 * Created by Sahan Ruwanga on 3/11/2018.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private List<Appointment> appointments;
    private AppointmentActivity context;
    private RecyclerView recyclerView;

    private int selectingCount;
    private ArrayList<Appointment> selectedAppointments;
    private ArrayList<ImageView> selectedImageViews;
    private ArrayList<ImageView> imageViews;
    private ArrayList<Switch> switches;

    private DateTimeFormatting dateTimeFormatting;

    private static final int NOTIFICATION_STATUS_ON = 1;
    private static final int NOTIFICATION_STATUS_OFF = 0;

    public AppointmentAdapter(List<Appointment> appointments, AppointmentActivity context, RecyclerView recyclerView){
        this.appointments = appointments;
        this.context = context;
        this.recyclerView = recyclerView;

        this.setSelectingCount(0);
        this.selectedAppointments = new ArrayList<>();
        this.selectedImageViews = new ArrayList<>();
        this.imageViews = new ArrayList<>();
        this.switches = new ArrayList<>();
        if(appointments.isEmpty()){
            getContext().showEmptyMessage(View.VISIBLE);
        }else
            getContext().showEmptyMessage(View.INVISIBLE);

        this.dateTimeFormatting = new DateTimeFormatting();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view  =inflater.inflate(R.layout.layout_appointment_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Appointment appointment = appointments.get(position);

        // Show data in layout
        holder.appointmentVenue.setText(appointment.getVenue());
        holder.appointmentDate.setText(getDateTimeFormatting().getDateToShowInUI(appointment.getDate()));
        holder.appointmentTime.setText(getDateTimeFormatting().getTimeToShowInUI(appointment.getTime()));

        // Make Notification switch on
        int notificationStatus = appointment.getNotificationStatus();
        if(notificationStatus == NOTIFICATION_STATUS_ON)
            holder.appointmentSwitch.setChecked(true);
        else
            holder.appointmentSwitch.setChecked(false);

        getImageViews().add(holder.checkIcon);

        getSwitches().add(holder.appointmentSwitch);

        // Call for turning on and off notification
        holder.appointmentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            // Define objects to set notification
            Notification notification;
            PendingIntent pendingIntent;
            AlarmManager alarmManager;

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    setAlarmNoification(appointment);

                }else{
                    // If the alarm has been set, cancel it.

                }
            }
        });

        // OnLongClick listener for each card view
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if(holder.checkIcon.isSelected()) {
                    setSelectingCount(getSelectingCount() - 1);
                    getSelectedAppointments().remove(appointment);
                    getSelectedImageViews().remove(holder.checkIcon);
                    if(getSelectingCount() == 0)
                        setVisibleSwitch(View.VISIBLE);
                    holder.checkIcon.setVisibility(View.GONE);
                    holder.checkIcon.setSelected(false);
                }else{
                    setSelectingCount(getSelectingCount() + 1);
                    getSelectedAppointments().add(appointment);
                    getSelectedImageViews().add(holder.checkIcon);
                    if(getSelectingCount() == 1)
                        setVisibleSwitch(View.GONE);
                    holder.checkIcon.setVisibility(View.VISIBLE);
                    holder.checkIcon.setSelected(true);
                }
                notifyParent();
                return true;
            }
        });

        // OnClick listener for each card view
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSelectingCount() == 0) {
                    Intent intent = new Intent(context, ViewAppointmentActivity.class);
                    intent.putExtra(Appointment.APPOINTMENT, appointment);
                    context.startActivity(intent);
                }
                else{
                    if(holder.checkIcon.isSelected()) {
                        setSelectingCount(getSelectingCount() - 1);
                        getSelectedAppointments().remove(appointment);
                        selectedImageViews.remove(holder.checkIcon);
                        if(getSelectingCount() == 0)
                            setVisibleSwitch(View.VISIBLE);
                        holder.checkIcon.setVisibility(View.GONE);
                        holder.checkIcon.setSelected(false);
                    }else{
                        setSelectingCount(getSelectingCount() + 1);
                        getSelectedAppointments().add(appointment);
                        selectedImageViews.add(holder.checkIcon);
                        if(getSelectingCount() == 1)
                            setVisibleSwitch(View.GONE);
                        holder.checkIcon.setVisibility(View.VISIBLE);
                        holder.checkIcon.setSelected(true);
                    }
                }
                notifyParent();
            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setAlarmNoification(Appointment appointment){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                calendar.setTime(sdfDate.parse(appointment.getDate()+ " " + appointment.getNotifyTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        Intent intent = new Intent("com.sahanruwanga.medcarer.app.DISPLAY_NOTIFICATION");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    @Override
    public int getItemCount() {
        return appointments.size();
    }

    // set switches invisible when check icons are visible or other way around
    private void setVisibleSwitch(int visibility){
        for(Switch aSwitch : getSwitches()){
            aSwitch.setVisibility(visibility);
        }
    }

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
        getSelectedAppointments().clear();
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
        for(Appointment appointment : getSelectedAppointments()){
            if(!getSelectedAppointments().contains(appointment)){
                getSelectedAppointments().add(appointment);
            }
        }
        setSelectingCount(getItemCount());
    }
    //endregion

    //region Getter and Setters
    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public AppointmentActivity getContext() {
        return context;
    }

    public void setContext(AppointmentActivity context) {
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

    public ArrayList<Appointment> getSelectedAppointments() {
        return selectedAppointments;
    }

    public void setSelectedAppointments(ArrayList<Appointment> selectedAppointments) {
        this.selectedAppointments = selectedAppointments;
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        public View layout;
        public TextView appointmentVenue;
        public TextView appointmentDate;
        public TextView appointmentTime;
        public Switch appointmentSwitch;
        public ImageView checkIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            appointmentVenue = itemView.findViewById(R.id.appointmentVenue);
            appointmentDate = itemView.findViewById(R.id.appointmentDate);
            appointmentTime = itemView.findViewById(R.id.appointmentTime);
            appointmentSwitch = itemView.findViewById(R.id.appointmentSwitch);
            checkIcon = itemView.findViewById(R.id.appointmentCheckIcon);
        }
    }
}
