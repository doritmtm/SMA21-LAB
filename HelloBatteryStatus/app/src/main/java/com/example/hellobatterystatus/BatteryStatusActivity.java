package com.example.hellobatterystatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hellobatterystatus.broadcast.BatteryStatusActivityReceiver;
import com.example.hellobatterystatus.broadcast.PowerConnectionReceiver;

public class BatteryStatusActivity extends AppCompatActivity {
    private ProgressBar procentBar;
    private TextView procentText,chargeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        procentBar=findViewById(R.id.procentBar);
        procentText=findViewById(R.id.procentText);
        chargeText=findViewById(R.id.chargeText);
        BatteryStatusActivityReceiver batteryReceiver=new BatteryStatusActivityReceiver(procentBar,procentText,chargeText);
        IntentFilter filter1=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent1=registerReceiver(batteryReceiver,filter1);
        /*
        https://developer.android.com/guide/components/broadcasts
        Android 8.0
        Beginning with Android 8.0 (API level 26), the system imposes additional restrictions
        on manifest-declared receivers.

        If your app targets Android 8.0 or higher, you cannot use the manifest to declare
        a receiver for most implicit broadcasts (broadcasts that don't target your app specifically).
        You can still use a context-registered receiver when the user is actively using your app.
         */
        PowerConnectionReceiver powerReceiver=new PowerConnectionReceiver();
        IntentFilter filter2=new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        filter2.addAction(Intent.ACTION_POWER_DISCONNECTED);
        Intent intent2=registerReceiver(powerReceiver,filter2);
    }
}