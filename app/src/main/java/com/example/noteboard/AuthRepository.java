package com.example.noteboard;

import static android.content.ContentValues.TAG;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuthRepository {
    private final FirebaseAuth firebaseAuth;
    private final Application application;
    private final MutableLiveData<Boolean> loggedOutMutableLiveData;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<FirebaseUser> userMutableLiveData;

    public AuthRepository(Application application){
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
        loggedOutMutableLiveData = new MutableLiveData<>();
        userMutableLiveData = new MutableLiveData<>();
    }

    public void logIn(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(application.getMainExecutor(),task -> {
                    if(task.isSuccessful()){
                        if(firebaseAuth.getCurrentUser().isEmailVerified()){
                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                        }
                        else {
                            Toast.makeText(application.getApplicationContext(), application.getString(R.string.verifyEmail),Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(application, application.getString(R.string.loginError), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void sendPasswordResetEmail(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(application,"Password reset email was successfully sent!",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(application, application.getString(R.string.error, task.getException()
                                            .getMessage())
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void logOut(){
        firebaseAuth.signOut();
        loggedOutMutableLiveData.postValue(true);
    }


    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutMutableLiveData() {
        return loggedOutMutableLiveData;
    }



}
