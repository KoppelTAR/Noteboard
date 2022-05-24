package com.example.noteboard.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.noteboard.AuthRepository;
import com.example.noteboard.PostsRepository;

public class FindPostViewModel extends AndroidViewModel {
    PostsRepository repository;
    public FindPostViewModel(@NonNull Application application) {
        super(application);
        repository = new PostsRepository(application);
    }

    public void findPost(String sharingCode, Context context){
        repository.addPostThroughCode(sharingCode, context);
    }
}
