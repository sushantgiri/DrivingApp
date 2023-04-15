package com.driving_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.driving_app.R;
import com.driving_app.model.MessagesModel;

import java.util.ArrayList;
import java.util.HashMap;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {


   private ArrayList<Object> messages;

   public MessagesAdapter(){
       messages = new ArrayList<>();
   }

   public void addAll(ArrayList<Object> messages){
       this.messages =    messages;
       notifyDataSetChanged();
   }



    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        return new MessagesViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        HashMap<String, String> hashMap = (HashMap<String, String>) messages.get(position);
//        MessagesModel messagesModel = (MessagesModel) messages.get(position);
        holder.title.setText(hashMap.get("title"));
        holder.message.setText(hashMap.get("message"));
    }


    @Override
    public int getItemCount() {
        return messages.size();
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
