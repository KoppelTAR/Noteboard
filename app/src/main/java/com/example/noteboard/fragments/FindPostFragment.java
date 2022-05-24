package com.example.noteboard.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.noteboard.PostsRepository;
import com.example.noteboard.R;
import com.example.noteboard.Utils;
import com.example.noteboard.viewmodels.FindPostViewModel;


public class FindPostFragment extends Fragment {

    FindPostViewModel findPostViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findPostViewModel = new ViewModelProvider(this).get(FindPostViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_post, container, false);

        EditText postCode = view.findViewById(R.id.editTextPostCode);

        view.findViewById(R.id.btnFindPost).setOnClickListener(view1 -> {
            if(!Utils.isEditTextEmpty(postCode,getContext())){
                findPostViewModel.findPost(postCode.getText().toString(),getContext());
            }
        });

        view.findViewById(R.id.btnFindPostBack).setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_findPostFragment_to_userFragment);
        });

        return view;
    }
}