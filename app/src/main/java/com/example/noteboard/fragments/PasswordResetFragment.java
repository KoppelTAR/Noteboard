package com.example.noteboard.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.noteboard.R;
import com.example.noteboard.viewmodels.PasswordResetViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;
import java.util.Objects;

public class PasswordResetFragment extends Fragment {

    private com.example.noteboard.viewmodels.PasswordResetViewModel PasswordResetViewModel;
    private final String TAG= "";
    String email;

    public static boolean isAnyStringNullOrEmpty(String... strings) {
        for (String s : strings)
            if (s == null || s.isEmpty())
                return true;
        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            email = getArguments().getString("Email");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.resetPasswordTitle);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.password_reset_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText oldEmail = view.findViewById(R.id.txtPasswordReset);
        oldEmail.setText(email);
        PasswordResetViewModel = new ViewModelProvider(this).get(PasswordResetViewModel.class);
        view.findViewById(R.id.btnResetPassword).setOnClickListener(view1 -> {
            email = Objects.requireNonNull(oldEmail.getText()).toString().trim();
            if (isAnyStringNullOrEmpty(email)) {
                Log.i(TAG, String.valueOf(getActivity().getResources().getConfiguration().locale));
                Toast.makeText(getContext(), getActivity().getString(R.string.EnterEmailToast),Toast.LENGTH_SHORT).show();
            }
            else{
                PasswordResetViewModel.resetPassword(email,String.valueOf(getActivity().getResources().getConfiguration().locale));
                Navigation.findNavController(view).navigate(R.id.action_passwordResetFragment_to_loginFragment);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Navigation.findNavController(getView()).navigate(R.id.action_passwordResetFragment_to_loginFragment2);
        }
        return super.onOptionsItemSelected(item);
    }
}