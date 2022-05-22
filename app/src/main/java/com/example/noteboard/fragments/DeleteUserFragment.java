package com.example.noteboard.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.noteboard.R;
import com.example.noteboard.viewmodels.DeleteUserViewModel;


public class DeleteUserFragment extends Fragment {

    Button backBtn;
    Button deleteUser;
    EditText confirmPassword;
    DeleteUserViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DeleteUserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_user, container, false);

        confirmPassword = view.findViewById(R.id.deleteUserConfirmPassword);
        backBtn = view.findViewById(R.id.btnDeleteUserBack);
        deleteUser = view.findViewById(R.id.btnDeleteUser);

        backBtn.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_deleteUserFragment_to_userDetailsFragment);
        });

        deleteUser.setOnClickListener(view1 -> {
            viewModel.deleteCurrentUser(confirmPassword,Navigation.findNavController(view));
        });


        return view;
    }
}