package com.example.noteboard.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.noteboard.R;
import com.example.noteboard.Utils;
import com.example.noteboard.viewmodels.CreatePostViewModel;

import java.util.Objects;


public class CreatePostFragment extends Fragment {

    CreatePostViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CreatePostViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Create post");
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        EditText postContent = view.findViewById(R.id.editTextPostContent);
        EditText postTitle = view.findViewById(R.id.editTextPostTitle);
        view.findViewById(R.id.btnCreate).setOnClickListener(view1 -> {
            if(!Utils.isEditTextEmpty(postContent,getContext())
                    && !Utils.isEditTextEmpty(postTitle, getContext())){
                viewModel.createPost(postTitle.getText().toString(),
                        postContent.getText().toString());

                try {
                    Toast.makeText(getContext(), getActivity().getString(R.string.pleaseWait), Toast.LENGTH_LONG).show();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Bundle args = new Bundle();
                args.putString("type",getArguments().getString("type"));
                Navigation.findNavController(getView()).navigate(R.id.action_createPostFragment_to_mainFragment, args);
                Toast.makeText(getContext(), getActivity().getString(R.string.postCreated), Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}