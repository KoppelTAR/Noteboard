package com.example.noteboard.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.navigation.NavController;

import com.example.noteboard.AuthRepository;

public class DeleteUserViewModel extends AndroidViewModel {
    AuthRepository authRepository;
    public DeleteUserViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public void deleteCurrentUser(EditText confirmPassword, NavController navController, Activity activity){
        authRepository.deleteCurrentUser(confirmPassword, navController, activity);
    }
}
