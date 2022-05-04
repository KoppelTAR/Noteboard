package com.example.noteboard.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.noteboard.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;


    public LoginViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        userMutableLiveData = authRepository.getUserMutableLiveData();
    }

    public void logIn(String email, String password){
        authRepository.logIn(email, password);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return authRepository.getUserMutableLiveData();
    }

}
