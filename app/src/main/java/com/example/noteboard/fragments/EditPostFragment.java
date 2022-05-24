package com.example.noteboard.fragments;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.noteboard.R;
import com.example.noteboard.viewmodels.EditPostViewModel;
import com.example.noteboard.viewmodels.MainViewModel;

public class EditPostFragment extends Fragment {

    EditPostViewModel EditViewmodel;

    EditText ContentEditText;
    EditText TitleEditText;
    Button Save;

    String Title;
    String Content;
    Long Id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_post_fragment, container, false);

        ContentEditText = view.findViewById(R.id.ContentEditText);
        TitleEditText = view.findViewById(R.id.TitleEditText);
        Save = view.findViewById(R.id.btnSaveChanges);

        if (getArguments() != null) {
            Title = getArguments().getString("title");
            Content = getArguments().getString("content");
            Id = getArguments().getLong("Id");

            TitleEditText.setText(Title);
            ContentEditText.setText(Content);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditViewmodel = new ViewModelProvider(this).get(EditPostViewModel.class);

        Save.setOnClickListener(view1 -> {
            String TitleFinal = TitleEditText.getText().toString();
            String ContentFinal = ContentEditText.getText().toString();

            Log.i("TAG", "onCreateView: "+ContentFinal+" :::   ::: "+TitleFinal);

            EditViewmodel.SaveChanges(TitleFinal,ContentFinal,Id);
        });
    }
}