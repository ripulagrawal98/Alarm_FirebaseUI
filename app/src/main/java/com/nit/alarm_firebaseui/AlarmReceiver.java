package com.nit.alarm_firebaseui;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();

        System.out.println("Alarm Fired");
        Toast.makeText(context,"Alarm Fired",Toast.LENGTH_SHORT).show();

// Create an explicit intent for an Activity in your app
        Intent mintent = new Intent(context, AfterAlarm.class);
//        mintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mintent, PendingIntent.FLAG_UPDATE_CURRENT);
        System.out.println("Checking for pending intent "+pendingIntent);
//        nb.setContentIntent(pendingIntent)
//        .setAutoCancel(true);
        nb.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setFullScreenIntent(pendingIntent,true);
        notificationHelper.getManager().notify(1, nb.build());


    }
}
