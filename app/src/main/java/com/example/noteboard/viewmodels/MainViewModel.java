package com.example.noteboard.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.noteboard.AuthRepository;
import com.example.noteboard.Repository;
import com.example.noteboard.models.Post;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final Repository repository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    private final MutableLiveData<ArrayList<Post>> postLiveData;



    public MainViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        repository = new Repository(application);
        userMutableLiveData = authRepository.getUserMutableLiveData();
        postLiveData = repository.getPostLiveData();
    }


    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return authRepository.getUserMutableLiveData();
    }

    public MutableLiveData<ArrayList<Post>> getPostLiveData() {
        return repository.getPostLiveData();
    }
}