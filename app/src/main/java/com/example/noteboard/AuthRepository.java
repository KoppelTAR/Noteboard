package com.example.noteboard;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuthRepository {

    private static final String TAG = "Firebase: ";
    private final FirebaseAuth firebaseAuth;
    private final Application application;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<FirebaseUser> userMutableLiveData;

    public AuthRepository(Application application){
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
        userMutableLiveData = new MutableLiveData<>();
    }

    public void userRegistration(String username, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(application.getMainExecutor(),Registertask-> {
            //if user was created successfully then save data to firebase firestore
            if (Registertask.isSuccessful()){
                if (firebaseAuth.getCurrentUser() != null) {
                    //gets newly created users UID
                    String userId = firebaseAuth.getCurrentUser().getUid();
                    long unixTime = System.currentTimeMillis() /1000;

                    //creates new collection named users if one doesn't exist into it add a new document with UID reference
                    DocumentReference documentReference = db.collection("users").document(userId);
                    Map<String,Object> user = new HashMap<>();
                    user.put("username",username);
                    user.put("email",email);
                    user.put("OwnedPosts", null);
                    user.put("CreatedAt", unixTime);
                    documentReference.set(user).addOnSuccessListener(aVoid -> Log.i(TAG, "onSuccess: user data was saved"))
                            .addOnFailureListener(e -> Log.e(TAG, "onFailure: Error writing to DB document", e));
                    userMutableLiveData.postValue(firebaseAuth.getCurrentUser());

                    //TODO UNCOMMENT EmailVerification();
                }

            } else {
                Toast.makeText(application, application.getString(R.string.error, Objects.requireNonNull(Registertask.getException()).getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }



}
