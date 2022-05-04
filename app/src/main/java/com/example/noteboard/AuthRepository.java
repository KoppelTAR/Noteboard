package com.example.noteboard;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuthRepository {

    private static final String TAG = "Firebase: ";
    private FirebaseAuth mAuth;

    //firebase variables
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Mutable Livedata
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    private final Application application;

    public AuthRepository(Application application) {
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

                    //creates new collection named users if one doesnt exist into it add a new document with UID reference
                    DocumentReference documentReference = db.collection("users").document(userId);
                    Map<String,Object> user = new HashMap<>();
                    user.put("username",username);
                    user.put("email",email);
                    user.put("OwnedPosts", null);
                    documentReference.set(user).addOnSuccessListener(aVoid -> Log.i(TAG, "onSuccess: user data was saved"))
                            .addOnFailureListener(e -> Log.e(TAG, "onFaliure: Error writing to DB document", e));
                    userMutableLiveData.postValue(firebaseAuth.getCurrentUser());

                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser VerUser = mAuth.getCurrentUser();

                    VerUser.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                        Toast.makeText(application, "Email sent. Please verify email", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

            } else {
                Toast.makeText(application, application.getString(R.string.error, Registertask.getException().getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

}
