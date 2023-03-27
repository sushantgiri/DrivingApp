package com.driving_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.driving_app.R;

public class AppointmentsFragment extends Fragment {

    public static AppointmentsFragment newInstance() {
        AppointmentsFragment fragment = new AppointmentsFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_appointments, container, false);
        return view;
    }
}
