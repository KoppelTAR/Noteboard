package com.example.noteboard.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.noteboard.AuthRepository;

public class SettingsViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;


    public SettingsViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }
}