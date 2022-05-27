package com.example.noteboard.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.noteboard.SMS;

public class SMSViewModel extends AndroidViewModel {

    SMS SMS;

    public SMSViewModel(@NonNull Application application) {
        super(application);
        SMS = new SMS();
    }

    public void sendSMS(String number, String content){
        SMS.sendSMS(number,content);
    }
}
