package com.example.noteboard.viewmodels;

import android.app.Application;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.noteboard.AuthRepository;
import com.example.noteboard.PostsRepository;
import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    private final PostsRepository postsRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;


    public UserViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        postsRepository = new PostsRepository(application);
        userMutableLiveData = authRepository.getUserMutableLiveData();
    }

    public void logout(){
        authRepository.logOut();
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return authRepository.getUserMutableLiveData();
    }



    public void showUsername(TextView textview){
        postsRepository.SetUsername(textview);
    }

}