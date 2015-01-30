package com.example.herry_wang.week4_media_player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Herry_Wang on 2015/1/30.
 */
public class Call extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String msg = "Phone state changed to " + state;

        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
        {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            msg += ". Incoming number is " + incomingNumber;
            Intent myIntent = new Intent(context, MyService.class);
            context.stopService(myIntent);
        }
        if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
        {
            Intent myIntent = new Intent(context, MyService.class);
            context.stopService(myIntent);
        }

        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE))
        {

            Intent myIntent = new Intent(context, MyService.class);
            context.startService(myIntent);
        }

        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}
