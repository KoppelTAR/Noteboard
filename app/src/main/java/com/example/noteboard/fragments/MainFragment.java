package com.example.noteboard.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.example.noteboard.models.Post;
import com.example.noteboard.viewmodels.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;


public class MainFragment extends Fragment {
    private MainRecyclerAdapter mainRecyclerAdapter;
    FloatingActionButton fab;
    TextView title;
    MainViewModel mainViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.mainfragment_title);
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerAllPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mainRecyclerAdapter = new MainRecyclerAdapter();
        recyclerView.setAdapter(mainRecyclerAdapter);

        mainRecyclerAdapter.setOnItemClickListener(new MainRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemClickListener(Post post) {
                Bundle bundle = new Bundle();
                bundle.putString("type", getArguments().getString("type"));
                bundle.putString("title",post.getTitle());
                bundle.putString("content",post.getContent());
                bundle.putLong("sharingcode",post.getSharingCode());
                bundle.putString("author",post.getPostAuthor());

                mainViewModel.clearPosts();
                Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_singlePostFragment,bundle);
            }
        });
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
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        fab = view.findViewById(R.id.floatingBtn);
        title = view.findViewById(R.id.txtTitle);
        if(getArguments().getString("type").equals("all")){
            mainViewModel.showPosts();
            title.setText(getActivity().getApplication().getString(R.string.allPosts));
        }
        if(getArguments().getString("type").equals("own")){
            mainViewModel.showUserPosts();
            mainViewModel.setPostsTitle(title);
        }
        mainViewModel.getPostLiveData().observe(getViewLifecycleOwner(),posts -> mainRecyclerAdapter.updatePostList(posts));
        fab = view.findViewById(R.id.floatingBtn);
        Navigation.findNavController(getView());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewModel.clearPosts();
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
            mainViewModel.clearPosts();
            Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_settingsFragment);
        }
        if (item.getItemId() == R.id.menuUser){
            mainViewModel.clearPosts();
            Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_userFragment);
        }
        return false;
    }
}