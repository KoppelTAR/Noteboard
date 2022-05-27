package com.example.noteboard.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteboard.R;
import com.example.noteboard.Utils;
import com.example.noteboard.viewmodels.SMSViewModel;

import java.util.Objects;


public class SmsFragment extends Fragment {

    SMSViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SMSViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms, container, false);
        EditText numberEditText = view.findViewById(R.id.editTextPhoneNumber);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.smsTitle);
        setHasOptionsMenu(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        view.findViewById(R.id.btnSendPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utils.isEditTextEmpty(numberEditText, getContext())){
                        String SMS_post = getString(R.string.postShared);
                        String title = getArguments().getString("title");
                        String content= getArguments().getString("content");
                        viewModel.sendSMS(numberEditText.getText().toString(), String.format(SMS_post,title,content));
                        Toast.makeText(getContext(), getContext().getString(R.string.sms_sent),Toast.LENGTH_SHORT).show();
                }
            }
        });
        view.findViewById(R.id.btnSendCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utils.isEditTextEmpty(numberEditText, getContext())){
                        String SMS_sharingCode = getString(R.string.codeShared);
                        String sharingCode = String.valueOf(getArguments().getLong("sharingcode"));
                        viewModel.sendSMS(numberEditText.getText().toString(), String.format(SMS_sharingCode,sharingCode));
                        Toast.makeText(getContext(), getContext().getString(R.string.sms_sent),Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Bundle bundle = new Bundle();
            bundle.putString("title",getArguments().getString("title"));
            bundle.putString("content",getArguments().getString("content"));
            bundle.putLong("sharingcode",getArguments().getLong("sharingcode"));
            bundle.putString("author", getArguments().getString("author"));
            bundle.putString("type",getArguments().getString("type"));
            Navigation.findNavController(getView()).navigate(R.id.action_smsFragment_to_singlePostFragment,bundle);
        }
        return super.onOptionsItemSelected(item);
    }
}