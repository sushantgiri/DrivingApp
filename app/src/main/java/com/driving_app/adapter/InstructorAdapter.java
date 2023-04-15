package com.driving_app.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.driving_app.R;
import com.driving_app.helpers.CircleImageView;
import com.driving_app.model.Instructor;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class InstructorAdapter extends FirestoreRecyclerAdapter<Instructor, InstructorAdapter.InstructorViewHolder>
{


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public InstructorAdapter(@NonNull FirestoreRecyclerOptions<Instructor> options) {
        super(options);
    }

    private InstructorAdapterListener instructorAdapterListener;

    public void setInstructorAdapterListener(InstructorAdapterListener instructorAdapterListener){
        this.instructorAdapterListener = instructorAdapterListener;
    }




    @Override
    protected void onBindViewHolder(@NonNull InstructorViewHolder holder, int position, @NonNull Instructor model) {
        holder.instructorName.setText(model.getName());
        holder.instructorDescription.setText(model.getSummary());
        holder.instructorRating.setText(model.getRating());
        holder.instructorAvailable.setVisibility(model.getInstructorAvailability() != null ? View.VISIBLE: View.GONE);
        holder.bookAppointmentButton.setOnClickListener(view -> {
            if(instructorAdapterListener != null){
                instructorAdapterListener.onAppointmentClicked(position, model);
            }
        });
        if(model.getProfileUrl() != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.getProfileUrl());
            Glide.with(holder.itemView.getContext())
                    .load(storageReference)
                    .centerCrop()
                    .into(holder.profileURL);
        }
        holder.rootView.setOnClickListener(v -> {
            if(instructorAdapterListener != null){
                instructorAdapterListener.onItemClicked(position, model);
            }
        });

    }


    @NonNull
    @Override
    public InstructorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_instructor, parent, false);
        return new InstructorViewHolder(rootView);
    }

    public static class InstructorViewHolder extends RecyclerView.ViewHolder{

        private final CircleImageView profileURL;
        private final TextView instructorName;
        private final TextView instructorDescription;
        private final TextView instructorRating;
        private final ImageView instructorAvailable;
        private final Button bookAppointmentButton;
        private final LinearLayout rootView;
        public InstructorViewHolder(@NonNull View itemView) {
            super(itemView);
            profileURL = itemView.findViewById(R.id.profileURL);
            instructorName = itemView.findViewById(R.id.instructorName);
            instructorDescription = itemView.findViewById(R.id.instructorDescription);
            instructorRating =itemView.findViewById(R.id.rating);
            instructorAvailable = itemView.findViewById(R.id.instructorAvailable);
            bookAppointmentButton = itemView.findViewById(R.id.bookAppointment);
            rootView =  itemView.findViewById(R.id.rootView);
        }
    }

    public interface InstructorAdapterListener{
        void onAppointmentClicked(int position, Instructor instructor);
        void onItemClicked(int position, Instructor instructor);
    }


}
