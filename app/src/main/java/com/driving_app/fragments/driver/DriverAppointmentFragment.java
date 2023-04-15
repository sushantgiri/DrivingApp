package com.driving_app.fragments.driver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.driving_app.R;

public class DriverAppointmentFragment extends Fragment {

    private View rootView;
    private int requestCode = 0;
    private static final String DRIVER_PARCEL = "DRIVER_PARCEL";
    private TextView pendingAppointments;

    public static DriverAppointmentFragment newInstance(int requestCode){
        Bundle bundle = new Bundle();
        bundle.putInt(DRIVER_PARCEL, requestCode);
        DriverAppointmentFragment driverAppointmentFragment = new DriverAppointmentFragment();
        driverAppointmentFragment.setArguments(bundle);
        return driverAppointmentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_driver_appointment, container,false);
        pendingAppointments = rootView.findViewById(R.id.pendingAppointments);
        int requestCode = getArguments().getInt(DRIVER_PARCEL);
//        pendingAppointments.setText(requestCode == 1 ? "Pending": "");
//        if(requestCode == 1){
//            pendingAppointments.setText("");
//        }else{
//
//        }
//        if(getArguments().getBundle(DRIVER_PARCEL).get(""))
//        requestCode = getArguments().getBundle("")
        return rootView;

    }
}
