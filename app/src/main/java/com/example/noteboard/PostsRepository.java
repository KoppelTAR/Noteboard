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
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
        DocumentReference userDocRef = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                Long currentMs = Calendar.getInstance().getTimeInMillis();
                DocumentReference docRefPost = db.collection("posts").document(currentMs.toString());
                Map<String,Object> post = new HashMap<>();
                post.put("content",postContent);
                post.put("title",postTitle);
                post.put("sharingCode", currentMs);
                post.put("postAuthor", firebaseAuth.getCurrentUser().getUid());
                post.put("editedAt", Timestamp.now());
                post.put("editedBy", firebaseAuth.getCurrentUser().getUid());
                docRefPost.set(post).addOnSuccessListener(aVoid -> Log.i(TAG, String.valueOf(R.string.UserDataWasSaved)));

                userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<DocumentReference> array = (ArrayList<DocumentReference>) task.getResult().get("ownedPosts");
                        array.add(docRefPost);
                        userDocRef.update("ownedPosts", array);
                        postLiveData.setValue(postArrayList);
                        }
                    });
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                }
            });
    }

    public void addPostThroughCode(String sharingCode, Context context){
    try{
        db.collection("posts").whereEqualTo("sharingCode", Long.parseLong(sharingCode)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            DocumentReference docRefPost;
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                try{
                    docRefPost = db.collection("posts").document(task.getResult().getDocuments().get(0).getId());
                }
                catch(Exception e){
                    Toast.makeText(context, context.getString(R.string.invalid_code),Toast.LENGTH_SHORT).show();
                    return;
                }
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

    public void SaveEditedPostChanges(String title, String content, Long id) {
        DocumentReference docRefPost = db.collection("posts").document(id.toString());
        docRefPost.update("content",content);
        docRefPost.update("title",title);
        docRefPost.update("editedAt", Timestamp.now());
        docRefPost.update("editedBy", firebaseAuth.getCurrentUser().getUid());
        docRefPost.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Toast.makeText(application, R.string.EditedChangesSaved, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getUserPosts(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            DocumentReference reference = db.collection("users").document(uid);
            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                        if (object.getLong("sharingCode") != null) {
                                            Timestamp time = (Timestamp) object.get("editedAt");
                                            /*long dv = Long.valueOf(time.getSeconds())*1000;
                                            Date df = new Date(dv);*/
                                            Long s = object.getLong("sharingCode");
                                            Post post = new Post();
                                            post.setPostAuthor(object.getString("postAuthor"));
                                            post.setTitle(object.getString("title"));
                                            post.setEditedAt(object.getTimestamp("editedAt"));
                                            post.setSharingCode(s);
                                            post.setContent(object.getString("content"));

                                            postArrayList.add(post);
                                            postLiveData.setValue(postArrayList);
                                        } else if (object.getLong("sharingCode") == null){
                                            ownedPostsList.remove(i);
                                            list.remove(i);
                                            i--;
                                        }
                                    }
                                    reference.update("ownedPosts", ownedPostsList);
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

    public void setPostContent(Long postId, TextView title, TextView content){
        db.collection("posts").document(postId.toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                title.setText(task.getResult().getString("title"));
                content.setText(task.getResult().getString("content"));
            }
        });
    }

    public void setLastEditor(TextView textView, String userUID, Long postId){
        DocumentReference userDocRef = db.collection("users").document(userUID);
        DocumentReference postDocRef = db.collection("posts").document(postId.toString());
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String username;
                if(task.getResult().getString("username")==null){
                    username = application.getString(R.string.deleted_user);
                }
                else{
                    username = task.getResult().getString("username");
                }

                postDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Map<TimeUnit,Long> editTimeDiff = Utils.getDiffBetweenDates(new Date(),task.getResult().getTimestamp("editedAt").toDate());

                        if(editTimeDiff.get(TimeUnit.DAYS) == 0
                                && editTimeDiff.get(TimeUnit.HOURS) == 0
                                && editTimeDiff.get(TimeUnit.MINUTES) == 0){
                            textView.setText(String.format(application.getString(R.string.lastEdit),username,application.getString(R.string.just_now)));
                        }
                        else if(editTimeDiff.get(TimeUnit.DAYS) == 0
                                && editTimeDiff.get(TimeUnit.HOURS) == 0
                                && editTimeDiff.get(TimeUnit.MINUTES) < 0){
                            textView.setText(String.format(application.getString(R.string.lastEdit),username,
                                    String.format(application.getString(R.string.minutes_ago),
                                            String.valueOf(Math.abs(editTimeDiff.get(TimeUnit.MINUTES))))));
                        }
                        else if(editTimeDiff.get(TimeUnit.DAYS) == 0
                                && editTimeDiff.get(TimeUnit.HOURS) < 0){
                            textView.setText(String.format(application.getString(R.string.lastEdit),username,
                                    String.format(application.getString(R.string.hours_ago),
                                            String.valueOf(Math.abs(editTimeDiff.get(TimeUnit.HOURS))))));
                        }
                        else if(editTimeDiff.get(TimeUnit.DAYS)<0){
                            textView.setText(String.format(application.getString(R.string.lastEdit),username,
                                    String.format(application.getString(R.string.days_ago),
                                            String.valueOf(Math.abs(editTimeDiff.get(TimeUnit.DAYS))))));
                        }
                    }
                });

            }
        });
    }

    public static void findAndSetUsername(TextView textView, String uid, Context context){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("users").document(uid);

        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String username = task.getResult().getString("username");
                if(username != null){
                    textView.setText(String.format(context.getString(R.string.byAuthor),username));
                }
                if(username == null){
                    textView.setText(String.format(context.getString(R.string.byAuthor),context.getString(R.string.deleted_user)));
                }
            }
        });
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
            DocumentReference reference = db.collection("users").document(uid);
            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<DocumentReference> ownedPostsList = (List<DocumentReference>) document.get("ownedPosts");
                            List<DocumentReference> sharedPostsList = (List<DocumentReference>) document.get("sharedPosts");
                            List<Task<DocumentSnapshot>> tasksOwnedPosts = new ArrayList<>();
                            List<Task<DocumentSnapshot>> tasksSharedPosts = new ArrayList<>();
                            if (ownedPostsList != null) {
                                for (DocumentReference documentReference : ownedPostsList) {
                                        Task<DocumentSnapshot> documentSnapshotTask = documentReference.get();
                                        tasksOwnedPosts.add(documentSnapshotTask);
                                }
                            }
                            if (sharedPostsList != null) {
                                for (DocumentReference documentReference : sharedPostsList) {
                                        Task<DocumentSnapshot> documentSnapshotTask = documentReference.get();
                                        tasksSharedPosts.add(documentSnapshotTask);
                                }

                            }

                            Tasks.whenAllSuccess(tasksOwnedPosts).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                @Override
                                public void onSuccess(List<Object> listOwned) {
                                    Log.i(TAG, "BEGIN LIST: "+ownedPostsList);
                                    Log.i(TAG, "STARTED ON SUCCESS: ");
                                    for (int i = 0; i < listOwned.size(); i++) {
                                        DocumentSnapshot object = (DocumentSnapshot) listOwned.get(i);
                                        if (object.getLong("sharingCode") != null) {
                                            Long s = object.getLong("sharingCode");
                                            Post post = new Post();
                                            post.setEditedBy(object.getString("editedBy"));
                                            post.setPostAuthor(object.getString("postAuthor"));
                                            post.setTitle(object.getString("title"));
                                            post.setEditedAt(object.getTimestamp("editedAt"));

                                            post.setSharingCode(s);
                                            post.setContent(object.getString("content"));

                                            postArrayList.add(post);
                                            postLiveData.setValue(postArrayList);
                                        } else {
                                            Log.i(TAG, "sharingCode: "+ object.getLong("sharingCode"));
                                            ownedPostsList.remove(i);
                                            listOwned.remove(i);
                                            i--;
                                        }
                                    }
                                    Log.i(TAG, "LIST: "+ ownedPostsList);
                                    reference.update("ownedPosts", ownedPostsList);
                                }
                            });
                            Tasks.whenAllSuccess(tasksSharedPosts).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                @Override
                                public void onSuccess(List<Object> listShared) {
                                    for (int i = 0; i < listShared.size(); i++) {
                                        Log.i("TAG", "onSuccess: "+listShared.get(i));

                                        DocumentSnapshot object = (DocumentSnapshot) listShared.get(i);
                                        if (object.getLong("sharingCode") != null) {
                                            Long s = object.getLong("sharingCode");
                                            Post post = new Post();
                                            post.setEditedBy(object.getString("editedBy"));
                                            post.setPostAuthor(object.getString("postAuthor"));
                                            post.setTitle(object.getString("title"));
                                            post.setEditedAt(object.getTimestamp("editedAt"));

                                            post.setSharingCode(s);
                                            post.setContent(object.getString("content"));

                                            postArrayList.add(post);
                                            postLiveData.setValue(postArrayList);
                                        } else {
                                            sharedPostsList.remove(i);
                                            listShared.remove(i);
                                            i--;
                                        }
                                    }
                                    reference.update("sharedPosts", sharedPostsList);
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


    public void DeletePost(Long id) {
        db.collection("posts").document(id.toString())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(application.getApplicationContext(), application.getString(R.string.PostDeleted), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(application.getApplicationContext(), application.getString(R.string.errorDeletingPost), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
