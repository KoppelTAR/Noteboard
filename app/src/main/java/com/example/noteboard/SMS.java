package com.example.noteboard;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;

import androidx.core.app.ActivityCompat;

public class SMS {
    public SmsManager smsManager = SmsManager.getDefault();

    public void sendSMS(String number, String content){
        smsManager.sendTextMessage(number,null, content, null, null);
    }
}
