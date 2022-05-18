package com.example.noteboard.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteboard.R;
import com.example.noteboard.adapters.MainRecyclerAdapter;
import com.example.noteboard.viewmodels.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainFragment extends Fragment {
    private MainRecyclerAdapter mainRecyclerAdapter;
    FloatingActionButton fab;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Noteboard");
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerAllPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mainRecyclerAdapter = new MainRecyclerAdapter();
        recyclerView.setAdapter(mainRecyclerAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.showPosts();
        mainViewModel.getPostLiveData().observe(getViewLifecycleOwner(),posts -> mainRecyclerAdapter.updatePostList(posts));
        mainViewModel.getLoggedOutMutableLiveData().observe(getViewLifecycleOwner(), loggedOut ->{
            if(loggedOut){
                if(getView() != null) Navigation.findNavController(getView())
                        .navigate(R.id.action_mainFragment_to_loginFragment);
            }
        });
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
            Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_settingsFragment);
        }
        if (item.getItemId() == R.id.menuUser){
            Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_userFragment);
        }
        return false;
    }
}