package com.ipia.mmarket.View.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.ipia.mmarket.Models.Message;
import com.ipia.mmarket.R;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private Context context;
    private List<Message> messages;
    private DatabaseReference messageDb;
    private String loggedInUsername;

    public MessageAdapter(Context context, List<Message> messages, DatabaseReference messageDb, String loggedInUsername) {
        this.context = context;
        this.messages = messages;
        this.messageDb = messageDb;
        this.loggedInUsername = loggedInUsername;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);

        String name = message.getName();
        String msg = message.getMessage();

        holder.tvTitle.setText(!TextUtils.isEmpty(name) ? name : "Unknown User");
        holder.tvMessage.setText(!TextUtils.isEmpty(msg) ? msg : "No message content");


        if ("admin".equals(name)) {
            holder.tvTitle.setText("MMarket Helper");
            holder.userAvatar.setVisibility(View.GONE);
            holder.adminAvatar.setVisibility(View.VISIBLE);
            holder.adminVerified.setVisibility(View.VISIBLE);
        }else{
            holder.userAvatar.setVisibility(View.VISIBLE);
            holder.adminAvatar.setVisibility(View.GONE);
            holder.adminVerified.setVisibility(View.GONE);
        }

        if (name != null && name.equals(loggedInUsername)) {
            holder.ibDelete.setVisibility(View.VISIBLE);
        } else {
            if ("admin".equals(loggedInUsername) || "MMarket Helper".equals(loggedInUsername))
            {
                holder.ibDelete.setVisibility(View.VISIBLE);
            }else holder.ibDelete.setVisibility(View.GONE);

        }

        holder.ibDelete.setOnClickListener(v -> {
            messageDb.child(message.getKey()).removeValue();
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvMessage;
        ImageView userAvatar, adminAvatar, adminVerified;
        ImageButton ibDelete;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.message);
            ibDelete = itemView.findViewById(R.id.ibDelete);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            adminAvatar = itemView.findViewById(R.id.admin_avatar);
            adminVerified = itemView.findViewById(R.id.admin_verified);
        }
    }
}




