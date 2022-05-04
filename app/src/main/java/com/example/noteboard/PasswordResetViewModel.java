package com.example.noteboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

public class PasswordResetViewModel extends AndroidViewModel {
    private final Repository repository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    public PasswordResetViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        userMutableLiveData = repository.getUserMutableLiveData();
    }
    public void resetPassword(String email){
        repository.sendPasswordResetEmail(email);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}