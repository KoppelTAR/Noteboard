package com.example.noteboard.fragments;

import static androidx.fragment.app.FragmentManager.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteboard.PostsRepository;
import com.example.noteboard.R;
import com.example.noteboard.Utils;
import com.example.noteboard.viewmodels.CreatePostViewModel;


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
        requireActivity().getActionBar().setTitle("Create a new post.");
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        EditText postContent = view.findViewById(R.id.editTextPostContent);
        EditText postTitle = view.findViewById(R.id.editTextPostTitle);
        view.findViewById(R.id.btnCreate).setOnClickListener(view1 -> {
            if(!Utils.isEditTextEmpty(postContent,getContext())
                    && !Utils.isEditTextEmpty(postTitle, getContext())){
                viewModel.createPost(postTitle.getText().toString(),
                        postContent.getText().toString());

                Toast.makeText(getContext(), getActivity().getString(R.string.postCreated), Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}