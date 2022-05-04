package com.example.noteboard;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class PasswordResetFragment extends Fragment {

    private PasswordResetViewModel PasswordResetViewModel;
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
                Toast.makeText(getContext(),"Please enter your email.",Toast.LENGTH_SHORT).show();
            }
            else{
                PasswordResetViewModel.resetPassword(email);
            }
        });
    }
}