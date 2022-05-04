package com.example.noteboard.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.noteboard.R;
import com.example.noteboard.Utils;
import com.example.noteboard.viewmodels.LoginViewModel;


public class LoginFragment extends Fragment {
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getUserMutableLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                if(getView()!=null){

                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        EditText editTextEmail = view.findViewById(R.id.editTextEmail);
        EditText editTextPassword = view.findViewById(R.id.editTextPassword);
        view.findViewById(R.id.btnLogin).setOnClickListener(view1 -> {
            if(!Utils.isEditTextEmpty(editTextEmail, getContext())
                    && !Utils.isEditTextEmpty(editTextPassword, getContext())){
                loginViewModel.logIn(editTextEmail.getText().toString(),
                        editTextPassword.getText().toString());
            }
        });
        return view;
    }
}