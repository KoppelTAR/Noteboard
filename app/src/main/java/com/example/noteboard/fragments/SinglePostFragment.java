package com.example.noteboard.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteboard.PostsRepository;
import com.example.noteboard.R;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;

public class SinglePostFragment extends Fragment {

    TextView ContentTextView;
    TextView TitleTextView;
    TextView UsernameTextView;
    TextView sharingCodeTextView;

    Button copyBtn;

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
        sharingCodeTextView = view.findViewById(R.id.sharingCodeText);


        if (getArguments() != null) {
            content = getArguments().getString("content");
            title = getArguments().getString("title");
            sharingCode = getArguments().getLong("sharingcode");
            author = getArguments().getString("author");

            ContentTextView.setText(content);
            TitleTextView.setText(title);
            sharingCodeTextView.setText(sharingCode.toString());
            PostsRepository.findAndSetUsername(UsernameTextView,author,getContext());
        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.error,"Viewing data"), Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            bundle.putString("type",getArguments().getString("type"));

            Navigation.findNavController(getView()).navigate(R.id.action_singlePostFragment_to_mainFragment,bundle);
        }

        copyBtn = view.findViewById(R.id.copyBtn);
        copyBtn.setOnClickListener(view1 -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("sharing code", sharingCode.toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity(), R.string.copyToClip, Toast.LENGTH_SHORT).show();
        });

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
        Bundle bundle = new Bundle();
        bundle.putString("type",getArguments().getString("type"));

        if (item.getItemId() == R.id.menuSettings){
            Navigation.findNavController(getView()).navigate(R.id.action_singlePostFragment_to_settingsFragment);
        }
        else if (item.getItemId() == R.id.menuUser){
            Navigation.findNavController(getView()).navigate(R.id.action_singlePostFragment_to_userFragment);
        }
        else if (item.getItemId() == R.id.menuEdit){ //TODO send bundle to edit + nav
            bundle.putString("title",title);
            bundle.putString("content",content);
            bundle.putLong("sharingcode",sharingCode);
            bundle.putString("author", author);
            Navigation.findNavController(getView()).navigate(R.id.action_singlePostFragment_to_editPostFragment, bundle);
        }
        else if (item.getItemId() == android.R.id.home){
            Navigation.findNavController(getView()).navigate(R.id.action_singlePostFragment_to_mainFragment,bundle);
        }
        return false;
    }
}