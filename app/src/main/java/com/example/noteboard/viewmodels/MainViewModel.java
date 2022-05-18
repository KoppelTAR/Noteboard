package com.example.noteboard.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.noteboard.PostsRepository;
import com.example.noteboard.models.Post;

import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {

    private final PostsRepository postsRepository;
    private final MutableLiveData<ArrayList<Post>> postLiveData;


    public MainViewModel(@NonNull Application application) {
        super(application);
        postsRepository = new PostsRepository(application);
        postLiveData = postsRepository.getPostLiveData();
    }

    public MutableLiveData<ArrayList<Post>> getPostLiveData() {
        return postLiveData;
    }

    public void showUserPosts(){postsRepository.getUserPosts();}

    public void showPosts(){
        postsRepository.getPosts();
    }
}