package com.example.noteboard.fragments;

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
import android.widget.TextView;

import com.example.noteboard.R;

public class SinglePostFragment extends Fragment {

    TextView ContentTextView;
    TextView TitleTextView;
    TextView UsernameTextView;

    //%s

    String content;
    String title;
    String sharingCode;
    String author;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_post,container,false);
        ContentTextView = view.findViewById(R.id.contentText);
        TitleTextView = view.findViewById(R.id.titleText);
        UsernameTextView = view.findViewById(R.id.usernameText);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            content = getArguments().getString("content");
            title = getArguments().getString("title");
            sharingCode = getArguments().getString("sharingcode");
            author = getArguments().getString("author");

            ContentTextView.setText(content);
            TitleTextView.setText(title);
            UsernameTextView.setText(String.format(getString(R.string.ViewSingle_UsernameDisplay),author));
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_singlepost, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //TODO add nav
        if (item.getItemId() == R.id.menuSettings){

        }
        if (item.getItemId() == R.id.menuUser){

        }
        if (item.getItemId() == R.id.menuEdit){ //TODO send bundle to edit
            Bundle bundle = new Bundle();
            bundle.putString("title",title);
            bundle.putString("content",content);
            bundle.putString("sharingcode",sharingCode);
            bundle.putString("author",author);

        }
        return false;
    }
}