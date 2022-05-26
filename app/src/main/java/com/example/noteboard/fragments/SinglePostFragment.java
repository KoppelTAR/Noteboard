package com.example.noteboard.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.noteboard.PostsRepository;
import com.example.noteboard.R;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;

public class SinglePostFragment extends Fragment {

    TextView ContentTextView;
    TextView TitleTextView;
    TextView UsernameTextView;

    //%s

    String content;
    String title;
    Long sharingCode;
    String author;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.singlepost_title);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        View view = inflater.inflate(R.layout.fragment_single_post,container,false);
        ContentTextView = view.findViewById(R.id.contentText);
        TitleTextView = view.findViewById(R.id.titleText);
        UsernameTextView = view.findViewById(R.id.usernameText);

        if (getArguments() != null) {
            content = getArguments().getString("content");
            title = getArguments().getString("title");
            sharingCode = getArguments().getLong("sharingcode");
            author = getArguments().getString("author");

            ContentTextView.setText(content);
            TitleTextView.setText(title);
            PostsRepository.findAndSetUsername(UsernameTextView,author,getContext());
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
        inflater.inflate(R.menu.menu_singlepost, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuSettings){
            Navigation.findNavController(getView()).navigate(R.id.action_singlePostFragment_to_settingsFragment);
        }
        if (item.getItemId() == R.id.menuUser){
            Navigation.findNavController(getView()).navigate(R.id.action_singlePostFragment_to_userFragment);
        }
        if (item.getItemId() == R.id.menuEdit){ //TODO send bundle to edit + nav
            Bundle bundle = new Bundle();
            bundle.putString("title",title);
            bundle.putString("content",content);
            bundle.putLong("sharingcode",sharingCode);
            bundle.putString("author", author);
            bundle.putString("type",getArguments().getString("type"));
            Navigation.findNavController(getView()).navigate(R.id.action_singlePostFragment_to_editPostFragment, bundle);
        }
        if (item.getItemId() == android.R.id.home){
            Bundle typeBundle = new Bundle();
            typeBundle.putString("type",getArguments().getString("type"));
            Navigation.findNavController(getView()).navigate(R.id.action_singlePostFragment_to_mainFragment,typeBundle);
        }
        return false;
    }
}