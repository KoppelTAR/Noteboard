package com.example.noteboard.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.noteboard.AuthRepository;

public class UserDetailsViewModel extends AndroidViewModel {

    AuthRepository authRepository;

    public UserDetailsViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public void resetPassword(){
        authRepository.sendPasswordResetEmailForCurrentUser();
    }
}
