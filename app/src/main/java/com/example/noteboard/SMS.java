package com.example.noteboard;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;

import androidx.core.app.ActivityCompat;

public class SMS {
    public SmsManager smsManager = SmsManager.getDefault();

    public void checkSMSPermission(Activity activity){
        if(ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS},1);
        }
    }

    public void sendSMS(String number, String content){
        smsManager.sendTextMessage(number,null, content, null, null);
    }
}
