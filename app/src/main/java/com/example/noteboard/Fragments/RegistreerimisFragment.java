package com.example.noteboard.Fragments;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.noteboard.R;
import com.example.noteboard.ViewModel.RegistreerimisViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegistreerimisFragment extends Fragment {

    TextInputLayout emailEditText;
    TextInputLayout PasswordEditText;
    TextInputLayout UsernameEditText;
    TextInputLayout PasswordConfirmEditText;

    Button Register;

    private RegistreerimisViewModel registreerimisViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Register User");
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.registreerimis_fragment, container, false);

        Register = view.findViewById(R.id.BtnRegister);
        Register.setOnClickListener(view1 -> {
            Log.i(TAG, "onCreateView: ");
            emailEditText = requireView().findViewById(R.id.EmailEdit);
            PasswordConfirmEditText = getView().findViewById(R.id.ConfirmPasswordEdit);
            PasswordEditText = getView().findViewById(R.id.PasswordEdit);
            UsernameEditText = getView().findViewById(R.id.UsernameEdit);

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z0-9]+\\.+[a-z]+";

            String email = Objects.requireNonNull(emailEditText.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(PasswordEditText.getEditText()).getText().toString().trim();
            String confPassword = Objects.requireNonNull(PasswordConfirmEditText.getEditText()).getText().toString().trim();
            String Username = Objects.requireNonNull(UsernameEditText.getEditText()).getText().toString().trim();


            if (email.matches(emailPattern)
                    && password.length() >= 6
                    && confPassword.equals(password)
                    && Username.length() > 0) {
                Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();

                //creates user and saves data by default firebase logs the user in after creating account
                registreerimisViewModel.userRegistration(Username,email,password);
                //TODO LOG THE USER OUT
            }else {
                if (Username.equals("")) {
                    Toast.makeText(getActivity(), "Fill out first name", Toast.LENGTH_SHORT).show();
                }if (!email.matches(emailPattern)) {
                    Toast.makeText(getActivity(), "Must be a valid email", Toast.LENGTH_SHORT).show();
                }if ( password.length() < 6) { //&& !password.matches(PasswordPattern)
                    Toast.makeText(getActivity(), "A password must contain a capital letter, digit and at least 6 characters", Toast.LENGTH_LONG).show();
                }if (!confPassword.equals(password)) {
                    Toast.makeText(getActivity(), "Confirm password must match the password", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getActivity(), "Registration failed, Returning to Login screen", Toast.LENGTH_SHORT).show();
                //TODO UNCOMMENT AFTER NAVIGATION IS ADDED navController.navigate(R.id.mainFragment);
            }
        });

        return view;
    }

    NavController navController;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        Register = view.findViewById(R.id.BtnRegister);
        Register.setOnClickListener(view1 -> {
            emailEditText = requireView().findViewById(R.id.EmailEdit);
            PasswordConfirmEditText = getView().findViewById(R.id.ConfirmPasswordEdit);
            PasswordEditText = getView().findViewById(R.id.PasswordEdit);
            UsernameEditText = getView().findViewById(R.id.UsernameEdit);

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z0-9]+\\.+[a-z]+";

            String email = Objects.requireNonNull(emailEditText.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(PasswordEditText.getEditText()).getText().toString().trim();
            String confPassword = Objects.requireNonNull(PasswordConfirmEditText.getEditText()).getText().toString().trim();
            String Username = Objects.requireNonNull(UsernameEditText.getEditText()).getText().toString().trim();


                if (email.matches(emailPattern)
                        && password.length() >= 6
                        && confPassword.equals(password)
                        && Username.length() > 0) {
                    Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();

                    //creates user and saves data by default firebase logs the user in after creating account
                    registreerimisViewModel.userRegistration(Username,email,password);
                    //TODO LOG THE USER OUT
                }else {
                    if (Username.equals("")) {
                        Toast.makeText(getActivity(), "Fill out first name", Toast.LENGTH_SHORT).show();
                    }if (!email.matches(emailPattern)) {
                        Toast.makeText(getActivity(), "Must be a valid email", Toast.LENGTH_SHORT).show();
                    }if ( password.length() < 6) { //&& !password.matches(PasswordPattern)
                        Toast.makeText(getActivity(), "A password must contain a capital letter, digit and at least 6 characters", Toast.LENGTH_LONG).show();
                    }if (!confPassword.equals(password)) {
                        Toast.makeText(getActivity(), "Confirm password must match the password", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getActivity(), "Registration failed, Returning to Login screen", Toast.LENGTH_SHORT).show();
                    //TODO UNCOMMENT AFTER NAVIGATION IS ADDED navController.navigate(R.id.mainFragment);
                }
        });
    }
}