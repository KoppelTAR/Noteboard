package com.example.noteboard.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.noteboard.R;
import com.example.noteboard.viewmodels.UserViewModel;

import java.util.Locale;
import java.util.Objects;

public class UserFragment extends Fragment {

    Button logout;
    Button viewPosts;
    TextView hello;
    Button viewAllPosts;
    UserViewModel userViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, container, false);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.profile_title);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        logout= view.findViewById(R.id.btnLogout);
        hello = view.findViewById(R.id.txtHelloUser);
        viewPosts = view.findViewById(R.id.btnViewPosts);
        viewAllPosts = view.findViewById(R.id.btnViewAllPosts);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
            userViewModel.showUsername(hello, String.valueOf(getActivity().getResources().getConfiguration().locale));
        userViewModel.getLoggedOutMutableLiveData().observe(getViewLifecycleOwner(), loggedOut ->{
            if(loggedOut){
                if(getView() != null) Navigation.findNavController(getView())
                        .navigate(R.id.action_userFragment_to_loginFragment);
            }
        });

        viewAllPosts.setOnClickListener(view1 -> {
            Bundle args = new Bundle();
            args.putString("type","all");
            Navigation.findNavController(getView()).navigate(R.id.action_userFragment_to_mainFragment,args);
        });

        viewPosts.setOnClickListener(view1 -> {
            Bundle args = new Bundle();
            args.putString("type","own");
            Navigation.findNavController(getView()).navigate(R.id.action_userFragment_to_mainFragment, args);
        });


        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Navigation.findNavController(getView()).navigate(R.id.action_userFragment_to_loginFragment);
                userViewModel.logout();
            }
        });
    }

}