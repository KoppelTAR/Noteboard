package com.example.noteboard.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.noteboard.MainActivity;
import com.example.noteboard.R;
import com.example.noteboard.Utils;
import com.example.noteboard.models.Post;
import com.example.noteboard.viewmodels.CreatePostViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class CreatePostFragment extends Fragment {

    CreatePostViewModel viewModel;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CreatePostViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.createPostTitle);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        EditText postContent = view.findViewById(R.id.editTextPostContent);
        EditText postTitle = view.findViewById(R.id.editTextPostTitle);
        // creating a variable
        // for firebasefirestore.

        view.findViewById(R.id.btnCreate).setOnClickListener(view1 -> {
            if(!Utils.isEditTextEmpty(postContent,getContext())
                    && !Utils.isEditTextEmpty(postTitle, getContext())){
                firebaseAuth= FirebaseAuth.getInstance();
                addDataToFirestore(postTitle.getText().toString(),postContent.getText().toString(), Calendar.getInstance().getTime(),Calendar.getInstance().getTimeInMillis(),firebaseAuth.getCurrentUser().getUid());
            }
        });
        return view;
    }

    private void addDataToFirestore(String courseName, String courseDescription, Date date, Long id, String courseDuration) {

        // creating a collection reference
        // for our Firebase Firetore database.
        db = FirebaseFirestore.getInstance();
        CollectionReference dbCourses = db.collection("posts");

        // adding our data to our courses object class.
        Post courses = new Post(courseName, courseDescription, date,id,courseDuration);

        // below method is use to add data to Firebase Firestore.
        dbCourses.add(courses).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(getActivity(), "Your Course has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
                Bundle args = new Bundle();
                args.putString("type",getArguments().getString("type"));
                Navigation.findNavController(getView()).navigate(R.id.action_createPostFragment_to_mainFragment, args);
                Toast.makeText(getContext(), getActivity().getString(R.string.postCreated), Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(getActivity(), "Fail to add course \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Bundle args = new Bundle();
            args.putString("type",getArguments().getString("type"));
            Navigation.findNavController(getView()).navigate(R.id.action_createPostFragment_to_mainFragment,args);
        }
        return super.onOptionsItemSelected(item);
    }
}