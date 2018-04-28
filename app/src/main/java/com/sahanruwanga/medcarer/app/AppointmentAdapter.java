package com.sahanruwanga.medcarer.app;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.AppointmentActivity;
import com.sahanruwanga.medcarer.activity.ViewAppointmentActivity;

import java.util.ArrayList;
import java.util.List;

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
        holder.appointmentDate.setText(appointment.getDate());
        holder.appointmentTime.setText(getTimeFormat(appointment.getTime()));

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

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    notification = getNotification(appointment.getVenue(),
                            "You have an appointment at " + appointment.getTime());
                    Intent notificationIntent = new Intent(context, NotificationHandler.class);
                    int[] id = new int[1];
                    id[0] = appointment.getAppointmentId();
                    notificationIntent.putExtra(NotificationHandler.NOTIFICATION_ID, id);
                    notificationIntent.putExtra(NotificationHandler.NOTIFICATION, notification);
                    long futureInMillis = SystemClock.elapsedRealtime() + 5000;

                    pendingIntent = PendingIntent.getBroadcast(context, 0,
                            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            futureInMillis,
                            5000, pendingIntent);

                }else{
                    // If the alarm has been set, cancel it.
                    if (alarmManager!= null) {
                        alarmManager.cancel(pendingIntent);
                    }
                }
            }
        });

        // OnLongClick listener for each card view
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "Long click", Toast.LENGTH_LONG).show();

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
                notifyParent(getSelectingCount());
                return true;
            }
        });

        // OnClick listener for each card view
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSelectingCount() == 0) {
                    // OnClick activity on the list
//                    scheduleNotification(getNotification(appointment.getVenue(),
//                            "You have an appointment in " + appointment.getNotifyTime()),
//                            5000, appointment.getAppointmentId());
                    Intent intent = new Intent(context, ViewAppointmentActivity.class);
                    intent.putExtra("Appointment", appointment);
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
                notifyParent(getSelectingCount());
            }

        });

    }

    private String getTimeFormat(String time){
        if(Integer.parseInt(time.substring(0,2)) > 12 ){
            time = String.valueOf(Integer.parseInt(time.substring(0,2)) - 12) + time.substring(2, 5) + " PM";
        } else if(time.equals("00")){
            time = "12" + time.substring(2, 5) + " AM";
        }else{
            time = time.substring(0, 5) + " AM";
        }
        return time;
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

    private Notification getNotification(String title, String content) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_aboutme);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent intent = new Intent(context, AppointmentActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        // Single notification
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setGroup("Appointments");
        builder.setSmallIcon(R.drawable.ic_notification);
        // To show more than one notification
        builder.setLargeIcon(bitmap);
        builder.setStyle(new NotificationCompat.InboxStyle()
                .addLine("Alex Faaborg   Check this out")
                .addLine("Jeff Chang   Launch Party")
                .setBigContentTitle("2 Appointments"));
//                .setSummaryText("johndoe@gmail.com"));
        builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
        builder.setAutoCancel(true);
        return builder.build();
    }

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
