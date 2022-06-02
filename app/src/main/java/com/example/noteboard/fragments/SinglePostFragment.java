package com.example.noteboard.fragments;

import static androidx.fragment.app.FragmentManager.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteboard.PostsRepository;
import com.example.noteboard.R;
import com.example.noteboard.viewmodels.MainViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class SinglePostFragment extends Fragment {

    TextView ContentTextView;
    TextView TitleTextView;
    TextView UsernameTextView;
    TextView sharingCodeTextView;
    TextView lastEditTextView;

    Button copyBtn;

    //%s

    String content;
    String title;
    Long sharingCode;
    String author;
    String editedBy;

    MainViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.singlepost_title);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        View view = inflater.inflate(R.layout.fragment_single_post,container,false);
        ContentTextView = view.findViewById(R.id.contentText);
        TitleTextView = view.findViewById(R.id.titleText);
        lastEditTextView = view.findViewById(R.id.textViewLastEdit);
        UsernameTextView = view.findViewById(R.id.usernameText);
        sharingCodeTextView = view.findViewById(R.id.sharingCodeText);

        if (getArguments() != null) {
            sharingCode = getArguments().getLong("sharingcode");
            author = getArguments().getString("author");
            viewModel.showLastEdit(lastEditTextView,getArguments().getString("editedBy"), sharingCode,String.valueOf(getActivity().getResources().getConfiguration().locale));
            editedBy = getArguments().getString("editedBy");
            viewModel.setPostContent(sharingCode,TitleTextView,ContentTextView);
            sharingCodeTextView.setText(sharingCode.toString());
            PostsRepository.findAndSetUsername(UsernameTextView,author,getContext());
        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.error,"Viewing data"), Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            bundle.putString("type",getArguments().getString("type"));

            Navigation.findNavController(getView()).navigate(R.id.action_singlePostFragment_to_mainFragment,bundle);
        }

        sharingCodeTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                        Log.i("TAG", "onTouch: ");
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("sharing code", sharingCode.toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getActivity(), R.string.copyToClip, Toast.LENGTH_SHORT).show();


                return false;
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
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
            bundle.putString("settings","single");
            bundle.putString("title",title);
            bundle.putString("content",content);
            bundle.putLong("sharingcode",sharingCode);
            bundle.putString("author", author);
            bundle.putString("editedBy",editedBy);
            Navigation.findNavController(getView()).navigate(R.id.action_singlePostFragment_to_settingsFragment,bundle);
        }
        if(item.getItemId() == R.id.menuShare){
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED){
                bundle.putString("title",title);
                bundle.putLong("editedAt",getArguments().getLong("editedAt"));
                bundle.putString("editedBy",getArguments().getString("editedBy"));
                bundle.putString("content",content);
                bundle.putLong("sharingcode",sharingCode);
                bundle.putString("author", author);
                Navigation.findNavController(getView()).navigate(R.id.action_singlePostFragment_to_smsFragment,bundle);
            }
            else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.SEND_SMS)){
                    Snackbar.make(getView().findViewById(R.id.singlePostLayout), getString(R.string.sms_permission),Snackbar.LENGTH_INDEFINITE)
                            .setAction(android.R.string.ok, view1 -> {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 100);
                            }).show();
                }
                else{
                    Toast.makeText(getContext(), getString(R.string.sms_permission_2), Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 100);
                }
            }

        }
        else if (item.getItemId() == R.id.menuUser){
            Navigation.findNavController(getView()).navigate(R.id.action_singlePostFragment_to_userFragment);
        }
        else if (item.getItemId() == R.id.menuEdit){
            bundle.putString("title",title);
            bundle.putLong("editedAt",getArguments().getLong("editedAt"));
            bundle.putString("editedBy",getArguments().getString("editedBy"));
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