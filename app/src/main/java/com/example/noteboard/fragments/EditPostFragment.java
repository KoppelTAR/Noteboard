package com.example.noteboard.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.noteboard.R;

public class EditPostFragment extends Fragment {

    EditText ContentEditText;
    EditText TitleEditText;

    String Title;
    String Content;
    String SharingCode;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_post_fragment, container, false);

        ContentEditText = view.findViewById(R.id.ContentEditText);
        TitleEditText = view.findViewById(R.id.TitleEditText);

        if (getArguments() != null) {
            Title = getArguments().getString("title");
            Content = getArguments().getString("content");
            //TODO left off
        }

        return view;
    }

}