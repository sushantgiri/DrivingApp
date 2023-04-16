package com.driving_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.driving_app.R;
import com.driving_app.model.Appointments;

import java.util.ArrayList;

public class DriverAppointmentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_PENDING = 1, TYPE_UPCOMING = 2;
    private ArrayList<Appointments> driverAppointments;
    private UpComingAppointmentListener upComingAppointmentListener;

    public void setUpComingAppointmentListener(UpComingAppointmentListener listener){
        this.upComingAppointmentListener = listener;
    }


    public DriverAppointmentsAdapter() {
        driverAppointments = new ArrayList<>();
    }

    public void addAll(ArrayList<Appointments> appointments){
        if(appointments != null){
            this.driverAppointments = appointments;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_PENDING){
            View pendingView = inflater.inflate(R.layout.item_booking_pending, parent, false);
            viewHolder = new PendingAppointmentViewHolder(pendingView);
        }else {
            View upcomingView = inflater.inflate(R.layout.item_upcoming_appointment,parent,false);
            viewHolder = new UpcomingAppointmentViewHolder(upcomingView);
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Appointments appointments = driverAppointments.get(position);
        if(holder.getItemViewType() == TYPE_PENDING){
            PendingAppointmentViewHolder viewHolder = (PendingAppointmentViewHolder) holder;
            viewHolder.userDetails.setText(
                    "Username: " + appointments.getUserName() + "\n" +
                    "Email: " + appointments.getUserEmail() + "\n "
                    );
            viewHolder.acceptButton.setOnClickListener(v -> {
                if(upComingAppointmentListener != null){
                    upComingAppointmentListener.onBookingAccepted(appointments);
                }
            });
        }else{
            UpcomingAppointmentViewHolder upcomingAppointmentViewHolder = (UpcomingAppointmentViewHolder) holder;
            upcomingAppointmentViewHolder.getUserName().setText(appointments.getUserName());
            upcomingAppointmentViewHolder.getUserEmail().setText(appointments.getUserEmail());
            upcomingAppointmentViewHolder.getBookingTime().setText(appointments.getTimeStamp());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(driverAppointments.get(position).isBookingAccepted().equals("false")){
            return TYPE_PENDING;
        }else{
            return TYPE_UPCOMING;
        }
    }

    @Override
    public int getItemCount() {
        return driverAppointments.size();
    }

    public class PendingAppointmentViewHolder extends RecyclerView.ViewHolder {

        private TextView userDetails;
        private Button acceptButton;
        public PendingAppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            userDetails = itemView.findViewById(R.id.userDetails);
        }

        public TextView getUserDetails() {
            return userDetails;
        }

        public Button getAcceptButton() {
            return acceptButton;
        }
    }

    public class UpcomingAppointmentViewHolder extends RecyclerView.ViewHolder {

        private TextView userName, userEmail, bookingTime;
        public UpcomingAppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.username);
            userEmail = itemView.findViewById(R.id.userEmail);
            bookingTime = itemView.findViewById(R.id.bookingTime);
        }

        public TextView getUserName() {
            return userName;
        }

        public TextView getUserEmail() {
            return userEmail;
        }

        public TextView getBookingTime() {
            return bookingTime;
        }
    }

    public interface UpComingAppointmentListener{
        void onBookingAccepted(Appointments appointments);
    }
}
