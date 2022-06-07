package com.example.noteboard.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteboard.R;
import com.example.noteboard.viewmodels.MainViewModel;
import com.example.noteboard.viewmodels.UserDetailsViewModel;

import java.util.Objects;


public class UserDetailsFragment extends Fragment {

    Button updateDataBtn;
    Button forgotPasswordBtn;
    Button deleteUserBtn;
    EditText username;
    EditText email;
    EditText confirmPassword;
    UserDetailsViewModel userDetailsViewModel;
    MainViewModel mainViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        userDetailsViewModel = new ViewModelProvider(this).get(UserDetailsViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user_settings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuSettings){
            mainViewModel.clearPosts();
            Bundle bundle = new Bundle();
            bundle.putString("settings","details");
            Navigation.findNavController(getView()).navigate(R.id.action_userDetailsFragment_to_settingsFragment,bundle);
        }
        if (item.getItemId() == R.id.menuUser){
            mainViewModel.clearPosts();
            Navigation.findNavController(getView()).navigate(R.id.action_userDetailsFragment_to_userFragment2);
        }

        if (item.getItemId() == android.R.id.home){
            Navigation.findNavController(getView()).navigate(R.id.action_userDetailsFragment_to_userFragment);
        }
        return false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateDataBtn = view.findViewById(R.id.btnUpdateData);
        forgotPasswordBtn = view.findViewById(R.id.forgotPasswordBtn);
        deleteUserBtn = view.findViewById(R.id.btnDeleteAccount);
        username = view.findViewById(R.id.editTextUpdateUserName);
        email = view.findViewById(R.id.editTextUpdateEmail);
        confirmPassword = view.findViewById(R.id.editTextConfirmPassword);

        userDetailsViewModel.showCurrentUserEmail(email);
        userDetailsViewModel.showCurrentUserUsername(username);

        updateDataBtn.setOnClickListener(view1 -> {
            if (username.length() <= 0 && username.length() > 20) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.FillUsernameToast), Toast.LENGTH_LONG).show();
            } else {
                userDetailsViewModel.updateData(email,username,confirmPassword, String.valueOf(getActivity().getResources().getConfiguration().locale));
            }
        });

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
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        return view;
    }
}