package com.example.noteboard;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
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

    public void logIn(String email, String password,String localeString){
        String newlocale = localeString;
        Locale locale = new Locale(newlocale);
        Locale.setDefault(locale);
        Configuration config = application.getResources().getConfiguration();
        config.locale = locale;
        application.getResources().updateConfiguration(config,
                application.getResources().getDisplayMetrics());
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

    public void sendPasswordResetEmail(String email,String localeString) {
        String newlocale = localeString;
        Locale locale = new Locale(newlocale);
        Locale.setDefault(locale);
        Configuration config = application.getResources().getConfiguration();
        config.locale = locale;
        application.getResources().updateConfiguration(config,
                application.getResources().getDisplayMetrics());
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(application, R.string.passRezSuccess,Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(application, application.getString(R.string.error,  Objects.requireNonNull(task.getException()).getLocalizedMessage())
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void userRegistration(String username, String email, String password,String localeString) {
        String newlocale = localeString;
        Locale locale = new Locale(newlocale);
        Locale.setDefault(locale);
        Configuration config = application.getResources().getConfiguration();
        config.locale = locale;
        application.getResources().updateConfiguration(config,
                application.getResources().getDisplayMetrics());
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

                    EmailVerification(localeString);

                    Toast.makeText(application, R.string.reg_success, Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(application, application.getString(R.string.error, Objects.requireNonNull(Registertask.getException()).getLocalizedMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void EmailVerification(String localeString) {
        String newlocale = localeString;
        Locale locale = new Locale(newlocale);
        Locale.setDefault(locale);
        Configuration config = application.getResources().getConfiguration();
        config.locale = locale;
        application.getResources().updateConfiguration(config,
                application.getResources().getDisplayMetrics());
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

    public void SetUsername(TextView hellousernameTextView,String locale) {
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


    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutMutableLiveData() {
        return loggedOutMutableLiveData;
    }



}
