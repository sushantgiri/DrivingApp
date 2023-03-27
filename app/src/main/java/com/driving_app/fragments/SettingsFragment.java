package com.driving_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.driving_app.R;

public class SettingsFragment extends Fragment {

    public static SettingsFragment newInstance(){
        SettingsFragment fragment =new SettingsFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        return view;
    }
}
