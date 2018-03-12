package com.sahanruwanga.medcarer.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.AppointmentActivity;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Sahan Ruwanga on 3/11/2018.
 */

public class NotificationHandler extends BroadcastReceiver{
    public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION = "notification";
    private Appointment appointment;
    private Context context;
    private final static String APPOINTMENT_BODY = "You have an appointment in ";


    public void displayAppointmentNotification(){
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(getContext());
        Intent intent = new Intent(context, AppointmentActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        noBuilder.setContentIntent(pendingIntent);
        noBuilder.setSmallIcon(R.drawable.ic_notification);
        noBuilder.setContentTitle(this.getAppointment().getVenue());
        noBuilder.setContentText(APPOINTMENT_BODY + this.getAppointment().getNotifyTime());
        noBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
//        noBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        noBuilder.setAutoCancel(true);

        noBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(001, noBuilder.build());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int[] id = intent.getIntArrayExtra(NOTIFICATION_ID);
        notificationManager.notify(id[0], notification);
    }

    //region Getters and Setters
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    //endregion
}
