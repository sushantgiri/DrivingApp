package com.driving_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.driving_app.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MessagesAdapter extends FirestoreRecyclerAdapter<MessagesModel, MessagesAdapter.MessagesViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MessagesAdapter(@NonNull FirestoreRecyclerOptions<MessagesModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessagesViewHolder holder, int position, @NonNull MessagesModel model) {
        holder.title.setText(model.getTitle());
        holder.message.setText(model.getMessage());
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        return new MessagesViewHolder(rootView);
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder{
        private TextView title, message;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTV);
            message  = itemView.findViewById(R.id.messageTV);

        }
    }
}
