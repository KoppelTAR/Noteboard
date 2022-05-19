package com.example.noteboard.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.noteboard.R;
import com.example.noteboard.viewmodels.UserDetailsViewModel;


public class UserDetailsFragment extends Fragment {

    Button updateDataBtn;
    Button forgotPasswordBtn;
    Button deleteUserBtn;
    UserDetailsViewModel userDetailsViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDetailsViewModel = new ViewModelProvider(this).get(UserDetailsViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateDataBtn = view.findViewById(R.id.btnUpdateData);
        forgotPasswordBtn = view.findViewById(R.id.forgotPasswordBtn);
        deleteUserBtn = view.findViewById(R.id.btnDeleteAccount);

        deleteUserBtn.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_userDetailsFragment_to_deleteUserFragment);
        });

        forgotPasswordBtn.setOnClickListener(view1 -> {
            userDetailsViewModel.resetPassword();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        return view;
    }
}