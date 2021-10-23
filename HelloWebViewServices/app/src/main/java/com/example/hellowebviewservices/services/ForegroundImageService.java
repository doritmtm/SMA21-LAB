package com.example.hellowebviewservices.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.hellowebviewservices.ImageActivity;
import com.example.hellowebviewservices.MyApplication;
import com.example.hellowebviewservices.R;

public class ForegroundImageService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notifChannel=new NotificationChannel("ch01","Channel 1", NotificationManager.IMPORTANCE_DEFAULT);
            notifChannel.setDescription("Description of Channel 1.");
            NotificationManager notifManager=getSystemService(NotificationManager.class);
            notifManager.createNotificationChannel(notifChannel);
        }

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"ch01");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Foreground Service")
                .setContentText("Processing...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        startForeground(1,builder.build());

    }
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(intent.getAction().equals("myact.startforegroundsvc"))
        {
            DownloadImageThread down=new DownloadImageThread(intent,(MyApplication) getApplication());
        }
        if(intent.getAction().equals("myact.stopforegroundsvc"))
        {
            Intent intent1=new Intent(this, ImageActivity.class);
            startActivity(intent1);
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }
}
