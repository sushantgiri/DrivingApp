package com.driving_app.fragments;

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
import com.driving_app.adapter.MessagesAdapter;
import com.driving_app.model.MessagesModel;
import com.driving_app.utils.MessageUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;

public class AppointmentsFragment extends Fragment {

    public static AppointmentsFragment newInstance() {
        AppointmentsFragment fragment = new AppointmentsFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    private View view;
    private MessagesAdapter messagesAdapter;
    private RecyclerView appointmentsRecyclerView;
    private FirebaseFirestore database;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView noMessagesTV;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser  = firebaseAuth.getCurrentUser();
        database = FirebaseFirestore.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_appointments, container, false);

        noMessagesTV = view.findViewById(R.id.noMessages);

        appointmentsRecyclerView = view.findViewById(R.id.messagesRV);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        messagesAdapter = new MessagesAdapter();
        appointmentsRecyclerView.setAdapter(messagesAdapter);

        FirebaseFirestore.getInstance().collection("messageList")
                .document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        ArrayList<Object> messagesModels = (ArrayList<Object>) document.get("messages");

                        if(messagesModels != null ){
                            Collections.reverse(messagesModels);
                            messagesAdapter.addAll(messagesModels);
                        }
                        noMessagesTV.setVisibility(messagesModels != null && messagesModels.size() > 0 ?
                                View.GONE : View.VISIBLE);

                    }else{
                        MessageUtils.showMessage(getActivity(),"Could not fetch messages");
                    }
                });


        return view;
    }


}
