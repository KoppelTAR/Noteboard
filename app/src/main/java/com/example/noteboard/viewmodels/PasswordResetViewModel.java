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
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    public PasswordResetViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        userMutableLiveData = authRepository.getUserMutableLiveData();
    }
    public void resetPassword(String email){
        authRepository.sendPasswordResetEmail(email);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}