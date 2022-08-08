package com.example.noteboard.viewmodels;

import android.app.Application;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.noteboard.AuthRepository;
import com.example.noteboard.PostsRepository;
import com.example.noteboard.models.Post;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;

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

    public void setPostContent(Long postId, TextView title, TextView content, TextView author, TextView lastEdit, TextView sharingCode){postsRepository.setPostContent(postId,title,content,author,lastEdit, sharingCode);}
    public void showUserPosts(){postsRepository.getUserPosts();}

    public void clearPosts(){postsRepository.clearPosts();}

    public void setPostsTitle(TextView textView){
        postsRepository.setTitle(textView);
    }

    public void showPosts(){
        postsRepository.getPosts();
    }

    public void showLastEdit(TextView textView, String userUID, Long postId,String locale){postsRepository.setLastEditor(textView,userUID, postId,locale);}
}