package com.ameskate.fingo;

import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;

public final class NotificationUtils {

    private NotificationUtils(){}

    public static void showNotification(Context context, int iconId, String title, String text){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(iconId)
                .setContentTitle(title)
                .setTicker(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL);
        NotificationManagerCompat mNotificationManager = (NotificationManagerCompat.from(context));
        mNotificationManager.notify(0, builder.build());
    }

    public static void showAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, listener)
                .setCancelable(false)
                .show();
    }
}
