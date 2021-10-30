package com.example.hellobatterystatus.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BatteryStatusActivityReceiver extends BroadcastReceiver {
    private ProgressBar procentBar;
    private TextView procentText,chargeText;

    public BatteryStatusActivityReceiver(ProgressBar procentBar, TextView procentText, TextView chargeText) {
        this.procentBar = procentBar;
        this.procentText = procentText;
        this.chargeText = chargeText;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int level=intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int scale=intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
        float procentf=(float)level*100/(float)scale;
        int procent=(int)procentf;
        procentBar.setProgress(procent);
        procentText.setText(Integer.toString(procent));
        int status=intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
        int plugged=intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1);
        String resultCharge="";
        if(status==BatteryManager.BATTERY_STATUS_CHARGING || status==BatteryManager.BATTERY_STATUS_FULL)
        {
            resultCharge+="Charging";
            switch (plugged)
            {
                case BatteryManager.BATTERY_PLUGGED_USB:
                    resultCharge+=" (USB)";
                    break;
                case BatteryManager.BATTERY_PLUGGED_AC:
                    resultCharge+=" (AC)";
                    break;
                case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                    resultCharge+=" (WIRELESS)";
                    break;
            }
        }
        else
        {
            resultCharge="Not charging";
        }
        chargeText.setText(resultCharge);
    }
}
