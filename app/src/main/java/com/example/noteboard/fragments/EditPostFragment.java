package com.example.noteboard.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteboard.AuthRepository;
import com.example.noteboard.R;
import com.example.noteboard.viewmodels.EditPostViewModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class EditPostFragment extends Fragment {

    EditPostViewModel EditViewmodel;

    EditText ContentEditText;
    EditText TitleEditText;
    Button Save;

    String user;
    Long Id;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.EditingTitle);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        View view = inflater.inflate(R.layout.edit_post_fragment, container, false);

        ContentEditText = view.findViewById(R.id.ContentEditText);
        TitleEditText = view.findViewById(R.id.TitleEditText);
        Save = view.findViewById(R.id.btnSaveChanges);

        if (getArguments() != null) {
            Id = getArguments().getLong("sharingcode");
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditViewmodel = new ViewModelProvider(this).get(EditPostViewModel.class);
        EditViewmodel.setPostContent(getArguments().getLong("sharingcode"), TitleEditText, ContentEditText,null,null,null);
        user = getArguments().getString("editedBy");

        Save.setOnClickListener(view1 -> {
            String ContentFinal = ContentEditText.getText().toString().trim();
            String TitleFinal = TitleEditText.getText().toString().trim();
            if (TitleFinal.equals("") || ContentFinal.equals("")) {
                Toast.makeText(getContext(), R.string.emptyInput, Toast.LENGTH_SHORT).show();
            } else {
                EditViewmodel.SaveChanges(TitleFinal,ContentFinal,Id);
                user = AuthRepository.getCurrentUserUID();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Bundle bundle = new Bundle();
        bundle.putLong("editedAt",getArguments().getLong("editedAt"));
        bundle.putString("editedBy",user);
        bundle.putString("type",getArguments().getString("type"));
        bundle.putString("title",TitleEditText.getText().toString().trim());
        bundle.putString("content",ContentEditText.getText().toString().trim());
        bundle.putLong("sharingcode",Id);
        bundle.putString("author", getArguments().getString("author"));

        if (item.getItemId() == android.R.id.home) {
            bundle.putBoolean("anon",false);
            Navigation.findNavController(getView()).navigate(R.id.action_editPostFragment_to_singlePostFragment,bundle);
        }
        if (item.getItemId() == R.id.menuDelete) {
            Navigation.findNavController(getView()).navigate(R.id.action_editPostFragment_to_deletionConfirmationFragment,bundle);
        }
        return super.onOptionsItemSelected(item);
    }


}