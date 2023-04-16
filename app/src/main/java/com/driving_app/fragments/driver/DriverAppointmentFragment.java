package com.driving_app.fragments.driver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.driving_app.R;
import com.driving_app.adapter.DriverAppointmentsAdapter;
import com.driving_app.model.Appointments;
import com.driving_app.utils.BookingUtils;
import com.driving_app.utils.MessageUtils;

import java.util.ArrayList;

public class DriverAppointmentFragment extends Fragment implements BookingUtils.AppointmentFetchListener, DriverAppointmentsAdapter.UpComingAppointmentListener, BookingUtils.BookingStatusListener {

    private View rootView;
    private int requestCode = 0;
    private static final String DRIVER_PARCEL = "DRIVER_PARCEL";
    private TextView appointmentType;
    private RecyclerView driverRV;
    private TextView noAppointment;
    private DriverAppointmentsAdapter adapter;
    private String instructorName = "Krishna Parajuli";
    private DriverAppointmentListener driverAppointmentListener;

    public void setDriverAppointmentListener(DriverAppointmentListener driverAppointmentListener){
        this.driverAppointmentListener = driverAppointmentListener;
    }

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
        noAppointment = rootView.findViewById(R.id.noAppointment);

        adapter = new DriverAppointmentsAdapter();
        adapter.setUpComingAppointmentListener(this);
        driverRV = rootView.findViewById(R.id.driverRV);
        driverRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        appointmentType = rootView.findViewById(R.id.appointmentType);
        requestCode = getArguments().getInt(DRIVER_PARCEL);
        appointmentType.setText(requestCode == 1 ? "Pending": "Upcoming");

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            BookingUtils.fetchAppointments(instructorName, this, requestCode);
    }

    @Override
    public void onFailed(String message) {
        MessageUtils.showMessage(getActivity(),message);
    }

    @Override
    public void onUpcomingAppointmentFetched(ArrayList<Appointments> appointments) {
        adapter.addAll(appointments);
        noAppointment.setVisibility(appointments.size() > 0 ? View.VISIBLE : View.GONE) ;
    }

    @Override
    public void onPendingAppointmentFetched(ArrayList<Appointments> appointments) {
        adapter.addAll(appointments);
        noAppointment.setVisibility(appointments.size() > 0 ? View.VISIBLE : View.GONE) ;
    }

    @Override
    public void onBookingAccepted(Appointments appointments) {
        BookingUtils.acceptBooking(appointments,instructorName,this);
    }

    @Override
    public void onBookingSuccess() {
        if(driverAppointmentListener != null){
            driverAppointmentListener.onDrivingAppointmentSuccess();
        }
    }

    @Override
    public void onBookingFailed(String message) {
        MessageUtils.showMessage(getActivity(), message);
    }

    public interface DriverAppointmentListener {
        void onDrivingAppointmentSuccess();
    }
}
