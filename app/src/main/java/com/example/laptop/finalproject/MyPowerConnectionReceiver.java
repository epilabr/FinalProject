package com.example.laptop.finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/*this receiver class is to give indication if the device was plugged in or disconnected from charging*/


public class MyPowerConnectionReceiver extends BroadcastReceiver {
    public MyPowerConnectionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {  //if received by intent connected toasts "charging"
            Toast.makeText(context, "The device is charging", Toast.LENGTH_SHORT).show();
        } else {
            intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED);   //if received by intent disconnected toasts "not charging"
            Toast.makeText(context, "The device is not charging/unplugged", Toast.LENGTH_SHORT).show();
        }
    }
}
