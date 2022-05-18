package com.example.noteboard.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.noteboard.R;
import com.example.noteboard.adapters.MainRecyclerAdapter;
import com.example.noteboard.viewmodels.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class UserPostsFragment extends Fragment {

    private MainRecyclerAdapter mainRecyclerAdapter;
    FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerAllPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mainRecyclerAdapter = new MainRecyclerAdapter();
        recyclerView.setAdapter(mainRecyclerAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.showUserPosts();
        mainViewModel.getPostLiveData().observe(getViewLifecycleOwner(),posts -> mainRecyclerAdapter.updatePostList(posts));
        fab = view.findViewById(R.id.floatingBtn);
        Navigation.findNavController(getView());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_createPostFragment);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuSettings){
            Navigation.findNavController(getView()).navigate(R.id.action_userPostsFragment2_to_settingsFragment);
        }
        if (item.getItemId() == R.id.menuUser){
            Navigation.findNavController(getView()).navigate(R.id.action_userPostsFragment2_to_userFragment);
        }
        return false;
    }
}