package com.example.noteboard.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.noteboard.R;
import com.example.noteboard.viewmodels.DeletionConfirmationViewModel;
import com.example.noteboard.viewmodels.EditPostViewModel;

import java.util.Objects;

public class DeletionConfirmationFragment extends Fragment {

    DeletionConfirmationViewModel Viewmodel;
    Button yes;
    Button no;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.DeletionConfTitle);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_deletion_confirmation, container, false);

        yes = view.findViewById(R.id.yesBtn);
        no = view.findViewById(R.id.noBtn);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Viewmodel = new ViewModelProvider(this).get(DeletionConfirmationViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = new Bundle();
        bundle.putString("type",getArguments().getString("type"));

        yes.setOnClickListener(view1 -> {
            Viewmodel.DeletePost(getArguments().getLong("sharingcode"));
            Navigation.findNavController(getView()).navigate(R.id.action_deletionConfirmationFragment_to_mainFragment,bundle);
        });
        no.setOnClickListener(view1 -> {
            bundle.putString("title",getArguments().getString("title"));
            bundle.putString("content",getArguments().getString("title"));
            bundle.putLong("sharingcode",getArguments().getLong("sharingcode"));
            bundle.putString("author", getArguments().getString("author"));
            Navigation.findNavController(getView()).navigate(R.id.action_deletionConfirmationFragment_to_editPostFragment,bundle);
        });
    }
}