package com.sahanruwanga.medcarer.app;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.AppointmentActivity;

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
        holder.reason.setText("Reason: "+appointment.getReason());
        holder.date.setText(appointment.getDate());
        holder.time.setText(appointment.getTime());
        holder.venue.setText("Disease: "+appointment.getVenue());
        holder.doctor.setText(appointment.getDoctor());
        holder.clinicContact.setText(appointment.getClinicContact());
        holder.notifyTime.setText(appointment.getNotifyTime());

        final LinearLayout[] linearLayout = new LinearLayout[1];
        linearLayout[0] = holder.layout.findViewById(R.id.mainLayoutAppointmentList);
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
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSelectingCount() == 0) {
                    // OnClick activity on the list
                    scheduleNotification(getNotification(appointment.getVenue(),
                            "You have an appointment in " + appointment.getNotifyTime()),
                            5000, appointment.getAppointmentId());
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
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, 0); //PendingIntent.FLAG_UPDATE_CURRENT

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
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
        public TextView reason;
        public TextView date;
        public TextView time;
        public TextView venue;
        public TextView doctor;
        public TextView clinicContact;
        public TextView notifyTime;
        public View layout;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            reason = itemView.findViewById(R.id.appointmentReason);
            date = itemView.findViewById(R.id.appointmentDate);
            time = itemView.findViewById(R.id.appointmentTime);
            venue = itemView.findViewById(R.id.appointmentVenue);
            doctor = itemView.findViewById(R.id.appointmentDoctor);
            clinicContact = itemView.findViewById(R.id.appointmentClinicContact);
            notifyTime = itemView.findViewById(R.id.appointmentNotifyTime);
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
