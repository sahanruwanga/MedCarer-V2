package com.sahanruwanga.medcarer.app;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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
    private ArrayList<LinearLayout> selectedLinearLayouts;

    public AppointmentAdapter(List<Appointment> appointments, AppointmentActivity context, RecyclerView recyclerView){
        this.appointments = appointments;
        this.context = context;
        this.recyclerView = recyclerView;
        this.setSelectingCount(0);
        selectedLinearLayouts = new ArrayList<>();
    }

    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view  =inflater.inflate(R.layout.layout_appointment_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Appointment appointment = appointments.get(position);

        // Show data in layout
        holder.appointmentVenue.setText(appointment.getVenue());
        holder.appointmentDate.setText(appointment.getDate());
        holder.appointmentTime.setText(appointment.getTime());

        // Make Notification switch on
//        holder.appointmentSwitch.setChecked(true);




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

        final LinearLayout[] linearLayout = new LinearLayout[1];
        linearLayout[0] = holder.layout.findViewById(R.id.mainLayoutAppointmentList);

        // OnLongClick listener for each card view
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "Long click", Toast.LENGTH_LONG).show();

                if(linearLayout[0].isSelected()) {
                    linearLayout[0].setBackgroundColor(Color.parseColor("#ffffff"));
                    setSelectingCount(getSelectingCount() - 1);
                    selectedLinearLayouts.remove(linearLayout[0]);
                    linearLayout[0].setSelected(false);
                }else{
                    linearLayout[0].setBackgroundColor(Color.parseColor("#FF53A7D4"));
                    setSelectingCount(getSelectingCount() + 1);
                    selectedLinearLayouts.add(linearLayout[0]);
                    linearLayout[0].setSelected(true);
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
                    if(linearLayout[0].isSelected()) {
                        linearLayout[0].setBackgroundColor(Color.parseColor("#ffffff"));
                        setSelectingCount(getSelectingCount() - 1);
                        selectedLinearLayouts.remove(linearLayout[0]);
                        linearLayout[0].setSelected(false);
                    }else{
                        linearLayout[0].setBackgroundColor(Color.parseColor("#FF53A7D4"));
                        setSelectingCount(getSelectingCount() + 1);
                        selectedLinearLayouts.add(linearLayout[0]);
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
        for(LinearLayout layout : selectedLinearLayouts){
            layout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        selectedLinearLayouts.clear();
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void add(int position, Appointment appointment) {
        appointments.add(position, appointment);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        appointments.remove(position);
        notifyItemRemoved(position);
    }

    private void scheduleNotification(Notification notification, int delay, int appointment_id) {
        Intent notificationIntent = new Intent(context, NotificationHandler.class);
        int[] id = new int[1];
        id[0]=appointment_id;
        notificationIntent.putExtra(NotificationHandler.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotificationHandler.NOTIFICATION, notification);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + 5000,
//                5000, pendingIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent);
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

    //region ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder{
        public View layout;
        public TextView appointmentVenue;
        public TextView appointmentDate;
        public TextView appointmentTime;
        public Switch appointmentSwitch;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            appointmentVenue = itemView.findViewById(R.id.appointmentVenue);
            appointmentDate = itemView.findViewById(R.id.appointmentDate);
            appointmentTime = itemView.findViewById(R.id.appointmentTime);
            appointmentSwitch = itemView.findViewById(R.id.appointmentSwitch);
        }
    }
    //endregion

    //region Getters and Setters
    public int getSelectingCount() {
        return selectingCount;
    }

    public void setSelectingCount(int selectingCount) {
        this.selectingCount = selectingCount;
    }
    //endregion
}
