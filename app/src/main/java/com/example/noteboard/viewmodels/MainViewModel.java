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

    private final PostsRepository postsRepository;
    private final MutableLiveData<ArrayList<Post>> postLiveData;
    private final AuthRepository authRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    private final MutableLiveData<Boolean> loggedOutMutableLiveData;


    public MainViewModel(@NonNull Application application) {
        super(application);
        postsRepository = new PostsRepository(application);
        authRepository = new AuthRepository(application);
        postLiveData = postsRepository.getPostLiveData();
        userMutableLiveData = authRepository.getUserMutableLiveData();
        loggedOutMutableLiveData = authRepository.getLoggedOutMutableLiveData();
    }

    public MutableLiveData<ArrayList<Post>> getPostLiveData() {
        return postLiveData;
    }
    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return authRepository.getUserMutableLiveData();
    }
    public MutableLiveData<Boolean> getLoggedOutMutableLiveData() {
        return loggedOutMutableLiveData;
    }

    public void showPosts(){
        postsRepository.getPosts();
    }
}