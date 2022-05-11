package com.example.noteboard.fragments;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteboard.R;
import com.example.noteboard.Utils;
import com.example.noteboard.viewmodels.LoginViewModel;

import java.util.Objects;


public class LoginFragment extends Fragment {
    private LoginViewModel loginViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application var = getActivity().getApplication();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getUserMutableLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                if(getView()!=null){
                    //TODO this needs to be added back in once we get the main page done -- kaspar
                    //TODO this should auto login if the user was previously logged in -- kaspar
                    //Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_userFragment);
                }
                else {
                    Toast.makeText(getActivity(), var.getString(R.string.LoginFailedToast), Toast.LENGTH_SHORT).show();
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

        view.findViewById(R.id.btnForgotPassword).setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_passwordResetFragment);
        });

        view.findViewById(R.id.btnRegister).setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registrationFragment);
        });
        return view;
    }
}