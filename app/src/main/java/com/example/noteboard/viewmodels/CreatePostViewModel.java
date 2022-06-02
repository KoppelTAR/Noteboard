package com.example.noteboard.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.noteboard.PostsRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class CreatePostViewModel extends AndroidViewModel {
    private final PostsRepository postsRepository;


    public CreatePostViewModel(@NonNull Application application) {
        super(application);
        postsRepository = new PostsRepository(application);
    }

    public Task createPost(String postTitle, String postContent){
        return postsRepository.createPost(postTitle, postContent);
    }

    public boolean isInDatabase(String docid) {
        return postsRepository.isInDatabase(docid);
    }


}
