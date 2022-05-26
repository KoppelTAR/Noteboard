package com.example.noteboard.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.noteboard.R;
import com.example.noteboard.viewmodels.DeleteUserViewModel;

import java.util.Objects;


public class DeleteUserFragment extends Fragment {


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
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.deleteAccountTitle);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        confirmPassword = view.findViewById(R.id.deleteUserConfirmPassword);
        deleteUser = view.findViewById(R.id.btnDeleteUser);


        deleteUser.setOnClickListener(view1 -> {
            viewModel.deleteCurrentUser(confirmPassword,Navigation.findNavController(view));
        });


        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Navigation.findNavController(getView()).navigate(R.id.action_deleteUserFragment_to_userDetailsFragment);
        }
        return super.onOptionsItemSelected(item);
    }
}