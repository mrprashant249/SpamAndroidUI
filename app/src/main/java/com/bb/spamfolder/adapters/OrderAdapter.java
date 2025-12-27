package com.bb.spamfolder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bb.spamfolder.R;
import com.bb.spamfolder.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private final List<Order> orders;
    private static OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderAdapter(List<Order> orders, OnOrderClickListener listener) {
        this.orders = orders;
        OrderAdapter.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        final TextView tvOrderId;
        final TextView tvAmount;
        final TextView tvStatus;
        final TextView tvEta;
        final TextView tvAddress;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvEta = itemView.findViewById(R.id.tvEta);
            tvAddress = itemView.findViewById(R.id.tvAddress);
        }

        void bind(Order order) {
            tvOrderId.setText("#" + order.getOrderId());
            tvAmount.setText(order.getPrice());
            tvStatus.setText(order.getStatus());
            tvEta.setText(order.getEta());
            tvAddress.setText(order.getAddress());

            // Set status background and color based on status
            int backgroundRes;
            int textColor = switch (order.getStatus().toLowerCase()) {
                case "pending" -> {
                    backgroundRes = R.drawable.bg_status_pending;
                    yield itemView.getContext().getColor(R.color.blue_button);
                }
                case "delivered" -> {
                    backgroundRes = R.drawable.bg_status_delivered;
                    yield itemView.getContext().getColor(R.color.green);
                }
                case "out for delivery" -> {
                    backgroundRes = R.drawable.bg_status_out_delivery;
                    yield itemView.getContext().getColor(R.color.orange);
                }
                case "cancelled" -> {
                    backgroundRes = R.drawable.bg_status_cancelled;
                    yield itemView.getContext().getColor(R.color.red_button);
                }
                default -> {
                    backgroundRes = R.drawable.bg_status_pending;
                    yield itemView.getContext().getColor(R.color.text_secondary);
                }
            };
            tvStatus.setBackgroundResource(backgroundRes);
            tvStatus.setTextColor(textColor);

            // Add click listener to the entire item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onOrderClick(order);
                }
            });
        }
    }
} 