package com.example.noteboard.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.noteboard.PostsRepository;

public class DeletionConfirmationViewModel extends AndroidViewModel {
    private final PostsRepository postsRepository;

    public DeletionConfirmationViewModel(@NonNull Application application) {
        super(application);
        postsRepository = new PostsRepository(application);
    }

    public void DeletePost(long id) {
        postsRepository.DeletePost(id);
    }
}