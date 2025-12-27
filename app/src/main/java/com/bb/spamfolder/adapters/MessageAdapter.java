package com.bb.spamfolder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bb.spamfolder.R;
import com.bb.spamfolder.models.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final List<Message> messages;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Message message);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);

        holder.ivAvatar.setImageResource(message.drawableImage());
        holder.tvName.setText(message.name());
        holder.tvMessage.setText(message.message());
        holder.tvTime.setText(message.time());

        if (message.messageCount() > 0) {
            holder.tvMessageCount.setVisibility(View.VISIBLE);
            holder.tvMessageCount.setText(String.valueOf(message.messageCount()));
        } else {
            holder.tvMessageCount.setVisibility(View.GONE);
        }

        if (message.isDelivered()) {
            holder.ivDelivered.setVisibility(View.VISIBLE);
            holder.ivDelivered.setImageResource(message.deliverImage());
        } else {
            holder.ivDelivered.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivAvatar;
        private final TextView tvName;
        private final TextView tvMessage;
        private final TextView tvTime;
        private final TextView tvMessageCount;
        private final ImageView ivDelivered;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvMessageCount = itemView.findViewById(R.id.tvMessageCount);
            ivDelivered = itemView.findViewById(R.id.ivDelivered);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(messages.get(position));
                }
            });
        }
    }
} 