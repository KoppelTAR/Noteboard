package com.example.noteboard.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.noteboard.R;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.Objects;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    Switch night;
    ImageView en;
    ImageView ee;
    ImageView ru;
    TextView txtLang;
    TextView txtDark;
    Button save;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.settings_title);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        night= view.findViewById(R.id.switchDarkMode);
        en = view.findViewById(R.id.imgEng);
        ee = view.findViewById(R.id.imgEst);
        ru = view.findViewById(R.id.imgRus);
        txtLang = view.findViewById(R.id.txtLang);
        txtDark = view.findViewById(R.id.txtDarkModeToggle);
        save = view.findViewById(R.id.btnSaveSettings);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        en.setOnClickListener(this);
        ee.setOnClickListener(this);
        ru.setOnClickListener(this);

        int nightModeFlags =  view.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            night.setChecked(true);
        }

        night.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(night.isChecked()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgEng:
                setLocale("en");
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE).edit(); editor.putString("language", "en"); editor.commit();
                break;
            case R.id.imgEst:
                setLocale("et");
                editor = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE).edit(); editor.putString("language", "et"); editor.commit();
                break;
            case R.id.imgRus:
                setLocale("ru");
                editor = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE).edit(); editor.putString("language", "ru"); editor.commit();
                break;
            default:
                break;
        }
    }

    private void setLocale(String language) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale(language);
        resources.updateConfiguration(configuration,metrics);
        onConfigurationChanged(configuration);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        txtLang.setText(R.string.language_selection);
        txtDark.setText(R.string.dark_mode_toggle);
        save.setText(R.string.btnSave);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.settings_title);
    }
}