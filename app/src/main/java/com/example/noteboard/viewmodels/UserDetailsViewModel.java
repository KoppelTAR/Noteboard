package com.example.noteboard.viewmodels;

import android.app.Application;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.noteboard.AuthRepository;

public class UserDetailsViewModel extends AndroidViewModel {

    AuthRepository authRepository;

    public UserDetailsViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public void updateData(EditText editTextEmail, EditText editTextUserName, EditText editTextPassword){
        authRepository.updateDataFromForm(editTextEmail,editTextUserName,editTextPassword);
    }

    public void showCurrentUserUsername(EditText editText){authRepository.setCurrentUserUserNameEditText(editText);}

    public void showCurrentUserEmail(EditText editText){authRepository.setCurrentUserEmailEditText(editText);}

    public void resetPassword(){
        authRepository.sendPasswordResetEmailForCurrentUser();
    }
}
