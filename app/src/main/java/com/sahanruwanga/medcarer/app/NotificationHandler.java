package com.sahanruwanga.medcarer.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.AppointmentActivity;
import com.sahanruwanga.medcarer.activity.HomeActivity;

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



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intentNotification = new Intent(context, HomeActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomeActivity.class);
        stackBuilder.addNextIntent(intentNotification);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = builder.setContentTitle("Appointment")
                .setContentText("Sample")
                .setSmallIcon(R.drawable.ic_notification)
                .setTicker("New Appointment")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

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
