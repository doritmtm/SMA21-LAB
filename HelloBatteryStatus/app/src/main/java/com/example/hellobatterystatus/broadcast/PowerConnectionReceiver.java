package com.example.hellobatterystatus.broadcast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.hellobatterystatus.R;

public class PowerConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String resultCharge="";
        if(intent.getAction().equals(Intent.ACTION_POWER_CONNECTED))
        {
            resultCharge="Charging";
        }
        if(intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED))
        {
            resultCharge="Not Charging";
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notifChannel=new NotificationChannel("ch01","Channel01", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notifManager=context.getSystemService(NotificationManager.class);
            notifManager.createNotificationChannel(notifChannel);
            NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"ch01")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Battery Status")
                    .setContentText(resultCharge)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            notifManager.notify(1,builder.build());
        }


    }
}
