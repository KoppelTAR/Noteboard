package com.example.noteboard.fragments;

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

import com.example.noteboard.R;
import com.example.noteboard.viewmodels.EditPostViewModel;

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

        Save.setOnClickListener(view1 -> {
            String TitleFinal = TitleEditText.getText().toString();
            String ContentFinal = ContentEditText.getText().toString();

            Log.i("TAG", "onCreateView: "+ContentFinal+" :::   ::: "+TitleFinal);

            EditViewmodel.SaveChanges(TitleFinal,ContentFinal,Id);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Bundle bundle = new Bundle();
        bundle.putString("type",getArguments().getString("type"));

        if (item.getItemId() == R.id.menuBack) {
            bundle.putString("title",Title);
            bundle.putString("content",Content);
            bundle.putLong("sharingcode",Id);
            bundle.putString("author", getArguments().getString("author"));

            Navigation.findNavController(getView()).navigate(R.id.action_editPostFragment_to_singlePostFragment,bundle);
        }
        if (item.getItemId() == R.id.menuDelete) {
            EditViewmodel.DeletePost(Id);
            Navigation.findNavController(getView()).navigate(R.id.action_editPostFragment_to_mainFragment,bundle);
        }
        return super.onOptionsItemSelected(item);
    }


}