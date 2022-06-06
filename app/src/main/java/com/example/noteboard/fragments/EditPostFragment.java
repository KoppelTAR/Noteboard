package com.example.noteboard.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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

import java.util.Objects;

public class EditPostFragment extends Fragment {

    EditPostViewModel editViewModel;

    EditText contentEditText;
    EditText titleEditText;
    Button save;

    String user;
    Long id;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.EditingTitle);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        View view = inflater.inflate(R.layout.edit_post_fragment, container, false);

        contentEditText = view.findViewById(R.id.ContentEditText);
        titleEditText = view.findViewById(R.id.titleEditText);
        save = view.findViewById(R.id.btnSaveChanges);

        if (getArguments() != null) {
            id = getArguments().getLong("sharingcode");
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
        editViewModel = new ViewModelProvider(this).get(EditPostViewModel.class);
        editViewModel.setPostContent(getArguments().getLong("sharingcode"), titleEditText, contentEditText);
        user = getArguments().getString("editedBy");

        save.setOnClickListener(view1 -> {
            String ContentFinal = contentEditText.getText().toString().trim();
            String TitleFinal = titleEditText.getText().toString().trim();
            if (TitleFinal.equals("") || ContentFinal.equals("")) {
                Toast.makeText(getContext(), R.string.emptyInput, Toast.LENGTH_SHORT).show();
            } else {
                editViewModel.SaveChanges(TitleFinal,ContentFinal, id);
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
        bundle.putString("title", titleEditText.getText().toString().trim());
        bundle.putString("content", contentEditText.getText().toString().trim());
        bundle.putLong("sharingcode", id);
        bundle.putString("author", getArguments().getString("author"));

        if (item.getItemId() == android.R.id.home) {
            Navigation.findNavController(getView()).navigate(R.id.action_editPostFragment_to_singlePostFragment,bundle);
        }
        if (item.getItemId() == R.id.menuDelete) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            editViewModel.DeletePost(getArguments().getLong("sharingcode"));
                            Navigation.findNavController(getView()).navigate(R.id.action_editPostFragment_to_mainFragment,bundle);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.are_you_sure).setPositiveButton(R.string.yes, dialogClickListener)
                    .setNegativeButton(R.string.no, dialogClickListener).show();
        }
        return super.onOptionsItemSelected(item);
    }


}