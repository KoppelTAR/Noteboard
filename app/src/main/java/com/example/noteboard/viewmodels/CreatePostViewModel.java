package com.example.noteboard.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.noteboard.PostsRepository;

public class CreatePostViewModel extends AndroidViewModel {
    private final PostsRepository postsRepository;


    public CreatePostViewModel(@NonNull Application application) {
        super(application);
        postsRepository = new PostsRepository(application);
    }

    public void createPost(String postTitle, String postContent){
        postsRepository.createPost(postTitle, postContent);
    }


}
