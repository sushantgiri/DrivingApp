package com.driving_app.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.driving_app.model.Appointments;
import com.driving_app.model.Instructor;
import com.driving_app.model.MessagesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.common.net.MediaType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

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

    public static void fetchAppointments(String driverName, AppointmentFetchListener listener, int requestCode){
        FirebaseFirestore.getInstance().collection("instructorList")
                .document(driverName)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
//                        ArrayList<Appointments> appointments = (ArrayList<Appointments>) task.getResult().get("appointmentList");
                        ArrayList<HashMap<String, String>> appointments = (ArrayList<HashMap<String, String>>) task.getResult().get("appointmentList");
//                        HashMap<String, Appointments> hashMap = (HashMap<String, Appointments>) task.getResult().get("appointmentList");
//                        ArrayList<Appointments> appointments = new ArrayList<>();
//                        appointments.addAll(hashMap.values());


                        ArrayList<Appointments> upcomingAppointments = new ArrayList<>();
                        ArrayList<Appointments> pendingAppointments = new ArrayList<>();
                        if(appointments != null){
                            for (int i = 0; i < appointments.size(); i++) {

                                Appointments info = new Appointments();
                                info.setUserId(appointments.get(i).get("userId"));
                                info.setDeviceId(appointments.get(i).get("deviceId"));
                                info.setUserEmail(appointments.get(i).get("userEmail"));
                                info.setUserName(appointments.get(i).get("userName"));
                                info.setTimeStamp(appointments.get(i).get("timeStamp"));
                                info.setBookingAccepted(appointments.get(i).get("bookingAccepted"));
                                if(info.isBookingAccepted().equals("true")){
                                    upcomingAppointments.add(info);
                                }else{
                                    pendingAppointments.add(info);

                                }
                            }


                        }else{
                            listener.onFailed("Appointment is null");
                        }
                        if(requestCode == 1){
                            listener.onPendingAppointmentFetched(pendingAppointments);
                        }else{
                            listener.onUpcomingAppointmentFetched(upcomingAppointments);
                        }


                    }else{
                        listener.onFailed(task.getException().getMessage());
                    }
                });
    }

    public static void acceptBooking(Context context, Appointments appointments, String instructorName, BookingStatusListener bookingStatusListener){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Accepting Booking...");
        progressDialog.show();

        firebaseFirestore.collection("instructorList")
                .document(instructorName)
                .get().addOnCompleteListener(task -> {
                    progressDialog.cancel();
                    if (task.isSuccessful()) {
                        ArrayList<HashMap<String, String>> localAppointments = (ArrayList<HashMap<String, String>>) task.getResult().get("appointmentList");
                        ArrayList<Appointments> localAppointmentArrayList = new ArrayList<>();
                        if(localAppointments != null){
                            for (int i = 0; i < localAppointments.size(); i++) {

                                Appointments info = new Appointments();
                                info.setUserId(localAppointments.get(i).get("userId"));
                                info.setDeviceId(localAppointments.get(i).get("deviceId"));
                                info.setUserEmail(localAppointments.get(i).get("userEmail"));
                                info.setUserName(localAppointments.get(i).get("userName"));
                                info.setTimeStamp(localAppointments.get(i).get("timeStamp"));
                                info.setBookingAccepted(localAppointments.get(i).get("bookingAccepted"));
                                localAppointmentArrayList.add(info);
                            }
                        }else{
                            bookingStatusListener.onBookingFailed("Appointment is null");
                        }

                            for(Appointments localAppointment: localAppointmentArrayList){
                                if(localAppointment.getUserId().equals(appointments.getUserId())){
                                    localAppointment.setBookingAccepted("true");
                                }
                            }

                            firebaseFirestore.collection("instructorList")
                                    .document(instructorName)
                                    .update("appointmentList",localAppointmentArrayList)
                                    .addOnCompleteListener(task1 -> {
                                        if(task1.isSuccessful()){
                                            bookingStatusListener.onBookingSuccess(appointments);
                                        }else{
                                            bookingStatusListener.onBookingFailed(task1.getException().getMessage());
                                        }
                                    });

                        }else{
                            bookingStatusListener.onBookingFailed("Local Appointments not found.");
                        }



                });
    }

    public static void notifyUser(String userId, String instructorName, String firebaseId, String date,OnCompleteListener<Void> onCompleteListener){
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
                        messagesModel.setTitle("Appointment Booking Accepted!");
                        messagesModel.setMessage("Your booking with instructor " +instructorName + " has been confirmed for "+ date + ".");
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

    public static void sendRequestByOk(Context context, String tile, String message, String deviceToken) {
        try{
            RequestQueue queue = Volley.newRequestQueue(context);

            String url = "https://fcm.googleapis.com/fcm/send";

            JSONObject data = new JSONObject();
            data.put("title", tile);
            data.put("body", message);
            JSONObject notification_data = new JSONObject();
            notification_data.put("data", data);
            notification_data.put("to",deviceToken);

            JsonObjectRequest request = new JsonObjectRequest(url, notification_data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, error -> {

            }) {
                @Override
                public Map<String, String> getHeaders() {
                    String api_key_header_value = "Key=AIzaSyAv0aO6GHsl8PxIcNBdNGc3bvo17MMiE4w";
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", api_key_header_value);
                    return headers;
                }
            };

            queue.add(request);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

//    private void sendRequestToFCMServer(String registrationToken, String title, String body){
//        dataJson.put("body",body);
//        dataJson.put("title",title);
//        json.put("notification",dataJson);
//        json.put("to",regToken);
//        AndroidNetworking.post("https://fcm.googleapis.com/fcm/send")
//                .addBodyParameter("body", "You")
//                .addBodyParameter("lastname", "Shekhar")
//                .addBodyParameter("notification", "")
//                .addBodyParameter("to", "")
//                .setTag("test")
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                    }
//                });
//    }
//    private void sendNotification(final String regToken) {
//
//
//        new AsyncTask<Void,Void,Void>(){
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    JSONObject json=new JSONObject();
//                    JSONObject dataJson=new JSONObject();
//                    dataJson.put("body","Hi this is sent from device to device");
//                    dataJson.put("title","dummy title");
//                    json.put("notification",dataJson);
//                    json.put("to",regToken);
//                    RequestBody body = RequestBody.create(JSON, json.toString());
//                    Request request = new Request.Builder()
//                            .header("Authorization","key="+Constants.LEGACY_SERVER_KEY)
//                            .url("https://fcm.googleapis.com/fcm/send")
//                            .post(body)
//                            .build();
//                    Response response = client.newCall(request).execute();
//                    String finalResponse = response.body().string();
//                }catch (Exception e){
//                    //Log.d(TAG,e+"");
//                }
//                return null;
//            }
//        }.execute();
//
//    }


    public interface BookingStatusListener {
        void onBookingSuccess(Appointments appointments);
        void onBookingFailed(String message);
    }



    public interface AppointmentFetchListener{
        void onFailed(String message);
        void onUpcomingAppointmentFetched(ArrayList<Appointments> appointments);

        void onPendingAppointmentFetched(ArrayList<Appointments> appointments);
    }


}
