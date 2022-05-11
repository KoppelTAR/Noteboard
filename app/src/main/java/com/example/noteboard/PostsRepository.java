package com.example.noteboard;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PostsRepository {

    private FirebaseAuth firebaseAuth;
    private final Application application;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PostsRepository(Application application){
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void createPost(String postTitle, String postContent){
        Long currentMs = Calendar.getInstance().getTimeInMillis();
        DocumentReference docRefPost = db.collection("posts").document(currentMs.toString());
        Map<String,Object> post = new HashMap<>();
        post.put("content",postContent);
        post.put("title",postTitle);
        post.put("sharingCode", currentMs);
        post.put("editedAt", Calendar.getInstance().getTime());
        docRefPost.set(post).addOnSuccessListener(aVoid -> Log.i(TAG, String.valueOf(R.string.UserDataWasSaved)));

        DocumentReference docRefUser = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());
        docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList<DocumentReference> array = (ArrayList<DocumentReference>) task.getResult().get("ownedPosts");
                array.add(docRefPost);
                docRefUser.update("ownedPosts", array);
            }
        });


    }

}
