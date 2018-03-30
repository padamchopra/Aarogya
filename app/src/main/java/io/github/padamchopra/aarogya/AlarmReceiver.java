package io.github.padamchopra.aarogya;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by padamchopra on 19/3/18.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private String mContent = "is about to expire";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String mTitle = "Hey there,";
        if (checkForExpiries(context)) {

            long when = System.currentTimeMillis();
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                    context).setSmallIcon(R.drawable.aarogya_launcher)
                    .setContentTitle(mTitle)
                    .setContentText(mContent).setSound(alarmSound)
                    .setAutoCancel(true).setWhen(when)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            notificationManager.notify(1, mNotifyBuilder.build());
        }
    }

    public boolean checkForExpiries(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Aarogya", Context.MODE_PRIVATE);
        String[] medicineNames = sharedPreferences.getString("medicinenames", "").split("newmed");
        String[] expiryDates = sharedPreferences.getString("medicineexpiries", "").split(" ");
        for (int i = 0; i < expiryDates.length; i++) {
            if (checkDateifBefore(expiryDates[i].split("/"))) {
                mContent = medicineNames[i] + " " + mContent;
                return true;
            }
        }
        return false;
    }

    public boolean checkDateifBefore(String[] expiryDate) {
        String[] todayDate = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH).format(new Date()).split("/");
        int year = Integer.parseInt(todayDate[2]);
        int month = Integer.parseInt(todayDate[1]);
        int date = Integer.parseInt(todayDate[0]);
        if (year == Integer.parseInt(expiryDate[2])) {
            if (month == Integer.parseInt(expiryDate[1])) {
                if (date >= Integer.parseInt(expiryDate[0])-2) {
                    return true;
                } else {
                    return false;
                }
            } else if (month > Integer.parseInt(expiryDate[1])) {
                return true;
            } else {
                return false;
            }
        } else if (year > Integer.parseInt(expiryDate[2])) {
            return true;
        } else {
            return false;
        }
    }
}
