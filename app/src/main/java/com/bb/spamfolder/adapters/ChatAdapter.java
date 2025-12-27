package com.bb.spamfolder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bb.spamfolder.R;
import com.bb.spamfolder.models.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private final List<Chat> chats;

    public ChatAdapter(List<Chat> chats) {
        this.chats = chats;
    }

    @Override
    public int getItemViewType(int position) {
        return chats.get(position).isSent() ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SentViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_sent, parent, false)
            );
        }
        return new ReceivedViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_received, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat chat = chats.get(position);

        if (holder instanceof SentViewHolder viewHolder) {
            viewHolder.bind(chat);
        } else if (holder instanceof ReceivedViewHolder viewHolder) {
            viewHolder.bind(chat);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    static class SentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvMessage;
        private final TextView tvTime;
        private final ImageView ivDelivered;
        private final View chatLayout;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivDelivered = itemView.findViewById(R.id.ivDelivered);
            chatLayout = itemView.findViewById(R.id.chatLayout);
        }

        void bind(Chat chat) {
            tvMessage.setText(chat.message());
            tvTime.setText(chat.time());

            if (chat.isDelivered()) {
                ivDelivered.setVisibility(View.VISIBLE);
                ivDelivered.setImageResource(chat.deliverImage());
            } else {
                ivDelivered.setVisibility(View.GONE);
            }

            if (chat.showDate()) {
                tvDate.setVisibility(View.VISIBLE);
                tvDate.setText(chat.date());
                chatLayout.setVisibility(View.GONE);
            } else {
                tvDate.setVisibility(View.GONE);
                chatLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvMessage;
        private final TextView tvTime;
        private final View chatLayout;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            chatLayout = itemView.findViewById(R.id.chatLayout);
        }

        void bind(Chat chat) {
            tvMessage.setText(chat.message());
            tvTime.setText(chat.time());

            if (chat.showDate()) {
                tvDate.setVisibility(View.VISIBLE);
                tvDate.setText(chat.date());
                chatLayout.setVisibility(View.GONE);
            } else {
                tvDate.setVisibility(View.GONE);
                chatLayout.setVisibility(View.VISIBLE);
            }
        }
    }
} 