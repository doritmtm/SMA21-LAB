package com.example.hellobatterystatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hellobatterystatus.broadcast.BatteryStatusActivityReceiver;

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
        IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent=registerReceiver(batteryReceiver,filter);
    }
}