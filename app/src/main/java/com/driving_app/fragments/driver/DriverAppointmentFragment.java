package com.driving_app.fragments.driver;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class DriverAppointmentFragment extends Fragment implements BookingUtils.AppointmentFetchListener, DriverAppointmentsAdapter.UpComingAppointmentListener, BookingUtils.BookingStatusListener, View.OnClickListener {

    private View rootView;
    private int requestCode = 0;
    private static final String DRIVER_PARCEL = "DRIVER_PARCEL";
    private TextView appointmentType;
    private RecyclerView driverRV;
    private TextView noAppointment;
    private DriverAppointmentsAdapter adapter;
    private String instructorName = "Krishna Parajuli";
    private DriverAppointmentListener driverAppointmentListener;
    private ImageButton logoutIcon;

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
        logoutIcon = rootView.findViewById(R.id.logoutIcon);
        logoutIcon.setOnClickListener(this);

        adapter = new DriverAppointmentsAdapter();
        adapter.setUpComingAppointmentListener(this);
        driverRV = rootView.findViewById(R.id.driverRV);
        driverRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        driverRV.setAdapter(adapter);

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
        noAppointment.setVisibility(appointments.size() > 0 ? View.GONE : View.VISIBLE) ;
    }

    @Override
    public void onPendingAppointmentFetched(ArrayList<Appointments> appointments) {
        adapter.addAll(appointments);
        noAppointment.setVisibility(appointments.size() > 0 ? View.GONE : View.VISIBLE) ;
    }

    @Override
    public void onBookingAccepted(Appointments appointments) {
        BookingUtils.acceptBooking(getActivity(),appointments,instructorName,this);
    }

    @Override
    public void onBookingSuccess(Appointments appointments) {
        BookingUtils.notifyUser(appointments.getUserId(), instructorName, appointments.getDeviceId(), appointments.getTimeStamp(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    BookingUtils.sendRequestByOk(getActivity(), "Booking Success", "Your booking has been confirmed.", appointments.getDeviceId());
                    if(driverAppointmentListener != null){
                        driverAppointmentListener.onDrivingAppointmentSuccess();
                    }else{
                    }
                }else{

                }
            }
        });

    }

    @Override
    public void onBookingFailed(String message) {
        MessageUtils.showMessage(getActivity(), message);
    }

    @Override
    public void onClick(View view) {
        if(view == logoutIcon){
            showAlertDialog(getActivity(), driverAppointmentListener);
        }
    }

    private void showAlertDialog(Context context, DriverAppointmentListener driverAppointmentListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.cancel();
            if(driverAppointmentListener !=null)
                driverAppointmentListener.onDriverLogoutClicked();

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.create();
        builder.show();
    }


    public interface DriverAppointmentListener {
        void onDrivingAppointmentSuccess();
        void onDriverLogoutClicked();

    }
}
