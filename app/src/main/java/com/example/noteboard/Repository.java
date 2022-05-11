package com.example.noteboard;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.noteboard.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;

public class Repository {
    private FirebaseAuth firebaseAuth;
    private final Application application;
    private final MutableLiveData<Boolean> loggedOutMutableLiveData;
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    private final MutableLiveData<ArrayList<Post>> postLiveData;
    private final ArrayList<Post> postArrayList = new ArrayList<>();

    public Repository(Application application){
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
        loggedOutMutableLiveData = new MutableLiveData<>();
        userMutableLiveData = new MutableLiveData<>();
        postLiveData = new MutableLiveData<>();
        databaseReference = db.getReference("posts");

        if (firebaseAuth.getCurrentUser() != null){
            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutMutableLiveData.postValue(false);
            loadPostsData();
        }
    }


    public void loadPostsData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postsnap : snapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    postArrayList.add(post);
                    Log.i("TAG", post.getTitle());
                    postLiveData.setValue(postArrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
            /*
            String uid = firebaseAuth.getCurrentUser().getUid();
            DocumentReference doc = db.collection("posts").document(uid);
            doc.get()
                    .addOnSuccessListener(documentSnapshot -> {

                        Post post = documentSnapshot.toObject(Post.class);
                        postArrayList.add(post);
                        postLiveData.setValue(postArrayList);
                    }).addOnFailureListener(e ->
                    Toast.makeText(application, application.getString(R.string.error,e.getMessage()), Toast.LENGTH_SHORT).show());

             */
        }


    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<ArrayList<Post>> getPostLiveData() {
        return postLiveData;
    }

}
