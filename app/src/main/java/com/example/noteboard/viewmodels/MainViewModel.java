package com.example.noteboard.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.noteboard.AuthRepository;
import com.example.noteboard.PostsRepository;
import com.example.noteboard.models.Post;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final PostsRepository repository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    private final MutableLiveData<ArrayList<Post>> postLiveData;
    private final MutableLiveData<Boolean> loggedOutMutableLiveData;



    public MainViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        repository = new PostsRepository(application);
        userMutableLiveData = authRepository.getUserMutableLiveData();
        postLiveData = repository.getPostLiveData();
        loggedOutMutableLiveData = authRepository.getLoggedOutMutableLiveData();
    }
    public void logOut(){ authRepository.logOut();}
    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return authRepository.getUserMutableLiveData();
    }
    public MutableLiveData<ArrayList<Post>> getPostLiveData() {
        return repository.getPostLiveData();
    }
    public MutableLiveData<Boolean> getLoggedOutMutableLiveData() {
        return loggedOutMutableLiveData;
    }

}