package com.driving_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.driving_app.R;
import com.driving_app.model.Instructor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class InstructorAdapter extends FirebaseRecyclerAdapter<Instructor, InstructorAdapter.InstructorViewHolder>
{


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public InstructorAdapter(@NonNull FirebaseRecyclerOptions<Instructor> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull InstructorViewHolder holder, int position, @NonNull Instructor model) {
        holder.instructorName.setText(model.getName());
        holder.instructorDescription.setText(model.getDrivingExperienceDetails());
        holder.instructorRating.setText(model.getRating());
        holder.instructorAvailability.setText(model.getInstructorAvailability());
    }

    @NonNull
    @Override
    public InstructorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_instructor, parent, false);
        return new InstructorViewHolder(rootView);
    }

    public class InstructorViewHolder extends RecyclerView.ViewHolder{

        private ImageView profileURL;
        private TextView instructorName, instructorDescription, instructorRating,instructorAvailability;
        public InstructorViewHolder(@NonNull View itemView) {
            super(itemView);
            profileURL = itemView.findViewById(R.id.profileURL);
            instructorName = itemView.findViewById(R.id.instructorName);
            instructorDescription = itemView.findViewById(R.id.instructorDescription);
            instructorRating =itemView.findViewById(R.id.rating);
            instructorAvailability = itemView.findViewById(R.id.instructorAvailability);
        }
    }
}
