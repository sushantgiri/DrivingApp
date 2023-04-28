package com.driving_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.driving_app.R;
import com.driving_app.adapter.InstructorAdapter;
import com.driving_app.helpers.WrapContentLinearLayoutManager;
import com.driving_app.model.Instructor;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment implements View.OnClickListener, InstructorAdapter.InstructorAdapterListener {


    public static HomeFragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    private View rootView;
    private RecyclerView instructorRecyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore database;
    private InstructorAdapter instructorAdapter;
    private TextView displayNameText;
    private TextView searchResultsCount;
    private ImageView profileImage;
    private HomeListener homeListener;

    public void setHomeListener(HomeListener homeListener){
        this.homeListener = homeListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser  = firebaseAuth.getCurrentUser();
        database = FirebaseFirestore.getInstance();

    }



    @Override
    public void onStart() {
        super.onStart();
        if(instructorAdapter != null){
            instructorAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(instructorAdapter != null){
            instructorAdapter.stopListening();
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        displayNameText = rootView.findViewById(R.id.displayName);
        profileImage = rootView.findViewById(R.id.profileImage);
        profileImage.setOnClickListener(this);
        searchResultsCount = rootView.findViewById(R.id.searchResultsCount);
        searchResultsCount.setVisibility(View.GONE);
        if(firebaseUser != null){
            displayNameText.setText("Hello, \n" + firebaseUser.getDisplayName());
            if(firebaseUser.getPhotoUrl() != null){
                Glide.with(this)
                        .load(firebaseUser.getPhotoUrl())
                        .circleCrop()
                        .into(profileImage);
            }
        }
        instructorRecyclerView = rootView.findViewById(R.id.instructorsList);
        instructorRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));

        Query query = database.collection("instructorList")
                .orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Instructor> options = new FirestoreRecyclerOptions.Builder<Instructor>()
                .setQuery(query, Instructor.class)
                .build();

        instructorAdapter = new InstructorAdapter(options);
        instructorAdapter.setInstructorAdapterListener(this);
        instructorRecyclerView.setAdapter(instructorAdapter);
        return rootView;

    }

    @Override
    public void onClick(View view) {
        if(view == profileImage){
            showLogoutDialog();
        }
    }

    private void showLogoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Logout");
        builder.setCancelable(false);
        builder.setMessage("Do you want to confirm the logout?");
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            firebaseAuth.signOut();
            if(homeListener != null){
                homeListener.onLogoutClicked();
            }


        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    @Override
    public void onAppointmentClicked(int position, Instructor instructor) {
        if(homeListener != null){
            homeListener.onBookingAppointmentClicked(position,instructor);
        }
    }

    @Override
    public void onItemClicked(int position, Instructor instructor) {
       if(homeListener != null){
           homeListener.onItemClicked(position, instructor);
       }
    }

    public interface HomeListener {
        void onLogoutClicked();

        void onItemClicked(int position, Instructor instructor);
        void onBookingAppointmentClicked(int position, Instructor instructor);
    }
}