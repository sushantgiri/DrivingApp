package com.driving_app.utils;

import com.driving_app.model.MessagesModel;
import com.driving_app.model.Appointments;
import com.driving_app.model.Instructor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Appointments createAppointments(String selectedDate, String token){
        Appointments appointments = new Appointments();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        appointments.setUserEmail(firebaseUser.getEmail());
        appointments.setUserName(firebaseUser.getDisplayName());
        appointments.setUserId(firebaseUser.getUid());
        appointments.setTimeStamp(selectedDate);
        appointments.setDeviceId(token);
        return appointments;
    }

    public static void bookUser(Instructor instructor, @NonNull OnCompleteListener<Void> listener){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("instructorList")
                .document(instructor.getName())
                .update("appointmentList", instructor.getAppointmentList())
                .addOnCompleteListener(listener);
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


    public static void initiateMessaging(String instructorName, String userId, String firebaseId, String date, OnCompleteListener<Void> onCompleteListener){

        FirebaseFirestore.getInstance().collection("messageList")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        ArrayList<MessagesModel> messagesModels = (ArrayList<MessagesModel>) document.get("messages");
                        ArrayList<MessagesModel> models;
                        Map<String, ArrayList<MessagesModel>> docData = new HashMap<>();
                        if(messagesModels != null && messagesModels.size() > 0){
                            models = messagesModels;
                        }else{
                            models = new ArrayList<>();
                        }

                        MessagesModel messagesModel = new MessagesModel();
                        messagesModel.setTitle("Appointment Booking Pending!");
                        messagesModel.setMessage("Your booking with instructor " +instructorName + " has been confirmed for "+ date + ". Please wait for the instructor to accept your booking.");
                        messagesModel.setDeviceId(firebaseId);
                        messagesModel.setUserId(userId);
                        messagesModel.setTimeStamp(date);
                        models.add(messagesModel);
                        docData.put("messages", models);

                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        firebaseFirestore.collection("messageList")
                                .document(userId)
                                .set(docData)
                                .addOnCompleteListener(onCompleteListener);
                    }else{

                    }
                });

    }


}
