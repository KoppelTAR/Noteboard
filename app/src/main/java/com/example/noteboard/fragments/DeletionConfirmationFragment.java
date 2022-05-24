package com.example.noteboard.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.noteboard.R;
import com.example.noteboard.viewmodels.DeletionConfirmationViewModel;
import com.example.noteboard.viewmodels.EditPostViewModel;

public class DeletionConfirmationFragment extends Fragment {

    DeletionConfirmationViewModel Viewmodel;
    Button yes;
    Button no;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deletion_confirmation, container, false);

        yes = view.findViewById(R.id.yesBtn);
        no = view.findViewById(R.id.noBtn);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Viewmodel = new ViewModelProvider(this).get(DeletionConfirmationViewModel.class);
    }
}