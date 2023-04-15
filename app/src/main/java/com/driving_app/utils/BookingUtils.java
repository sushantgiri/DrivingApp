package com.driving_app.utils;

import com.driving_app.model.Appointments;
import com.driving_app.model.Instructor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public final class BookingUtils {

    private BookingUtils(){

    }

    public static Appointments isBookingAvailable(List<Appointments> appointmentsList ){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(appointmentsList != null){
            for (Appointments appoint:
                 appointmentsList) {
                if(appoint.getUserId().equals(firebaseUser.getUid())){
                    return appoint;
                }
            }
        }

        return null;

    }

    public static Appointments createAppointments(String selectedDate){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Appointments appointments = new Appointments();
        appointments.setUserEmail(firebaseUser.getEmail());
        appointments.setUserName(firebaseUser.getDisplayName());
        appointments.setUserId(firebaseUser.getUid());
        appointments.setTimeStamp(selectedDate);
        return appointments;
    }

    public static void bookUser(Instructor instructor, @NonNull OnCompleteListener<Void> listener){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("instructorList")
                .document(instructor.getName())
                .update("appointmentList", instructor.getAppointmentList())
                .addOnCompleteListener(listener);




        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("/appointmentList");
    }

    private static List<String> getDisabledDates(List<Appointments> appointmentsList){
        List<String> disabledTime = new ArrayList<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(appointmentsList != null && appointmentsList.size() > 0){
            for(Appointments appointments: appointmentsList){
                if(!appointments.getUserId().equals(firebaseUser.getUid())){
                    disabledTime.add(appointments.getTimeStamp());
                }
            }
        }

        return disabledTime;
    }

    public static boolean containsDate(String selectedDate, List<Appointments> appointments){
        List<String> disabledDates = getDisabledDates(appointments);
        if(disabledDates.contains(selectedDate)){
            return true;
        }

        return false;

    }


}
