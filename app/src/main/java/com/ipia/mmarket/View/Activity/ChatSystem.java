package com.ipia.mmarket.View.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ipia.mmarket.View.Adapters.MessageAdapter;
import com.ipia.mmarket.Models.AllMethods;
import com.ipia.mmarket.Models.Message;
import com.ipia.mmarket.Models.User;
import com.ipia.mmarket.R;

import java.util.ArrayList;
import java.util.List;

public class ChatSystem extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference messagedb;
    MessageAdapter messageAdapter;
    User u;
    List<Message> message;

    RecyclerView rvMessage;
    EditText etMessage;
    ImageButton imgButton;
    ImageView returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_system);
        returnFeature();
        initMsg();
    }

    private void returnFeature() {
        returnButton = findViewById(R.id.chat_return);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initMsg() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://mmarket-ipia-rma2-default-rtdb.europe-west1.firebasedatabase.app");
        u = new User();


        rvMessage = findViewById(R.id.rvMessage);
        etMessage = findViewById(R.id.etMessage);
        imgButton = findViewById(R.id.btnSend);
        imgButton.setOnClickListener(this);

        message = new ArrayList<>();
        rvMessage.setLayoutManager(new LinearLayoutManager(this));

        messagedb = database.getReference("messages");

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    u = snapshot.getValue(User.class);
                    if (u != null) {
                        u.setUid(currentUser.getUid());
                        AllMethods.name = u.getName();
                        setupAdapter(u.getName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void setupAdapter(String username) {
        messageAdapter = new MessageAdapter(this, message, messagedb, username);
        rvMessage.setAdapter(messageAdapter);

        messagedb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message newMessage = snapshot.getValue(Message.class);
                if (newMessage != null) {
                    newMessage.setKey(snapshot.getKey());
                    message.add(newMessage);
                    messageAdapter.notifyItemInserted(message.size() - 1);
                    rvMessage.scrollToPosition(message.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message updatedMessage = snapshot.getValue(Message.class);
                if (updatedMessage != null) {
                    updatedMessage.setKey(snapshot.getKey());
                    for (int i = 0; i < message.size(); i++) {
                        if (message.get(i).getKey().equals(updatedMessage.getKey())) {
                            message.set(i, updatedMessage);
                            messageAdapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Message removedMessage = snapshot.getValue(Message.class);
                if (removedMessage != null) {
                    removedMessage.setKey(snapshot.getKey());
                    for (int i = 0; i < message.size(); i++) {
                        if (message.get(i).getKey().equals(removedMessage.getKey())) {
                            message.remove(i);
                            messageAdapter.notifyItemRemoved(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!TextUtils.isEmpty(etMessage.getText().toString())) {
            Message newMessage = new Message(etMessage.getText().toString(), u.getName());
            etMessage.setText("");
            messagedb.push().setValue(newMessage);
        } else {
            Toast.makeText(getApplicationContext(), "You can't send blank messages", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            u.setUid(currentUser.getUid());
            u.setEmail(currentUser.getEmail());

            database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    u = snapshot.getValue(User.class);
                    if (u != null) {
                        u.setUid(currentUser.getUid());
                        AllMethods.name = u.getName();
                    } else {
                        u = new User();
                        u.setUid(currentUser.getUid());
                        u.setName("Anonymous User");
                        u.setEmail(currentUser.getEmail());
                        database.getReference("Users").child(currentUser.getUid()).setValue(u);
                        AllMethods.name = u.getName();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    DisplayUtils.showFieldDialog(ChatSystem.this, "Error: Connection cancelled, please try again");
                }
            });
        }
    }
}



