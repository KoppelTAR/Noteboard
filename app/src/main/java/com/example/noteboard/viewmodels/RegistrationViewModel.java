package com.example.noteboard.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.noteboard.AuthRepository;

public class RegistrationViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public void userRegistration(String username, String email, String password) {
        /*authRepository.userRegistration(username,email,password);*/
    }

}