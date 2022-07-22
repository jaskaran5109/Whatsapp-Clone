package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsapp.Adaptors.ChatAdapter;
import com.example.whatsapp.Models.MessageModel;
import com.example.whatsapp.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        final String senderId=auth.getUid();
        String receiveId=getIntent().getStringExtra("userId");
        String userName=getIntent().getStringExtra("userName");
        String profilePic=getIntent().getStringExtra("profilePic");

        binding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.ic_avatar_svgrepo_com).into(binding.profileImage);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<MessageModel> messageModels=new ArrayList<>();
        final ChatAdapter chatAdapter=new ChatAdapter(messageModels,this,receiveId);

        binding.chatRecyclearView.setAdapter(chatAdapter);

        LinearLayoutManager linearlayoutManager=new LinearLayoutManager(this);
        binding.chatRecyclearView.setLayoutManager(linearlayoutManager);

        final String senderRoom=senderId+receiveId;
        final String receiverRoom=receiveId+senderId;

        database.getReference().child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for(DataSnapshot dataSnapshot1: snapshot.getChildren())
                        {
                            MessageModel model=dataSnapshot1.getValue(MessageModel.class);
                            model.setMessageID(dataSnapshot1.getKey());
                            messageModels.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.senderTextMessage.getText().toString().isEmpty())
                {
                    binding.senderTextMessage.setText("Cannot be empty");
                    return;
                }
                String msg=binding.senderTextMessage.getText().toString();
                final MessageModel model=new MessageModel(senderId,msg);
                model.setTimestamp(new Date().getTime());
                binding.senderTextMessage.setText("");
                database.getReference().child("Chats").child(senderRoom)
                .push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("Chats").child(receiverRoom)
                                .push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });
    }
}