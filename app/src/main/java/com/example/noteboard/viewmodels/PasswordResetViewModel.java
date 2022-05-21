package com.example.noteboard.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.noteboard.AuthRepository;

public class PasswordResetViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    public PasswordResetViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }
    public void resetPassword(String email,String locale){
        authRepository.sendPasswordResetEmail(email,locale);
    }
}