package com.example.noteboard.viewmodels;

import android.app.Application;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.noteboard.PostsRepository;

public class EditPostViewModel extends AndroidViewModel {

    private final PostsRepository postsRepository;

    public EditPostViewModel(@NonNull Application application) {
        super(application);
        postsRepository = new PostsRepository(application);
    }

    public void setPostContent(Long postId, TextView title, TextView content, TextView author, TextView lastEdit, TextView sharingCode){postsRepository.setPostContent(postId,title,content,author,lastEdit, sharingCode);}

    public void SaveChanges(String Title, String Content, Long Id) {
        postsRepository.SaveEditedPostChanges(Title,Content,Id);
    }
}