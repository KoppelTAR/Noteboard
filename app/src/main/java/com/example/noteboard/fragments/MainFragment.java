package com.example.noteboard.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.example.noteboard.models.Post;
import com.example.noteboard.viewmodels.MainViewModel;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    private  MainViewModel mainViewModel;
    private MainRecyclerAdapter mainRecyclerAdapter;
    ArrayList<Post> postArrayList;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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

        mainRecyclerAdapter.setOnItemClickListener(new MainRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemClickListener(Post post) {
                Bundle bundle = new Bundle();
                bundle.putString("title",post.getTitle());
                bundle.putString("content",post.getContent());
                bundle.putString("sharingcode",post.getSharingCode());
                bundle.putString("author",post.getPostAuthor());

                Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_singlePostFragment,bundle);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainRecyclerAdapter.loadPostsData();
        //mainViewModel.displayPosts();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuSettings){

        }
        if (item.getItemId() == R.id.menuUser){
        }
        return false;
    }
}