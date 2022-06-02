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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
        viewModel.createPost(courseName,courseDescription).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Your Course has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
                    Bundle args = new Bundle();
                    args.putString("type", getArguments().getString("type"));
                    Navigation.findNavController(getView()).navigate(R.id.action_createPostFragment_to_mainFragment, args);
                    Toast.makeText(getContext(), getActivity().getString(R.string.postCreated), Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(), "Fail to add post \n", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Bundle args = new Bundle();
            args.putString("type",getArguments().getString("type"));
            Navigation.findNavController(getView()).navigate(R.id.action_createPostFragment_to_mainFragment2,args);
        }
        return super.onOptionsItemSelected(item);
    }
}