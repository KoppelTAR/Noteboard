package com.example.noteboard;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.noteboard.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Iterator;

public class PostsRepository {
    private FirebaseAuth firebaseAuth;
    private final Application application;
    private final MutableLiveData<Boolean> loggedOutMutableLiveData;
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final FirebaseFirestore db2 = FirebaseFirestore.getInstance();
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    private final MutableLiveData<ArrayList<Post>> postLiveData;
    private final ArrayList<Post> postArrayList = new ArrayList<>();

    public PostsRepository(Application application){
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
        loggedOutMutableLiveData = new MutableLiveData<>();
        userMutableLiveData = new MutableLiveData<>();
        postLiveData = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null){
            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutMutableLiveData.postValue(false);
            loadPostsData();
        }
    }


    public void loadPostsData(){
        db2.collection("posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                postArrayList.add(dc.getDocument().toObject(Post.class));
                                Log.i("TAG", dc.getDocument().getId());
                            }
                        }
                    }
                });
        /*
        DocumentReference document = db2.collection("posts").document("1652266757168");
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Log.i("TAG", documentSnapshot.getString("title"));
                } else{
                    Log.i("TAG", "failed ");
                }
            }
        });

         */

        /*
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
         */


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
