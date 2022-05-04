package com.example.noteboard.ViewModel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;

import com.example.noteboard.AuthRepository;

public class RegistreerimisViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;

    public RegistreerimisViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void userRegistration(String Username, String email, String password) {
        authRepository.userRegistration(Username, email, password);
    }

}