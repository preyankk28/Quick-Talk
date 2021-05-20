package io.quicktalk.agilean.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import io.quicktalk.agilean.Model.ChatModel;
import io.quicktalk.agilean.R;

public class ChatRecyclerAdapter extends FirebaseRecyclerAdapter<ChatModel, ChatRecyclerAdapter.myviewholder> {
    Context context;


    public ChatRecyclerAdapter(@NonNull FirebaseRecyclerOptions<ChatModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatrecyclercard, parent,false);


        return new myviewholder(view);
    }




    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull ChatModel model) {
        holder.message.setText(model.getMessage());
        holder.name.setText(model.getFullName());

    }

    public class myviewholder extends RecyclerView.ViewHolder{
         TextView name;
         TextView message;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.namemessagechat);
            message = itemView.findViewById(R.id.messagechat);









        }
    }
}
