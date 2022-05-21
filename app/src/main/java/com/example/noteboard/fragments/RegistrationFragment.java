package com.example.noteboard.fragments;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.noteboard.R;
import com.example.noteboard.viewmodels.RegistrationViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;
import java.util.Objects;

public class RegistrationFragment extends Fragment {

    TextInputEditText emailEditText;
    TextInputEditText PasswordEditText;
    TextInputEditText UsernameEditText;
    TextInputEditText PasswordConfirmEditText;

    private RegistrationViewModel RegistrationView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RegistrationView = new ViewModelProvider(this).get(RegistrationViewModel.class);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Register User");
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.registration_fragment, container, false);

        Button Register = view.findViewById(R.id.BtnRegister);
        Register.setOnClickListener(view1 -> {
            emailEditText = requireView().findViewById(R.id.EmailEdit);
            PasswordConfirmEditText = requireView().findViewById(R.id.ConfirmPasswordEdit);
            PasswordEditText = requireView().findViewById(R.id.PasswordEdit);
            UsernameEditText = requireView().findViewById(R.id.UsernameEdit);

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z0-9]+\\.+[a-z]+";

            String email = Objects.requireNonNull(Objects.requireNonNull(emailEditText).getText()).toString().trim();
            String password = Objects.requireNonNull(Objects.requireNonNull(PasswordEditText).getText()).toString().trim();
            String confPassword = Objects.requireNonNull(Objects.requireNonNull(PasswordConfirmEditText).getText()).toString().trim();
            String Username = Objects.requireNonNull(Objects.requireNonNull(UsernameEditText).getText()).toString().trim();

            if (email.matches(emailPattern)
                    && password.length() >= 6
                    && confPassword.equals(password)
                    && Username.length() > 0) {

                //creates user and saves data by default firebase logs the user in after creating account
                RegistrationView.userRegistration(Username,email,password,String.valueOf(getActivity().getResources().getConfiguration().locale));
                RegistrationView.logOut();
                navController.navigate(R.id.action_registrationFragment_to_loginFragment);
            }else {

                if (Username.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.FillUsernameToast), Toast.LENGTH_SHORT).show();
                }else if (!email.matches(emailPattern)) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.ValidEmailToast), Toast.LENGTH_SHORT).show();
                }else if ( password.length() < 6) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.PassLenghtToast), Toast.LENGTH_LONG).show();
                } else if (!confPassword.equals(password)) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.ConfPasstToast), Toast.LENGTH_SHORT).show();
                }
            }

        });

        return view;
    }

    NavController navController;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
}