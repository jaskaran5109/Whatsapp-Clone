package com.example.whatsapp.Adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.Models.MessageModel;
import com.example.whatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{
    ArrayList<MessageModel> messageModels;
    Context context;
    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;
    String recId;
    public ChatAdapter(ArrayList<MessageModel> list, Context context) {
        this.messageModels = list;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recId) {
        this.messageModels = messageModels;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(viewType== SENDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        MessageModel messageModel=messageModels.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure want to delete this message")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                String sender=FirebaseAuth.getInstance().getUid()+recId;
                                database.getReference().child("Chats")
                                        .child(sender)
                                        .child(messageModel.getMessageID())
                                        .setValue(null);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                return false;
            }
        });
        if(holder.getClass()==SenderViewHolder.class)
        {
            ((SenderViewHolder)holder).senderMessage.setText(messageModel.getMessage());
        }
        else
        {
            ((ReceiverViewHolder)holder).receiverMessage.setText((messageModel.getMessage()));
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(messageModels.get(position).getUid().equals(FirebaseAuth.getInstance().getUid()))
            return SENDER_VIEW_TYPE;
        else
            return RECEIVER_VIEW_TYPE;

    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView receiverMessage,receiverTime;


        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMessage=itemView.findViewById(R.id.receiverText);
            receiverTime=itemView.findViewById(R.id.receiverTime);

        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView senderMessage,senderTime;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage=itemView.findViewById(R.id.senderText);
            senderTime=itemView.findViewById(R.id.senderTime);
        }
    }


}
