package com.example.noteboard.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.noteboard.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class PasswordResetViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    public PasswordResetViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }
    public void resetPassword(String email){
        authRepository.sendPasswordResetEmail(email);
    }
}