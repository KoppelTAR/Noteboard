package com.example.noteboard;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.noteboard.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PostsRepository {

    private FirebaseAuth firebaseAuth;
    private final Application application;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final ArrayList<Post> postArrayList = new ArrayList<>();
    private final MutableLiveData<ArrayList<Post>> postLiveData;

    public PostsRepository(Application application){
        this.application = application;
        postLiveData = new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void clearPosts(){
        postArrayList.clear();
        postLiveData.setValue(postArrayList);
    }

    public void createPost(String postTitle, String postContent){
        db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Long currentMs = Calendar.getInstance().getTimeInMillis();
                DocumentReference docRefPost = db.collection("posts").document(currentMs.toString());
                Map<String,Object> post = new HashMap<>();
                post.put("content",postContent);
                post.put("title",postTitle);
                post.put("sharingCode", currentMs);
                post.put("postAuthor",task.getResult().getString("username"));
                post.put("editedAt", Calendar.getInstance().getTime());
                docRefPost.set(post).addOnSuccessListener(aVoid -> Log.i(TAG, String.valueOf(R.string.UserDataWasSaved)));
            }
        });
    }

    public void addPostThroughCode(String sharingCode, Context context){
    try{
        db.collection("posts").whereEqualTo("sharingCode", Long.parseLong(sharingCode)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                DocumentReference docRefPost = db.collection("posts").document(task.getResult().getDocuments().get(0).getId());
                DocumentReference docRefUser = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());
                docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<DocumentReference> ownPosts = (ArrayList<DocumentReference>) task.getResult().get("ownedPosts");
                        ArrayList<DocumentReference> array = (ArrayList<DocumentReference>) task.getResult().get("sharedPosts");
                        if(array.contains(docRefPost)){
                            Toast.makeText(context, context.getString(R.string.access_exists),Toast.LENGTH_SHORT).show();
                        }
                        else if(ownPosts.contains(docRefPost)){
                            Toast.makeText(context, context.getString(R.string.post_created_by_user),Toast.LENGTH_SHORT).show();
                        }
                        else{
                            array.add(docRefPost);
                            docRefUser.update("sharedPosts", array);
                            postLiveData.setValue(postArrayList);
                            Toast.makeText(context, context.getString(R.string.post_added),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    catch (Exception e){
        Toast.makeText(context, context.getString(R.string.invalid_code),Toast.LENGTH_SHORT).show();
    }
}


    public void getUserPosts(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<DocumentReference> ownedPostsList = (List<DocumentReference>) document.get("ownedPosts");
                            List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                            if (ownedPostsList != null) {
                                for (DocumentReference documentReference : ownedPostsList) {
                                    Task<DocumentSnapshot> documentSnapshotTask = documentReference.get();
                                    tasks.add(documentSnapshotTask);
                                }
                            }
                            Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                @Override
                                public void onSuccess(List<Object> list) {
                                    for (int i = 0; i < list.size(); i++) {

                                        DocumentSnapshot object = (DocumentSnapshot) list.get(i);

                                        Timestamp time = (Timestamp) object.get("editedAt");
                                        long dv = Long.valueOf(time.getSeconds())*1000;
                                        Date df = new Date(dv);

                                        Post post = new Post();
                                        post.setPostAuthor(object.getString("postAuthor"));
                                        post.setTitle(object.getString("title"));
                                        post.setEditedAt(df);
                                        Long s = object.getLong("sharingCode");
                                        post.setSharingCode(s);
                                        post.setContent(object.getString("content"));

                                        postArrayList.add(post);
                                        postLiveData.setValue(postArrayList);
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public void setTitle(TextView textView) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            db.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    String string = document.getString("username");
                    textView.setText(String.format(application.getString(R.string.postsTitle),string));
                }
            });
        }
    }

    public void SetUsername(TextView hellousernameTextView, String locale) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            db.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    Log.i("TAG", locale);
                    String newlocale = locale;
                    Locale locale = new Locale(newlocale);
                    Locale.setDefault(locale);
                    Configuration config = application.getResources().getConfiguration();
                    config.locale = locale;
                    application.getResources().updateConfiguration(config,
                            application.getResources().getDisplayMetrics());
                    String string = document.getString("username");
                    hellousernameTextView.setText(String.format(application.getString(R.string.hello_user),string));
                }
            });
        }
    }

    public void getPosts(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<DocumentReference> ownedPostsList = (List<DocumentReference>) document.get("ownedPosts");
                            List<DocumentReference> sharedPostsList = (List<DocumentReference>) document.get("sharedPosts");
                            List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                            if (ownedPostsList != null) {
                                for (DocumentReference documentReference : ownedPostsList) {
                                    Task<DocumentSnapshot> documentSnapshotTask = documentReference.get();
                                    tasks.add(documentSnapshotTask);
                                }
                            }
                            if (sharedPostsList != null) {
                                for (DocumentReference documentReference : sharedPostsList) {
                                    Task<DocumentSnapshot> documentSnapshotTask = documentReference.get();
                                    tasks.add(documentSnapshotTask);
                                }
                            }
                            Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                @Override
                                public void onSuccess(List<Object> list) {
                                    for (int i = 0; i < list.size(); i++) {
                                        Log.i("TAG", "onSuccess: "+list.get(i));

                                        DocumentSnapshot object = (DocumentSnapshot) list.get(i);

                                        Timestamp time = (Timestamp) object.get("editedAt");
                                        long dv = Long.valueOf(time.getSeconds())*1000;
                                        Date df = new Date(dv);

                                        Post post = new Post();
                                        post.setPostAuthor(object.getString("postAuthor"));
                                        post.setTitle(object.getString("title"));
                                        post.setEditedAt(df);
                                        Long s = object.getLong("sharingCode");
                                        post.setSharingCode(s);
                                        post.setContent(object.getString("content"));

                                        postArrayList.add(post);
                                        postLiveData.setValue(postArrayList);
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public MutableLiveData<ArrayList<Post>> getPostLiveData() {
        return postLiveData;
    }

}
