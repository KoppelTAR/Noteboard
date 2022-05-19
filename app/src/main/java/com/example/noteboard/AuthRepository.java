package com.example.noteboard;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuthRepository {
    private FirebaseAuth firebaseAuth;
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


    public void updateDataFromForm(EditText editTextEmail, EditText editTextUsername, EditText editTextConfirmPassword){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        user.reload();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail()
                ,editTextConfirmPassword.getText().toString());
        DocumentReference docRef = db.collection("users").document(user.getUid());

        if(!Utils.isEditTextEmpty(editTextEmail, application.getApplicationContext())
                && !Utils.isEditTextEmpty(editTextUsername, application.getApplicationContext())
                && !Utils.isEditTextEmpty(editTextConfirmPassword, application.getApplicationContext())){
            user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    docRef.update("username",editTextUsername.getText().toString());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (!editTextEmail.getText().toString()
                                    .equals(task.getResult().getString("email"))){
                                user.verifyBeforeUpdateEmail(editTextEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(application.getApplicationContext(), "Data successfully updated. Please verify new email.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                Toast.makeText(application.getApplicationContext(), "Data successfully updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(application.getApplicationContext(), "Error when getting data from database", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(application.getApplicationContext(), "Invalid password", Toast.LENGTH_SHORT).show();
                }
            });

        }
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

    public void sendPasswordResetEmailForCurrentUser(){
        firebaseAuth.sendPasswordResetEmail(firebaseAuth.getCurrentUser().getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(application, R.string.passRezSuccess,Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(application, application.getString(R.string.error, task.getException()
                                            .getMessage())
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void sendPasswordResetEmail(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(application, R.string.passRezSuccess,Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(application, application.getString(R.string.error, task.getException()
                                            .getMessage())
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void userRegistration(String username, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(application.getMainExecutor(),Registertask-> {
            //if user was created successfully then save data to firebase firestore
            if (Registertask.isSuccessful()){
                if (firebaseAuth.getCurrentUser() != null) {
                    //gets newly created users UID
                    String userId = firebaseAuth.getCurrentUser().getUid();
                    ArrayList<DocumentReference> posts = new ArrayList<DocumentReference>();

                    //creates new collection named users if one doesn't exist into it add a new document with UID reference
                    DocumentReference documentReference = db.collection("users").document(userId);
                    Map<String,Object> user = new HashMap<>();
                    user.put("username",username);
                    user.put("email",email);
                    user.put("ownedPosts", posts);
                    user.put("sharedPosts", posts);
                    documentReference.set(user).addOnSuccessListener(aVoid -> Log.i(TAG, String.valueOf(R.string.UserDataWasSaved)))
                            .addOnFailureListener(e -> Log.e(TAG, application.getString(R.string.onFaliureDbError), e));
                    userMutableLiveData.postValue(firebaseAuth.getCurrentUser());

                    EmailVerification();

                    Toast.makeText(application, "Registration successful", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(application, application.getString(R.string.error, Objects.requireNonNull(Registertask.getException()).getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void EmailVerification() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser VerUser = firebaseAuth.getCurrentUser();

        VerUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(application, application.getString(R.string.SuccessfulEmailVerSent), Toast.LENGTH_LONG).show();
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
